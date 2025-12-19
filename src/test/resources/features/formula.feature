Feature: Formula Creation with Ingredients
  As a bio-engineer
  I want to create product formulas with ingredients
  So that I can develop new freeze-dried snack recipes

  Background:
    Given the database is clean
    And a bio-engineer with name "Dr. Sarah Johnson" and email "sarah@lifeisbananas.com" exists

  Scenario: Create a simple fruit formula with one ingredient
    Given a fruit raw material "Banana" with 89.0 calories exists
    When the bio-engineer creates a formula named "Simple Banana Chips" with version "1.0"
    And adds ingredient "Banana" with quantity 500.0 grams at sequence 1
    Then the formula should be saved in the database
    And the formula should have 1 ingredient
    And the formula should be created by "sarah@lifeisbananas.com"

  Scenario: Create a complex formula with multiple ingredients
    Given a fruit raw material "Banana" with 89.0 calories exists
    And a fruit raw material "Strawberry" with 32.0 calories exists
    And a vegetable raw material "Spinach" with 23.0 calories exists
    When the bio-engineer creates a formula named "Green Smoothie Mix" with version "2.1"
    And adds ingredient "Banana" with quantity 300.0 grams at sequence 1
    And adds ingredient "Strawberry" with quantity 200.0 grams at sequence 2
    And adds ingredient "Spinach" with quantity 100.0 grams at sequence 3
    Then the formula should be saved in the database
    And the formula should have 3 ingredients
    And the ingredients should be in the correct sequence

  Scenario: Cannot create formula without bio-engineer
    Given no bio-engineer is selected
    When I try to create a formula named "Orphan Formula" with version "1.0"
    Then the formula creation should fail with a missing bio-engineer error

  Scenario: Update formula preparation method
    Given a formula named "Banana Chips" exists
    When I update the preparation method to "1. Slice bananas. 2. Freeze-dry at -40°C for 24 hours."
    Then the formula should have the updated preparation method
    And the last modified date should be updated

  Scenario Outline: Add different types of ingredients with various units
    Given a <material_type> raw material "<material_name>" exists
    And a formula named "Test Formula" exists
    When I add ingredient "<material_name>" with quantity <quantity> <unit> at sequence 1
    Then the ingredient should be added successfully
    And the ingredient should have quantity <quantity> and unit <unit>

    Examples:
      | material_type | material_name | quantity | unit       |
      | fruit         | Banana        | 500.0    | GRAM       |
      | fruit         | Mango         | 1.5      | KILOGRAM   |
      | vegetable     | Carrot        | 250.0    | GRAM       |
      | fruit         | Apple         | 3.0      | PIECE      |
      | vegetable     | Kale          | 15.0     | PERCENTAGE |