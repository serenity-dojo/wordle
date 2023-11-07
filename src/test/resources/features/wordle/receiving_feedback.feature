Feature: Receiving Feedback

  As a player,
  I want feedback on my guesses so that I can adjust my next guess accordingly.

  Rule: If a letter only appears once in the target word and more than once in the proposed word, it should be black for the subsequent places
    Example: The one where a letter is in the wrong place
      Given the target word is "BENCH"
      When the player proposes "TEETH"
      Then the feedback should be:
        | BLACK | GREEN | BLACK | BLACK | GREEN |

    Scenario Outline: Letters should appear in different colours according to the following rules
      Given the target word is "<target>"
      When the player proposes "<guess>"
      Then the feedback should be:
        | <feedback_1> | <feedback_2> | <feedback_3> | <feedback_4> | <feedback_5> |

      Examples: Letters that do not exist in the word are color coded black.
        | target | guess | feedback_1 | feedback_2 | feedback_3 | feedback_4 | feedback_5 |
        | FOUND  | CHAIR | BLACK      | BLACK      | BLACK      | BLACK      | BLACK      |
        | WEARY  | CHAIR | BLACK      | BLACK      | BLACK      | BLACK      | BLACK      |

      Examples: Letters that exist in the word and are in the correct position are color coded green.
        | target | guess | feedback_1 | feedback_2 | feedback_3 | feedback_4 | feedback_5 |
        | FOUND  | FINDS | GREEN      | BLACK      | BLACK      | BLACK      | BLACK      |
        | WEARY  | WEARY | GREEN      | GREEN      | GREEN      | GREEN      | GREEN      |

      Examples: Letters that exist in the word but are not in the correct position are color coded yellow.
        | target | guess | feedback_1 | feedback_2 | feedback_3 | feedback_4 | feedback_5 |
        | QUEST  | CHUTE | BLACK      | BLACK      | YELLOW     | YELLOW     | YELLOW     |
        | WEARY  | SCENE | BLACK      | BLACK      | YELLOW     | BLACK      | BLACK      |

      Examples: If a letter only appears once in the target word and more than once in the proposed word, it should be black for the subsequent places
        | target | guess | feedback_1 | feedback_2 | feedback_3 | feedback_4 | feedback_5 |
        | BENCH  | TEETH | BLACK      | GREEN      | BLACK      | BLACK      | GREEN      |
