Feature: Mock Coinbase Exchange Rates API

  Scenario: pathMatches('/v2/exchange-rates')
    * configure responseHeaders = { 'Content-Type': 'application/json' }
    * def response = read('classpath:response/coinbase/coinbase_exchange_rates_response.json')
    * def status = 200