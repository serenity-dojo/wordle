import { unicodeSplit } from './words'

export type CharStatus = 'absent' | 'present' | 'correct'

export const getStatuses = (
  gameStatus: string[],
  guesses: string[]
): { [key: string]: CharStatus } => {
  const charObj: { [key: string]: CharStatus } = {}

  guesses.forEach((word, index) => {
    unicodeSplit(word).forEach((letter, i) => {
      if (gameStatus[index][i] === 'GRAY')
        return (charObj[letter] = 'absent')
      else if (gameStatus[index][i] === 'YELLOW')
        return (charObj[letter] = 'present')
      else if (gameStatus[index][i] === 'GREEN')
        return (charObj[letter] = 'correct')
    })
  })

  return charObj
}

export const getGuessStatuses = (
  gameStatus: string[],
  number: number,
  guess: string
): CharStatus[] => {
  const splitGuess = unicodeSplit(guess)

  const statuses: CharStatus[] = Array.from(Array(guess.length))

  // handle all correct cases first
  splitGuess.forEach((letter, i) => {
    if (gameStatus[number][i] === 'GRAY')
      statuses[i] = 'absent'
    else if (gameStatus[number][i] === 'YELLOW')
      statuses[i] = 'present'
    else if (gameStatus[number][i] === 'GREEN')
      statuses[i] = 'correct'
  })

  return statuses
}
