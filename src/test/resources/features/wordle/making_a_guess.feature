Feature: Making a guess

  As a player,
  I want to be able to guess a 5-letter word in up to 6 tries
  So that I can attempt to win the game.

  Rule: Guesses must be a valid five letter word from the English dictionary

    Scenario Outline: Check that a proposed word is allowed
      Given the player is playing Wordle
      When the player proposes "<Word>"
      Then the word should or should not be allowed: <Is Allowed>

      Examples: Valid words should appear in the English dictionary
        | Word   | Is Allowed | Reason                  |
        | QUEST  | Yes        | .                       |
        | ABCDE  | No         | Not a real word         |
        | CHIEN  | No         | French word             |
        | TOOL!  | No         | Punctuation not allowed |
        | TINY   | No         | Too Short               |
        | Bigger | No         | Too Long                |
      Examples: Both UK and US spelling are allowed
        | Word  | Is Allowed | Reason      |
        | FIBRE | Yes        | UK spelling |
        | FIBER | Yes        | US spelling |
        | ENROL | Yes        | UK spelling |
        | LITRE | Yes        | UK spelling |







