import {
  CalendarIcon,
  ChartBarIcon,
  CogIcon,
  InformationCircleIcon,
  ClockIcon,
  TableIcon,
} from "@heroicons/react/outline";
import { toast } from "react-toastify";

import { ENABLE_ARCHIVED_GAMES } from "../../constants/settings";
import { GAME_TITLE } from "../../constants/strings";
import {start_new_game, find_hint, get_game_statistics, get_game_history, get_leaderboard} from "../../api/api";
import { useAuth } from "../../provider/authProvider";

export const Navbar = ({
  setIsInfoModalOpen,
  setIsStatsModalOpen,
  setIsDatePickerModalOpen,
  setIsSettingsModalOpen,
  setIsHistoryModalOpen,
  setIsLeaderboardModalOpen,
  handleNewGame,
}) => {
  const { signOut } = useAuth();

  const newStartGame = async () => {
    const result = await start_new_game();
    if (result === true) handleNewGame();
    else toast.error("error");
  };

  const findHint = async () => {
    find_hint();
  };

  const logOut = async () => {
    await signOut();
  };

  const getGameStatistics = async () => {
    const data = await get_game_statistics();
    setIsStatsModalOpen(true, data);
  };

  const getGameHistory = async () => {
    const data = await get_game_history();
    setIsHistoryModalOpen(true, data);
  }

  const getLeaderBoard = async () => {
    const data = await get_leaderboard();
    setIsLeaderboardModalOpen(true, data);
  }

  return (
    <div className="navbar">
      <div className="flex h-[3rem] items-center justify-between px-5 short:h-auto">
        <div className="flex items-center gap-2 w-1/3">
          <InformationCircleIcon
            className="h-6 w-6 cursor-pointer dark:stroke-white"
            onClick={() => setIsInfoModalOpen(true)}
          />
          {ENABLE_ARCHIVED_GAMES && (
            <CalendarIcon
              className="ml-3 h-6 w-6 cursor-pointer dark:stroke-white"
              onClick={() => setIsDatePickerModalOpen(true)}
            />
          )}
          <button
            type="button"
            className="inline-flex w-auto items-center justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-center text-base font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 sm:text-sm"
            onClick={logOut}
          >
            Logout
          </button>
        </div>
        <p className="game-headline text-xl text-center font-bold dark:text-white w-1/3">
          {GAME_TITLE}
        </p>
        <div className="right-icons items-center justify-end w-1/3">
          <div className="flex items-center mr-4">
            <button
              type="button"
              className="inline-flex w-auto items-center justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-center text-base font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 sm:text-sm"
              onClick={newStartGame}
            >
              New Game
            </button>
          </div>
          <div className="flex items-center mr-4">
            <button
              type="button"
              className="inline-flex w-auto items-center justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-center text-base font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 sm:text-sm"
              onClick={findHint}
            >
              Hint
            </button>
          </div>
          <ChartBarIcon
            className="mr-3 h-6 w-6 cursor-pointer dark:stroke-white"
            onClick={() => getGameStatistics()}
          />
          <ClockIcon
              className="mr-3 h-6 w-6 cursor-pointer dark:stroke-white"
              onClick={() => getGameHistory()}
          />
          <TableIcon
              className="mr-3 h-6 w-6 cursor-pointer dark:stroke-white"
              onClick={() => getLeaderBoard()}
          />
          <CogIcon
            className="h-6 w-6 cursor-pointer dark:stroke-white"
            onClick={() => setIsSettingsModalOpen(true)}
          />
        </div>
      </div>
      <hr></hr>
    </div>
  );
};
