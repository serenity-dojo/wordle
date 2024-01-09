import { getGuessStatuses } from '../../lib/statuses'
import { unicodeSplit } from '../../lib/words'
import { Cell } from './Cell'

type Props = {
  gameStatus: string[]
  guess: string
  number: number
  isRevealing?: boolean
}

export const CompletedRow = ({ gameStatus, guess, number, isRevealing }: Props) => {
  const statuses = getGuessStatuses(gameStatus, number, guess)
  const splitGuess = unicodeSplit(guess)

  return (
    <div className="mb-1 flex justify-center">
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
