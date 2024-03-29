import {
  BEST_STREAK_TEXT,
  CURRENT_STREAK_TEXT,
  SUCCESS_RATE_TEXT,
  TOTAL_TRIES_TEXT,
} from '../../constants/strings'

const StatItem = ({
  label,
  value,
}) => {
  return (
    <div className="m-1 w-1/4 items-center justify-center dark:text-white">
      <div className="text-3xl font-bold">{value}</div>
      <div className="text-xs">{label}</div>
    </div>
  )
}

export const StatBar = ({ gameStats }) => {
  return (
    <div className="my-2 flex justify-center">
      <StatItem label={TOTAL_TRIES_TEXT} value={gameStats.totalTries} />
      <StatItem label={SUCCESS_RATE_TEXT} value={`${gameStats.percentageSuccessRate}%`} />
      <StatItem label={CURRENT_STREAK_TEXT} value={gameStats.currentWinningStreak} />
      <StatItem label={BEST_STREAK_TEXT} value={gameStats.bestWinningStreak} />
    </div>
  )
}
