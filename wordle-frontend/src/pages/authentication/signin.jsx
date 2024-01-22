import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import clsx from "clsx";
import { toast } from "react-toastify";

import { useAuth } from "../../provider/authProvider";
import { signin } from "../../api/api";

const Signin = () => {
  const { signIn } = useAuth();
  const navigate = useNavigate();

  const [loadingView, setLoadingView] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const prefersDarkMode = window.matchMedia(
    "(prefers-color-scheme: dark)"
  ).matches;
  const [isDarkMode, setIsDarkMode] = useState(
    localStorage.getItem("theme")
      ? localStorage.getItem("theme") === "dark"
      : prefersDarkMode
      ? true
      : false
  );

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [isDarkMode]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoadingView(true);
    console.log(username, password)

    try {
      const response = await signin(username, password);
      setLoadingView(false);
      await signIn(response.accessToken);
      localStorage.setItem("isLogined", "true");
      navigate("/game");
    } catch (err) {
      setLoadingView(false);
      console.log(err);
      toast.error("Signin failed!");
    }
  };

  return (
    <div className="w-full min-h-screen flex flex-row">
      <div className="flex flex-col justify-center gap-16 w-full pt-12 sm:pt-0">
        <div className="relative flex flex-col bg-main justify-center w-full">
          <div className="flex flex-col justify-center gap-16 sm:w-1/3 mx-auto">
            <form onSubmit={handleLogin}>
              <div className="flex flex-col gap-8 text-sm text-[#111827] dark:text-white w-full px-4">
                <p className="text-xl sm:text-2xl font-bold text-center text-primary-500">
                  Login first to your account
                </p>
                <div className="flex flex-col gap-6">
                  <div className="flex flex-col gap-[10px]">
                    <p>
                      Username <span className="text-[#E03137]">*</span>
                    </p>
                    <input
                      type="text"
                      className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      required
                    />
                  </div>
                  <div className="flex flex-col gap-[10px]">
                    <p>
                      Password <span className="text-[#E03137]">*</span>
                    </p>
                    <input
                      type="password"
                      className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                    />
                  </div>
                </div>
                <div className="flex flex-col gap-4">
                  <button
                    type="submit"
                    className={clsx(
                      "flex items-center justify-center text-white focus:ring-0 font-bold text-center rounded-[10px] text-base px-6 py-5",
                      username === "" || password === ""
                        ? "cursor-not-allowed bg-primary-400"
                        : "bg-primary-500"
                    )}
                    disabled={
                      loadingView === true || username === "" || password === ""
                        ? true
                        : false
                    }
                  >
                    {loadingView === true && (
                      <svg
                        aria-hidden="true"
                        role="status"
                        className="inline w-4 h-4 me-3 text-white animate-spin"
                        viewBox="0 0 100 101"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                          fill="#E5E7EB"
                        />
                        <path
                          d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                          fill="currentColor"
                        />
                      </svg>
                    )}
                    Login
                  </button>
                </div>
                <p className="text-sm text-center text-[#A0AEC0]">
                  You are new in here?{" "}
                  <a
                    className="text-sm text-[#3562D4]"
                    href="\signup"
                    rel="noreferrer"
                  >
                    Create Account
                  </a>
                </p>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signin;
