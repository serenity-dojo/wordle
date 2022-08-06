Feature: Playing Wordle

  Players have to guess a five-letter word.
  - Correctly placed letters are shown as green (*)
  - Incorrectly placed letters are shown as yellow (+)
  - Letters not appearing in the word are represented as grey (-)

  Rule: Correctly placed letters appear in green
    Background:
      Given the target word is:
        | B | L | A | N | D |

    Scenario: Correctly placed letters
      When the player enters the following letters:
        | P | R | I | N | T |
      Then the squares should be colored as follows:
        | - | - | - | * | - |

    Scenario: Letters that are present but not in the right place
      When the player enters the following letters:
        | B | R | A | I | N |
      Then the squares should be colored as follows:
        | * | - | * | - | + |

    Scenario Outline: Letters that do not form valid words should be rejected
      When the player attempts to enter the following letters:
        | <Letter 1> | <Letter 2> | <Letter 3> | <Letter 4> | <Letter 5> |
      Then the attempt should be rejected
      Examples:
        | Letter 1 | Letter 2 | Letter 3 | Letter 4 | Letter 5 | Reason         |
        | T        | O        |          |          |          | Word too short |
        | A        | B        | C        | D        | E        | Not a word     |
        | F        | A        | S        | T        | !        | Not a word     |

  Rule: Repeated letters in the wrong spot appear in grey
    Scenario: Two incorrectly placed letters
      Given the target word is:
        | B | L | A | N | D |
      When the player enters the following letters:
        | L | A | B | E | L |
      Then the squares should be colored as follows:
        | + | + | + | - | - |

  Rule: The player wins when they find the right word in 6 or less tries
    Scenario: Player guesses the right word
      Given the target word is:
        | B | L | A | N | D |
      When the player enters the following letters:
        | B | E | A | S | T |
        | B | R | A | I | N |
        | B | L | A | N | D |
      Then the squares should be colored as follows:
        | * | - | * | - | - |
        | * | - | * | - | + |
        | * | * | * | * | * |
      And the player should win the game

    Scenario: Player runs out of tries
      Given the target word is:
        | B | L | A | N | D |
      When the player enters the following letters:
        | B | E | A | S | T |
        | B | R | A | I | N |
        | P | L | A | I | N |
        | P | L | A | N | E |
        | P | L | A | N | T |
        | P | L | A | N | K |
      Then the player should lose the game
