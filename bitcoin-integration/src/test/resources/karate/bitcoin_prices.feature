Feature: Verify bitcoin prices

  Background:
    Given url baseUrl
    Given path '/api/bitcoin/prices'

  Scenario: Is bitcoin price over 100k in all currencies?

    When method GET
    Then status 200
    And match $ == {"prices":{"EUR":127665.52,"GBP":123405.99,"USD":129230.555}}

  Scenario: Is bitcoin price over 100k while reading expected results from file?

    When method GET
    Then status 200
    And match $ == read('file:src/test/resources/karate/bitcoin_prices.json')

  Scenario Outline: Is bitcoin price over 100k in <currency>?

    When method GET
    Then status 200
    And match $ contains deep <expected>

    Examples:
      | currency | expected                      |
      | "USD"    | {"prices":{"USD":129230.555}} |
      | "EUR"    | {"prices":{"EUR":127665.52}}  |
      | "GBP"    | {"prices":{"GBP":123405.99}}  |
