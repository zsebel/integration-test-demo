Feature: BitcoinPriceIndex endpoint Karate tests

  Background:
    Given url baseUrl
    And path 'api/v2/bitcoin'

  Scenario: API schema validation
    * def expectedApiSchema = { bitcoinPriceIndex: '#string' }

    Given param currency = 'USD'
    When method GET
    Then status 200
    And match $ == expectedApiSchema

  Scenario: Bitcoin price index is returned in US dollars

    Given param currency = 'USD'
    When method GET
    Then status 200
    And match $.bitcoinPriceIndex == '$95,472.37'

  Scenario Outline:  Bitcoin price index in returned in all supported currencies

    Given param currency = <currency>
    When method GET
    Then status 200
    And match $.bitcoinPriceIndex == <formatted price>

    Examples:
      | currency | formatted price              |
      | "USD"    | "$95,472.37"                 |
      | "EUR"    | "83.887,85 \u20AC"        |
      | "GBP"    | "£71,265.38"                 |
      | "CHF"    | "CHF 78\u2019721.20"      |
      | "HUF"    | "33 914 580,15 Ft"  |

  Scenario: Invalid Currency format validation error
    Given param currency = 'Dollars'
    When method GET
    Then status 400
    And match $.status == 400
    And match $.path == '/api/v2/bitcoin'
    And match $.errors[0].requestParameter == 'currency'
    And match $.errors[0].message contains 'Invalid currency format [Dollars]'

  Scenario: Not supported Currency validation error
    Given param currency = 'JPN'
    When method GET
    Then status 400
    And match $.status == 400
    And match $.path == '/api/v2/bitcoin'
    And match $.errors[0].requestParameter == 'currency'
    And match $.errors[0].message contains 'Invalid currency [JPN]'