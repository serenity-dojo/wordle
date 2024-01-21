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
  const winDistribution = [];
  winDistribution.push(gameStats.guessDistribution["1"]);
  winDistribution.push(gameStats.guessDistribution["2"]);
  winDistribution.push(gameStats.guessDistribution["3"]);
  winDistribution.push(gameStats.guessDistribution["4"]);
  winDistribution.push(gameStats.guessDistribution["5"]);
  winDistribution.push(gameStats.guessDistribution["6"]);
  const maxValue = Math.max(...winDistribution, 1)
  console.log(maxValue)

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
