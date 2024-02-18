import { getGuessStatuses } from '../../lib/statuses'
import { unicodeSplit } from '../../lib/words'
import { Cell } from './Cell'

export const CompletedRow = ({ gameStatus, guess, number, isRevealing }) => {
  const statuses = getGuessStatuses(gameStatus, number, guess)
  const splitGuess = unicodeSplit(guess)

  return (
    <div className="completed-row mb-1 flex justify-center">
      {splitGuess.map((letter, i) => (
        <Cell
          key={i}
          value={letter}
          status={statuses[i]}
          position={i}
          isRevealing={isRevealing}
          isCompleted
        />
      ))}
    </div>
  )
}
