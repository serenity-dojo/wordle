Feature: Playing Wordle

  Players have to guess a five-letter word.
  - Correctly placed letters are shown as green (*)
  - Incorrectly placed letters are shown as yellow (+)
  - Letters not appearing in the word are represented as grey (-)

  Rule: Correctly placed letters appear in green
    Background:
      Given the target word is:
        | Goal | B | L | A | N | D |

    Scenario: Correctly placed letters
      When the player enters the following letters:
        | Attempt | P | R | I | N | T |
      Then the squares should be colored as follows:
        | Result | - | - | - | * | - |

    Scenario: Letters that are present but not in the right place
      When the player enters the following letters:
        | Attempt | B | R | A | I | N |
      Then the squares should be colored as follows:
        | Result | * | - | * | - | + |

  Rule: Repeated letters in the wrong spot appear in grey
    Scenario: Two incorrectly placed letters
      Given the target word is:
        | Goal | B | L | A | N | D |
      When the player enters the following letters:
        | Attempt | L | A | B | E | L |
      Then the squares should be colored as follows:
        | Result | + | + | + | - | - |





