Feature: Bio-Engineer Registration and Management
  As a system administrator
  I want to register and manage bio-engineers in the system
  So that they can create formulas and develop freeze-dried snack products

  Background:
    Given the database is clean

  Scenario: Successfully register a new bio-engineer with all details
    Given I have bio-engineer details with name "Dr. Sarah Johnson", email "sarah.johnson@lifeisbananas.com", phone "+32-123-456-789", and specialization "Fruit Processing"
    When I register the bio-engineer
    Then the bio-engineer should be saved in the database
    And I should be able to retrieve the bio-engineer by email "sarah.johnson@lifeisbananas.com"
    And the bio-engineer should have the name "Dr. Sarah Johnson"

  Scenario: Register a bio-engineer with minimal information
    Given I have bio-engineer details with name "John Doe" and email "john.doe@lifeisbananas.com"
    When I register the bio-engineer
    Then the bio-engineer should be saved in the database
    And the bio-engineer should have no phone number
    And the bio-engineer should have no specialization

  Scenario: Prevent duplicate email registration
    Given a bio-engineer with email "existing@lifeisbananas.com" already exists
    When I try to register another bio-engineer with email "existing@lifeisbananas.com"
    Then the registration should fail with a duplicate email error

  Scenario: Update bio-engineer information
    Given a bio-engineer with email "update.test@lifeisbananas.com" exists
    When I update the bio-engineer's specialization to "Freeze-Drying Expert"
    And I update the bio-engineer's phone to "+32-999-888-777"
    Then the bio-engineer should have specialization "Freeze-Drying Expert"
    And the bio-engineer should have phone "+32-999-888-777"

  Scenario Outline: Validate email format during registration
    Given I have bio-engineer details with name "Test Engineer" and email "<email>"
    When I register the bio-engineer
    Then the registration should <result>

    Examples:
      | email                          | result  |
      | valid.email@lifeisbananas.com | succeed |
      | another.valid@company.be      | succeed |
      | invalid.email                  | fail    |
      | @nouser.com                    | fail    |
      | no-at-sign.com                 | fail    |