package dev.zsebel.bitcoin.integration.resilience;

import dev.zsebel.bitcoin.integration.support.Currency;
import dev.zsebel.bitcoin.integration.support.Header;
import dev.zsebel.bitcoin.integration.support.Times;
import dev.zsebel.bitcoin.integration.wiremock.BaseWireMockIntegrationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This setup connects to a running server to perform full, end-to-end HTTP test
 * and CoinbaseClient requests are sent to a {@link WireMockServer} running on localhost
 * and CircuitBreaker states are tested in various scenarios.
 */
@ActiveProfiles("integration")
class CircuitBreakerIntegrationTest extends BaseWireMockIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerIntegrationTest.class);
    private static final String CIRCUIT_BREAKER_STATE = "CircuitBreaker State";
    private static final String STATE_OPEN = "Open";
    private static final String STATE_CLOSED = "Closed";
    private static final String CIRCUIT_BREAKER_METRICS_TEMPLATE = """
        
        ------------------------------------------------------
        CircuitBreaker metrics:
          State: %s
          Number of buffered calls (in sliding window): %d
          Number of failed calls (in sliding window): %d
          Number of successful calls (in sliding window): %d
          Failure rate: %.2f%%
          Number of calls not permitted: %d
        ------------------------------------------------------
        """;

    private CircuitBreaker circuitBreaker;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeAll
    void beforeAll() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("coinbase");
    }

    @AfterEach
    void setUp() {
        circuitBreaker.reset();
    }

    @Test
    void testCoinbaseCircuitBreakerShouldRemainInClosedStateWhenTheNumberOfErrorsAreLessThanTheMinimumNumberOfCalls() {
        // GIVEN
        setupCoinbaseExchangeRatesServerErrorStub();

        // WHEN
        performBitcoinPriceIndexApiCall().repeat(Times.FOUR);

        // THEN
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
        assertEquals(4, metrics.getNumberOfFailedCalls());
        assertEquals(0, metrics.getNumberOfNotPermittedCalls());
        assertEquals(-1.0, metrics.getFailureRate());
        printCircuitBreakerMetrics(circuitBreaker.getMetrics());
    }

    @Test
    void testCoinbaseCircuitBreakerShouldTransitionToOpenStateWhenTheNumberOfErrorsAreMoreThanTheMinimumNumberOfCalls() {
        // GIVEN
        setupCoinbaseExchangeRatesServerErrorStub();

        // WHEN
        performBitcoinPriceIndexApiCall().repeat(Times.FIVE);

        // THEN
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
        assertEquals(5, metrics.getNumberOfFailedCalls());
        assertEquals(0, metrics.getNumberOfNotPermittedCalls());
        assertEquals(100.0, metrics.getFailureRate());
        printCircuitBreakerMetrics(circuitBreaker.getMetrics());
    }

    @Test
    void testCoinbaseCircuitBreakerShouldNotPermitCallsWhenTheNumberOfErrorsExceedsTheThreshold() {
        // GIVEN
        setupCoinbaseExchangeRatesServerErrorStub();

        // WHEN
        performBitcoinPriceIndexApiCall().repeat(Times.SIX);

        // THEN
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
        assertEquals(5, metrics.getNumberOfFailedCalls());
        assertEquals(1, metrics.getNumberOfNotPermittedCalls());
        assertEquals(100.0, metrics.getFailureRate());
        printCircuitBreakerMetrics(circuitBreaker.getMetrics());
    }

    @Test
    void testCoinbaseCircuitBreakerShouldTransitionToHalfOpenStateWhenWaitDurationInOpenStateIsElapsed() {
        // GIVEN
        setupCoinbaseExchangeRatesServerErrorStubFor(CIRCUIT_BREAKER_STATE, STATE_CLOSED);
        setupCoinbaseExchangeRatesSuccessStubFor(CIRCUIT_BREAKER_STATE, STATE_OPEN);
        wireMockServer.setScenarioState(CIRCUIT_BREAKER_STATE, STATE_CLOSED);

        // WHEN
        // Make 5 calls to hit the minimum number of calls to transition CB's state from CLOSED to OPEN
        performBitcoinPriceIndexApiCall().repeat(Times.FIVE);
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
        // Change mock response from server error to valid response
        wireMockServer.setScenarioState(CIRCUIT_BREAKER_STATE, STATE_OPEN);

        // waitDurationInOpenState to transition CB's state from OPEN to HALF_OPEN
        waitFor(Duration.ofSeconds(2));

        // Make a new call to trigger CB
        performBitcoinPriceIndexApiCall().repeat(Times.ONCE);

        // THEN
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        assertEquals(1, metrics.getNumberOfBufferedCalls());
        assertEquals(0, metrics.getNumberOfFailedCalls());
        assertEquals(-1.0, metrics.getFailureRate());
        printCircuitBreakerMetrics(circuitBreaker.getMetrics());
    }

    @Test
    void testAllCoinbaseCircuitBreakerStates() {
        // GIVEN
        setupCoinbaseExchangeRatesServerErrorStubFor(CIRCUIT_BREAKER_STATE, STATE_CLOSED);
        setupCoinbaseExchangeRatesSuccessStubFor(CIRCUIT_BREAKER_STATE, STATE_OPEN);
        wireMockServer.setScenarioState(CIRCUIT_BREAKER_STATE, STATE_CLOSED);

        // WHEN
        // State transition from CLOSED to OPEN
        performBitcoinPriceIndexApiCall().repeat(Times.FIVE);
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
        wireMockServer.setScenarioState(CIRCUIT_BREAKER_STATE, STATE_OPEN);

        // State transition from OPEN to HALF_OPEN
        waitFor(Duration.ofSeconds(2));
        performBitcoinPriceIndexApiCall().repeat(Times.ONCE);
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        // State transition from HALF_OPEN to CLOSED
        performBitcoinPriceIndexApiCall().repeat(Times.ONCE);

        // THEN
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
        printCircuitBreakerMetrics(circuitBreaker.getMetrics());
    }

    private void setupCoinbaseExchangeRatesServerErrorStubFor(final String scenario, final String state) {
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .inScenario(scenario)
            .whenScenarioStateIs(state)
            .willReturn(serverError()));
    }

    private void setupCoinbaseExchangeRatesSuccessStubFor(final String scenario, final String state) {
        stubFor(WireMock.get(urlEqualTo(COINBASE_EXCHANGE_RATES_URL))
            .inScenario(scenario)
            .whenScenarioStateIs(state)
            .willReturn(ok()
                .withHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(COINBASE_RESPONSE_MOCK_JSON_FILE)));
    }

    private CallExecutor performBitcoinPriceIndexApiCall() {
        return times -> {
            for (int i = 0; i < times.value(); i++) {
                bitcoinPriceIndexApiTestClient.getBitcoinPriceIndexIn(Currency.USD);
            }
        };
    }

    private void printCircuitBreakerMetrics(final CircuitBreaker.Metrics metrics) {
        String formattedCircuitBreakerMetrics = String.format(
            CIRCUIT_BREAKER_METRICS_TEMPLATE,
            circuitBreaker.getState(),
            metrics.getNumberOfBufferedCalls(),
            metrics.getNumberOfFailedCalls(),
            metrics.getNumberOfSuccessfulCalls(),
            metrics.getFailureRate(),
            metrics.getNumberOfNotPermittedCalls()
        );
        LOGGER.info(formattedCircuitBreakerMetrics);
    }

    private void waitFor(final Duration duration) {
        try {
            long millis = duration.getSeconds() * 1000;
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            LOGGER.error("The current thread was interrupted", exception);
        }
    }

    @FunctionalInterface
    interface CallExecutor {
        void repeat(Times times);
    }
}
