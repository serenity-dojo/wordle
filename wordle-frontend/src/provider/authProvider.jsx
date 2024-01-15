// import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { createContext, useCallback, useEffect, useReducer, useContext } from "react";

import { accountApi } from "../api/account";

const STORAGE_KEY = "token";

let ActionType;
(function (ActionType) {
  ActionType["INITIALIZE"] = "INITIALIZE";
  ActionType["SIGN_IN"] = "SIGN_IN";
  ActionType["SIGN_UP"] = "SIGN_UP";
  ActionType["SIGN_OUT"] = "SIGN_OUT";
})(ActionType || (ActionType = {}));

const initialState = {
  isAuthenticated: false,
  isInitialized: false,
  account: null,
  accessToken: null,
};

const handlers = {
  INITIALIZE: (state, action) => {
    const { isAuthenticated, account, accessToken } = action.payload;

    return {
      ...state,
      isAuthenticated,
      isInitialized: true,
      account,
      accessToken
    };
  },
  SIGN_IN: (state, action) => {
    const { account, accessToken } = action.payload;

    return {
      ...state,
      isAuthenticated: true,
      account,
      accessToken
    };
  },
  SIGN_UP: (state, action) => {
    const { account } = action.payload;

    return {
      ...state,
      isAuthenticated: true,
      account,
    };
  },
  SIGN_OUT: (state) => ({
    ...state,
    isAuthenticated: false,
    account: null,
    accessToken : null
  })
};

const reducer = (state, action) =>
  handlers[action.type] ? handlers[action.type](state, action) : state;

export const AuthContext = createContext({
  ...initialState,
  signIn: (accessToken) => Promise.resolve(),
  signOut: () => Promise.resolve(),
});

export const AuthProvider = (props) => {
  const { children } = props;
  const [state, dispatch] = useReducer(reducer, initialState);

  const initialize = useCallback(async () => {
    try {
      const accessToken = window.sessionStorage.getItem(STORAGE_KEY);

      if (accessToken) {
        const account = await accountApi.me({ accessToken });

        dispatch({
          type: ActionType.INITIALIZE,
          payload: {
            isAuthenticated: true,
            account,
            accessToken
          },
        });
        
      } else {
        dispatch({
          type: ActionType.INITIALIZE,
          payload: {
            isAuthenticated: false,
            account: null,
            accessToken : null
          },
        });
      }
    } catch (err) {
      console.error(err);
      dispatch({
        type: ActionType.INITIALIZE,
        payload: {
          isAuthenticated: false,
          account: null,
          accessToken: null
        },
      });
    }
  }, [dispatch]);

  useEffect(
    () => {
      initialize();
    },
    []
  );

  const signIn = useCallback(
    async (accessToken) => {
      const account = await accountApi.me({ accessToken });

      sessionStorage.setItem(STORAGE_KEY, accessToken);

      dispatch({
        type: ActionType.SIGN_IN,
        payload: {
          account,
          accessToken
        },
      });
    },
    [dispatch]
  );

  const signOut = useCallback(async () => {
    sessionStorage.removeItem(STORAGE_KEY);
    dispatch({ type: ActionType.SIGN_OUT });
  }, [dispatch]);

  return (
    <AuthContext.Provider
      value={{
        ...state,
        signIn,
        signOut,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};





// const AuthContext = createContext();

// const AuthProvider = ({ children }) => {
//   // State to hold the authentication token
//   const [token, setToken_] = useState(localStorage.getItem("token"));

//   // Function to set the authentication token
//   const setToken = (newToken) => {
//     setToken_(newToken);
//   };

//   useEffect(() => {
//     if (token) {
//       // axios.defaults.headers.common["Authorization"] = "Bearer " + token;
//       localStorage.setItem('token', token);
//     } else {
//       // delete axios.defaults.headers.common["Authorization"];
//       localStorage.removeItem('token')
//     }
//   }, [token]);

//   // Memoized value of the authentication context
//   const contextValue = useMemo(
//     () => ({
//       token,
//       setToken,
//     }),
//     [token]
//   );

//   // Provide the authentication context to the children components
//   return (
//     <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
//   );
// };

export const useAuth = () => {
  return useContext(AuthContext);
};

export default AuthProvider;