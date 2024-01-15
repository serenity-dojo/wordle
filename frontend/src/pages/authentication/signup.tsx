import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import clsx from "clsx";
import { toast } from "react-toastify";
import validator from "validator";

const Signup = () => {
  const navigate = useNavigate();

  const [loadingView, setLoadingView] = useState(false);
  const [name, setName] = useState("");
  const [workEmail, setWorkEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleCreateAccount = async () => {
    if (!validator.isEmail(workEmail)) {
      toast.error("Please enter a valid email!");
      return;
    }

    setLoadingView(true);

    // axios
    //   .post(`${import.meta.env.VITE_SERVER_URL}/auth/signup`, {
    //     name: name,
    //     email: workEmail,
    //     password: password,
    //   })
    //   .then((response) => {
    //     setLoadingView(false);
    //     if (response.status === 200) navigate("/verifyemail");
    //     else toast.error(response.data.message);
    //   })
    //   .catch((error) => {
    //     setLoadingView(false);
    //     console.log(error);
    //     toast.error(error.message);
    //   });
  };

  return (
    <div className="w-full min-h-screen flex flex-row bg-white">
      <div className="flex flex-col justify-center gap-16 w-full">
        <div className="relative flex flex-col bg-main justify-center w-full">
          <div className="flex flex-col justify-center gap-8 sm:w-1/2 mx-auto px-4">
            <p className="text-xl sm:text-[32px] text-center font-bold text-primary-600">
              Sign up
            </p>
            <div className="flex flex-col gap-6 text-sm text-[#111827]">
              <div className="flex flex-col gap-[10px]">
                <p>
                  Name <span className="text-[#E03137]">*</span>
                </p>
                <input
                  type="text"
                  className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>
              <div className="flex flex-col gap-[10px]">
                <p>
                 E-Mail <span className="text-[#E03137]">*</span>
                </p>
                <input
                  type="email"
                  className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input"
                  value={workEmail}
                  onChange={(e) => setWorkEmail(e.target.value)}
                  required
                />
              </div>
              <div className="flex flex-col gap-[10px]">
                <p>
                  Password <span className="text-[#E03137]">*</span>
                </p>
                <input
                  type="password"
                  className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
            </div>
            <div className="flex flex-col gap-4">
              <button
                type="button"
                className={clsx(
                  "flex items-center justify-center text-white focus:ring-0 font-bold text-center rounded-[10px] text-base px-6 py-5",
                  name === "" || workEmail === "" || password === ""
                    ? "cursor-not-allowed bg-primary-400"
                    : "bg-primary-500"
                )}
                onClick={handleCreateAccount}
                disabled={
                  name === "" || workEmail === "" || password === ""
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
                Create Account
              </button>
              <p className="text-sm text-[#A0AEC0]">
                Already have an account?{" "}
                <a
                  className="text-sm text-[#3562D4]"
                  href="\"
                  rel="noreferrer"
                >
                  Login Here
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signup;
