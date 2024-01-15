import { unicodeSplit } from './words'

export const getStatuses = (
  gameStatus,
  guesses
) => {
  const charObj = {}

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
  gameStatus,
  number,
  guess
) => {
  const splitGuess = unicodeSplit(guess)

  const statuses = Array.from(Array(guess.length))

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
