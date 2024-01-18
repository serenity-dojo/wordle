import {
  CalendarIcon,
  ChartBarIcon,
  CogIcon,
  InformationCircleIcon,
} from "@heroicons/react/outline";
import { toast } from "react-toastify";

import { ENABLE_ARCHIVED_GAMES } from "../../constants/settings";
import { GAME_TITLE } from "../../constants/strings";
import { start_new_game, find_hint } from "../../api/api";
import { useAuth } from "../../provider/authProvider";

export const Navbar = ({
  setIsInfoModalOpen,
  setIsStatsModalOpen,
  setIsDatePickerModalOpen,
  setIsSettingsModalOpen,
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
        <p className="text-xl text-center font-bold dark:text-white w-1/3">
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
            onClick={() => setIsStatsModalOpen(true)}
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
