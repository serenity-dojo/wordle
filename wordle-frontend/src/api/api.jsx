import axios from "axios";
import { toast } from "react-toastify";

export const signin = async (name, password) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await axios.post(`${import.meta.env.VITE_SERVER_URL}/wordle/api/auth/login`, {
        username: name,
        password: password,
      }
      )
      console.log(response)
      resolve(response.data);
    } catch (err) {
      reject(err);
    }
  })
}

export const signup = async (name, workEmail, password) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await axios.post(`${import.meta.env.VITE_SERVER_URL}/wordle/api/auth/register`, {
        username: name,
        email: workEmail,
        password: password,
        role: "ROLE_PLAYER"
      }
      )
      console.log(response)
      resolve(response.data);
    } catch (err) {
      reject(err);
    }
  })
}

export const start_new_game = async () => {
  let result;
  await axios
    .post(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game`
    )
    .then((response) => {
      localStorage.setItem("gameId", response.data);
      result = true;
    })
    .catch((error) => {
      console.log(error);
      result = false;
    });
  return result;
}

export const attempt_word = async (word) => {
  const gameId = localStorage.getItem("gameId");
  const config = {
    headers: {
      'Content-Type': 'text/plain'
    }
  }
  let result;
  await axios
    .post(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/${gameId}/word`, word, config
    )
    .then((response) => {
      console.log(response.data)
      result = response;
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const get_answer = async () => {
  const gameId = localStorage.getItem("gameId");
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/${gameId}/answer`
    )
    .then((response) => {
      console.log(response.data)
      result = response.data;
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const find_hint = async () => {
  const gameId = localStorage.getItem("gameId");
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/${gameId}/hint`
    )
    .then((response) => {
      toast.info(<div>{response.data.map((string, index) => { return (<p key={index}>{string}</p>) })}</div>);
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const find_result = async () => {
  const gameId = localStorage.getItem("gameId");
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/${gameId}/result`
    )
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const find_current_history = async () => {
  const gameId = localStorage.getItem("gameId");
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/${gameId}/history`
    )
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const get_game_history = async () => {
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/history`
    )
    .then((response) => {
      console.log(response)
      result = response.data;
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}

export const get_game_statistics = async () => {
  let result;
  await axios
    .get(`${import.meta.env.VITE_SERVER_URL}/wordle/api/game/statistics`
    )
    .then((response) => {
      console.log(response)
      result = response.data;
    })
    .catch((error) => {
      console.log(error);
      result = error;
    });
  return result;
}