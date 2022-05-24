Feature: Is Bitcoin price over 100k?
  Everybody wants to know when to sell

  Scenario: Bitcoin price is above 100k
    Given Bitcoin price is above 100k
    When I fetch Bitcoin prices
    Then Bitcoin price should be more than 100k

  Scenario Outline: Bitcoin price is above 100k in all currencies
    Given Bitcoin price is above 100k
    When I fetch Bitcoin prices
    Then Bitcoin price should be "<amount>" in "<currency>"

    Examples:
      | amount     | currency |
      | 129230.555 | USD      |
      | 123405.99  | GBP      |
      | 127665.52  | EUR      |