Feature: BitcoinPriceIndex endpoint Cucumber tests

  Scenario: Bitcoin price index is returned in US dollars
    Given A Coinbase mock response containing Bitcoin exchange rates
    When I fetch BitcoinPriceIndex in USD currency
    Then The formatted Bitcoin's price should be "$95,472.37"

  Scenario Outline: Bitcoin price index in returned in all supported currencies
    Given A Coinbase mock response containing Bitcoin exchange rates
    When I fetch BitcoinPriceIndex in <currency> currency
    Then The formatted Bitcoin's price should be <amount>

    Examples:
      | amount                       | currency |
      | "$95,472.37"                 | USD      |
      | "£71,265.38"                 | GBP      |
      | "83.887,85 €"             | EUR      |
      | "CHF 78’721.20"           | CHF      |
      | "33 914 580,15 Ft"  | HUF      |