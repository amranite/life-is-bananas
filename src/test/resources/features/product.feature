Feature: Product Development Lifecycle
  As a bio-engineer
  I want to track product development status
  So that I can manage the product lifecycle from development to production

  Background:
    Given the database is clean
    And a bio-engineer with name "Dr. Sarah Johnson" and email "sarah@lifeisbananas.com" exists
    And a formula named "Banana Chips Formula" with version "1.0" exists

  Scenario: Create a new product in development status
    When the bio-engineer creates a product named "Banana Chips Original"
    And the product has description "Crispy freeze-dried banana slices"
    And the product has shelf life of 365 days
    And the product has weight of 50 grams
    And the product has batch number "BATCH-2024-001"
    Then the product should be saved in the database
    And the product status should be "IN_DEVELOPMENT"
    And the product should be based on formula "Banana Chips Formula"

  Scenario: Successfully transition product from development to tested
    Given a product named "Banana Chips" with status "IN_DEVELOPMENT" exists
    When I mark the product as tested
    Then the product status should be "TESTED"
    And the status transition should be recorded

  Scenario: Approve a tested product for production
    Given a product named "Banana Chips" with status "TESTED" exists
    When I approve the product
    Then the product status should be "APPROVED"
    And the product should be ready for production

  Scenario: Start production for an approved product
    Given a product named "Banana Chips" with status "APPROVED" exists
    When I start production for the product
    Then the product status should be "IN_PRODUCTION"
    And the production date should be set to today

  Scenario: Cannot skip testing phase
    Given a product named "Banana Chips" with status "IN_DEVELOPMENT" exists
    When I try to approve the product directly
    Then the status transition should fail
    And the product status should remain "IN_DEVELOPMENT"

  Scenario: Cannot start production without approval
    Given a product named "Banana Chips" with status "TESTED" exists
    When I try to start production for the product
    Then the status transition should fail
    And the product status should remain "TESTED"

  Scenario Outline: Product status workflow validation
    Given a product named "Test Product" with status "<current_status>" exists
    When I try to transition to status "<target_status>"
    Then the transition should <result>
    And the product status should be "<final_status>"

    Examples:
      | current_status  | target_status   | result  | final_status    |
      | IN_DEVELOPMENT  | TESTED          | succeed | TESTED          |
      | TESTED          | APPROVED        | succeed | APPROVED        |
      | APPROVED        | IN_PRODUCTION   | succeed | IN_PRODUCTION   |
      | IN_DEVELOPMENT  | APPROVED        | fail    | IN_DEVELOPMENT  |
      | IN_DEVELOPMENT  | IN_PRODUCTION   | fail    | IN_DEVELOPMENT  |
      | TESTED          | IN_PRODUCTION   | fail    | TESTED          |