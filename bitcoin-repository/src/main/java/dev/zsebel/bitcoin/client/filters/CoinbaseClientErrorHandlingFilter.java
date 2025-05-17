package dev.zsebel.bitcoin.client.filters;

import dev.zsebel.bitcoin.client.exception.CoinbaseClientException;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientInternalServerError;
import dev.zsebel.bitcoin.client.exception.CoinbaseClientTimeoutException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Component
public class CoinbaseClientErrorHandlingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {
        return next.exchange(request)
            .onErrorResume(throwable -> {
                Mono<ClientResponse> error = Mono.error(throwable);
                if (isTimeoutException(throwable)) {
                    error = Mono.error(new CoinbaseClientTimeoutException("Http connection timed out."));
                }
                return error;
            })
            .flatMap(response -> {
                if (response.statusCode().isError()) {
                    return response.createException().flatMap(exception -> {
                        if (response.statusCode().is5xxServerError()) {
                            return Mono.error(new CoinbaseClientInternalServerError("Coinbase internal server error:" + exception.getMessage()));
                        } else if (response.statusCode().is4xxClientError()) {
                            return Mono.error(new CoinbaseClientException("Coinbase client error: " + exception.getMessage()));
                        }
                        return Mono.error(exception);
                    });
                }
                return Mono.just(response);
            });
    }

    private boolean isTimeoutException(final Throwable throwable) {
        return throwable instanceof WebClientRequestException
            && throwable.getCause() instanceof ConnectTimeoutException
            || throwable.getCause() instanceof ReadTimeoutException;
    }
}
