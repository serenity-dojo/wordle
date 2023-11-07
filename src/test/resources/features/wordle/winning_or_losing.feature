Feature: Winning or losing

  As a player,
  I want to know when I've won or lost the game so that I can either celebrate or try again.

  Acceptance Criteria
  - When a player has used all of their guesses but have not guessed the word correctly, the Lose screen is displayed.
  - When a player guesses the word correctly before using all of their guesses, the Win screen is displayed.

  Rule: When a player has used all of their guesses but have not guessed the word correctly, the Lose screen is displayed.
    Scenario: No more than 6 tries are allowed
      Given the target word is "ARDOR"
      When the player proposes
        | Beast |
        | Brand |
        | Brine |
        | Cress |
        | Goat  |
      Then the result should be: “Lose”

  Rule: No more than 6 tries are allowed.
    Example: Player enters 6 wrong guesses and loses
      Given Wordle word is "ARDOR"
      When the Player attempts the following guesses:
        | BEAST |
        | BRAND |
        | BRINE |
        | CRESS |
        | GROAT |
        | ARSON |
      Then Game is "Lost"

  Rule: When a player guesses the word correctly before using all of their guesses, the Win screen is displayed.
    Example: Player enters less than 6 wrong guesses and wins
      Given Wordle word is "<target>"
      When Player enters "<guesses>"
      Then Game is "Win"
        | target | guesses                       |
        | ARDOR  | BEAST,CRESS,GROAT,ARSON,ARDOR |
        | BENCH  | AVAST,SHINE,CHEST,BENCH       |
        | BENCH  | AVAST,SHINE,BENCH             |
        | BENCH  | AVAST, BENCH                  |


  Rule: When a player has made five guesses without guessing the correct word, the game is over and the Lose screen is shown.
    Example: Player enters 5 wrong guesses
      Given the target word is SNACK
      When the player guesses: BUMP,CHINS,WAFTS,SHIFT,SNIPS
      Then the result should be Lose



