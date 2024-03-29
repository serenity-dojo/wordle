import { ClockIcon } from "@heroicons/react/outline";
import { format } from "date-fns";
import { default as GraphemeSplitter } from "grapheme-splitter";
import { useEffect, useState } from "react";
import Div100vh from "react-div-100vh";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { AlertContainer } from "../../components/alerts/AlertContainer";
import { Grid } from "../../components/grid/Grid";
import { Keyboard } from "../../components/keyboard/Keyboard";
import { DatePickerModal } from "../../components/modals/DatePickerModal";
import { InfoModal } from "../../components/modals/InfoModal";
import { MigrateStatsModal } from "../../components/modals/MigrateStatsModal";
import { SettingsModal } from "../../components/modals/SettingsModal";
import { StatsModal } from "../../components/modals/StatsModal";
import { HistoryModal } from "../../components/modals/HistoryModal";
import { Navbar } from "../../components/navbar/Navbar";
import {
  DATE_LOCALE,
  DISCOURAGE_INAPP_BROWSERS,
  LONG_ALERT_TIME_MS,
  MAX_CHALLENGES,
  REVEAL_TIME_MS,
  WELCOME_INFO_MODAL_MS,
} from "../../constants/settings";
import {
  CORRECT_WORD_MESSAGE,
  DISCOURAGE_INAPP_BROWSER_TEXT,
  GAME_COPIED_MESSAGE,
  NOT_ENOUGH_LETTERS_MESSAGE,
  SHARE_FAILURE_TEXT,
  WIN_MESSAGES,
  WORD_NOT_FOUND_MESSAGE,
} from "../../constants/strings";
import { useAlert } from "../../context/AlertContext";
import { isInAppBrowser } from "../../lib/browser";
import {
  getStoredIsHighContrastMode,
  loadGameStateFromLocalStorage,
  saveGameStateToLocalStorage,
  setStoredIsHighContrastMode,
} from "../../lib/localStorage";
import { addStatsForCompletedGame, loadStats } from "../../lib/stats";
import {
  getGameDate,
  getIsLatestGame,
  isWinningWord,
  setGameDate,
  solution,
  solutionGameDate,
  unicodeLength,
} from "../../lib/words";
import {
  attempt_word,
  get_answer,
  start_new_game,
  get_game_statistics,
} from "../../api/api";
import {LeaderboardModal} from "../../components/modals/LeaderboardModal.jsx";

