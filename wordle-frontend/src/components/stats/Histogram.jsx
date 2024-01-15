import { Progress } from './Progress'

const isCurrentDayStatRow = (
  isLatestGame,
  isGameWon,
  numberOfGuessesMade,
  i
) => {
  return isLatestGame && isGameWon && numberOfGuessesMade === i + 1
}

export const Histogram = ({
  gameStats,
  isLatestGame,
  isGameWon,
  numberOfGuessesMade,
}) => {
  const winDistribution = gameStats.winDistribution
  const maxValue = Math.max(...winDistribution, 1)

  return (
    <div className="justify-left m-2 columns-1 text-sm dark:text-white">
      {winDistribution.map((value, i) => (
        <Progress
          key={i}
          index={i}
          isCurrentDayStatRow={isCurrentDayStatRow(
            isLatestGame,
            isGameWon,
            numberOfGuessesMade,
            i
          )}
          size={90 * (value / maxValue)}
          label={String(value)}
        />
      ))}
    </div>
  )
}