function Game() {
  const isLatestGame = getIsLatestGame();
  const gameDate = getGameDate();
  const prefersDarkMode = window.matchMedia(
    "(prefers-color-scheme: dark)"
  ).matches;

  const {
    showError: showErrorAlert,
    showSuccess: showSuccessAlert,
    hiddenError: hiddenErrorAlert,
  } = useAlert();
  const [currentGuess, setCurrentGuess] = useState("");
  const [isGameWon, setIsGameWon] = useState(false);
  const [isInfoModalOpen, setIsInfoModalOpen] = useState(false);
  const [isStatsModalOpen, setIsStatsModalOpen] = useState(false);
  const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);
  const [isLeaderboardModalOpen, setIsLeaderboardModalOpen] = useState(false);
  const [isDatePickerModalOpen, setIsDatePickerModalOpen] = useState(false);
  const [isMigrateStatsModalOpen, setIsMigrateStatsModalOpen] = useState(false);
  const [isSettingsModalOpen, setIsSettingsModalOpen] = useState(false);
  const [currentRowClass, setCurrentRowClass] = useState("");
  const [isGameLost, setIsGameLost] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(
    localStorage.getItem("theme")
      ? localStorage.getItem("theme") === "dark"
      : prefersDarkMode
      ? true
      : false
  );
  const [isHighContrastMode, setIsHighContrastMode] = useState(
    getStoredIsHighContrastMode()
  );
  const [isRevealing, setIsRevealing] = useState(false);
  const [guesses, setGuesses] = useState(() => {
    const loaded = loadGameStateFromLocalStorage(isLatestGame);
    if (loaded?.solution !== solution) {
      return [];
    }
    const gameWasWon = loaded.guesses.includes(solution);
    if (gameWasWon) {
      setIsGameWon(true);
    }
    if (loaded.guesses.length === MAX_CHALLENGES && !gameWasWon) {
      setIsGameLost(true);
      showErrorAlert(CORRECT_WORD_MESSAGE(solution), {
        persist: true,
      });
    }
    return loaded.guesses;
  });
  const [gameStatus, setGameStatus] = useState(() => {
    const status = localStorage.getItem("gameStatus");
    return JSON.parse(status);
  });
  const [answer, setAnswer] = useState("");

  const [stats, setStats] = useState(() => loadStats());
  const [statsData, setstatsData] = useState(null);
  const [historyData, setHistoryData] = useState(null);
  const [leaderboardData, setLeaderboardData] = useState(null);

  useEffect(() => {
    const newStartGame = async () => {
      const result = await start_new_game();
      if (result === true) handleNewGame();
      else toast.error("error");
    };

    if (localStorage.getItem("isLogined") === "true") {
      localStorage.setItem("isLogined", "false");
      newStartGame();
    }
  }, []);

  useEffect(() => {
    // if no game state on load,
    // show the user the how-to info modal
    if (!loadGameStateFromLocalStorage(true)) {
      setTimeout(() => {
        setIsInfoModalOpen(true);
      }, WELCOME_INFO_MODAL_MS);
    }
  });

  useEffect(() => {
    DISCOURAGE_INAPP_BROWSERS &&
      isInAppBrowser() &&
      showErrorAlert(DISCOURAGE_INAPP_BROWSER_TEXT, {
        persist: false,
        durationMs: 7000,
      });
  }, [showErrorAlert]);

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }

    if (isHighContrastMode) {
      document.documentElement.classList.add("high-contrast");
    } else {
      document.documentElement.classList.remove("high-contrast");
    }
  }, [isDarkMode, isHighContrastMode]);

  const handleDarkMode = (isDark) => {
    setIsDarkMode(isDark);
    localStorage.setItem("theme", isDark ? "dark" : "light");
  };

  const handleHighContrastMode = (isHighContrast) => {
    setIsHighContrastMode(isHighContrast);
    setStoredIsHighContrastMode(isHighContrast);
  };

  const clearCurrentRowClass = () => {
    setCurrentRowClass("");
  };

  useEffect(() => {
    saveGameStateToLocalStorage(getIsLatestGame(), { guesses, solution });
  }, [guesses]);

  useEffect(() => {
    if (isGameWon) {
      handleGameWon();
    }

    if (isGameLost) {
      handleGameLost();
    }
  }, [isGameWon, isGameLost, showSuccessAlert]);

  const handleGameWon = async () => {
    const data = await get_game_statistics();
    setstatsData(data);

    const winMessage =
      WIN_MESSAGES[Math.floor(Math.random() * WIN_MESSAGES.length)];
    const delayMs = REVEAL_TIME_MS * solution.length;

    showSuccessAlert(winMessage, {
      delayMs,
      onClose: () => setIsStatsModalOpen(true),
    });
  };

  const handleGameLost = async () => {
    const data = await get_game_statistics();
    setstatsData(data);

    setTimeout(() => {
      setIsStatsModalOpen(true);
    }, (solution.length + 1) * REVEAL_TIME_MS);
  };

  const onChar = (value) => {
    if (
      unicodeLength(`${currentGuess}${value}`) <= solution.length &&
      guesses.length < MAX_CHALLENGES &&
      !isGameWon
    ) {
      setCurrentGuess(`${currentGuess}${value}`);
    }
  };

  const onDelete = () => {
    setCurrentGuess(
      new GraphemeSplitter().splitGraphemes(currentGuess).slice(0, -1).join("")
    );
  };

  const onEnter = async () => {
    if (isGameWon || isGameLost) {
      return;
    }

    if (!(unicodeLength(currentGuess) === 5)) {
      setCurrentRowClass("jiggle");
      return showErrorAlert(NOT_ENOUGH_LETTERS_MESSAGE, {
        onClose: clearCurrentRowClass,
      });
    }

    const result = await attempt_word(currentGuess);
    if (result?.data !== undefined) {
      console.log(result?.data);
      setGameStatus(result?.data);
      localStorage.setItem("gameStatus", JSON.stringify(result?.data));
    }

    if (result.response?.status === 403) {
      setCurrentRowClass("jiggle");
      return showErrorAlert(WORD_NOT_FOUND_MESSAGE, {
        onClose: clearCurrentRowClass,
      });
    }

    setIsRevealing(true);
    // turn this back off after all
    // chars have been revealed
    setTimeout(() => {
      setIsRevealing(false);
    }, REVEAL_TIME_MS * solution.length);

    console.log(result);
    const winningWord = isWinningWord(result?.data[result?.data.length - 1]);

    if (
      unicodeLength(currentGuess) === solution.length &&
      guesses.length < MAX_CHALLENGES &&
      !isGameWon
    ) {
      setGuesses([...guesses, currentGuess]);
      setCurrentGuess("");

      if (winningWord) {
        if (isLatestGame) {
          setStats(addStatsForCompletedGame(stats, guesses.length));
        }
        return setIsGameWon(true);
      }

      if (guesses.length === MAX_CHALLENGES - 1) {
        const ans = await get_answer();
        setAnswer(ans);
        if (isLatestGame) {
          setStats(addStatsForCompletedGame(stats, guesses.length + 1));
        }
        setIsGameLost(true);
        showErrorAlert(CORRECT_WORD_MESSAGE(ans), {
          persist: true,
          delayMs: REVEAL_TIME_MS * solution.length + 1,
        });
      }
    }
  };

  const handleNewGame = () => {
    setGuesses([]);
    setCurrentGuess("");
    setIsGameWon(false);
    setIsGameLost(false);
    hiddenErrorAlert();
    toast.success("New game started!");
  };

  return (
    <Div100vh>
      <div className="flex h-full flex-col">
        <Navbar
          setIsInfoModalOpen={setIsInfoModalOpen}
          setIsStatsModalOpen={(flag, data) => {
            setstatsData(data);
            setIsStatsModalOpen(flag);
          }}
          setIsDatePickerModalOpen={setIsDatePickerModalOpen}
          setIsSettingsModalOpen={setIsSettingsModalOpen}
          setIsHistoryModalOpen={(flag, data) => {
            console.log();
            setHistoryData(data);
            setIsHistoryModalOpen(flag);
          }}
          setIsLeaderboardModalOpen={(flag, data) => {
            console.log();
            setLeaderboardData(data);
            setIsLeaderboardModalOpen(flag);
          }}
          handleNewGame={handleNewGame}
        />

        {!isLatestGame && (
          <div className="flex items-center justify-center">
            <ClockIcon className="h-6 w-6 stroke-gray-600 dark:stroke-gray-300" />
            <p className="text-base text-gray-600 dark:text-gray-300">
              {format(gameDate, "d MMMM yyyy", { locale: DATE_LOCALE })}
            </p>
          </div>
        )}

        <div className="mx-auto flex w-full grow flex-col px-1 pt-2 pb-8 sm:px-6 md:max-w-7xl lg:px-8 short:pb-2 short:pt-2">
          <div className="flex grow flex-col justify-center pb-6 short:pb-2">
            <Grid
              gameStatus={gameStatus}
              guesses={guesses}
              currentGuess={currentGuess}
              isRevealing={isRevealing}
              currentRowClassName={currentRowClass}
            />
          </div>
          <Keyboard
            onChar={onChar}
            onDelete={onDelete}
            onEnter={onEnter}
            gameStatus={gameStatus}
            guesses={guesses}
            isRevealing={isRevealing}
          />
          <InfoModal
            isOpen={isInfoModalOpen}
            handleClose={() => setIsInfoModalOpen(false)}
          />
          {statsData !== null && (
            <StatsModal
              isOpen={isStatsModalOpen}
              handleClose={() => setIsStatsModalOpen(false)}
              solution={solution}
              guesses={guesses}
              gameStats={statsData}
              isLatestGame={isLatestGame}
              isGameLost={isGameLost}
              isGameWon={isGameWon}
              handleShareToClipboard={() =>
                showSuccessAlert(GAME_COPIED_MESSAGE)
              }
              handleShareFailure={() =>
                showErrorAlert(SHARE_FAILURE_TEXT, {
                  durationMs: LONG_ALERT_TIME_MS,
                })
              }
              handleMigrateStatsButton={() => {
                setIsStatsModalOpen(false);
                setIsMigrateStatsModalOpen(true);
              }}
              isDarkMode={isDarkMode}
              isHighContrastMode={isHighContrastMode}
              numberOfGuessesMade={guesses.length}
            />
          )}
          {historyData !== null && (
            <HistoryModal
              isOpen={isHistoryModalOpen}
              handleClose={() => setIsHistoryModalOpen(false)}
              historyData={historyData}
            />
          )}
          {leaderboardData !== null && (
              <LeaderboardModal
                  isOpen={isLeaderboardModalOpen}
                  handleClose={() => setIsLeaderboardModalOpen(false)}
                  leaderboardData={leaderboardData}
              />
          )}
          <DatePickerModal
            isOpen={isDatePickerModalOpen}
            initialDate={solutionGameDate}
            handleSelectDate={(d) => {
              setIsDatePickerModalOpen(false);
              setGameDate(d);
            }}
            handleClose={() => setIsDatePickerModalOpen(false)}
          />
          <MigrateStatsModal
            isOpen={isMigrateStatsModalOpen}
            handleClose={() => setIsMigrateStatsModalOpen(false)}
          />
          <SettingsModal
            isOpen={isSettingsModalOpen}
            handleClose={() => setIsSettingsModalOpen(false)}
            isDarkMode={isDarkMode}
            handleDarkMode={handleDarkMode}
            isHighContrastMode={isHighContrastMode}
            handleHighContrastMode={handleHighContrastMode}
          />
          <AlertContainer />
        </div>
      </div>
    </Div100vh>
  );
}

export default Game;
