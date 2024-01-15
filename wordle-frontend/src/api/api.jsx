import axios from "axios";
import { toast } from "react-toastify";

export const signin = async (name, password) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/wordle/api/auth/login`, {
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
      const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/wordle/api/auth/register`, {
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
  await axios
    .post(`${process.env.REACT_APP_SERVER_URL}/api/game`
    )
    .then((response) => {
      localStorage.setItem("gameId", response.data);
    })
    .catch((error) => {
      console.log(error);
    });
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
    .post(`${process.env.REACT_APP_SERVER_URL}/api/game/${gameId}/word`, word, config
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
    .get(`${process.env.REACT_APP_SERVER_URL}/api/game/${gameId}/answer`
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
    .get(`${process.env.REACT_APP_SERVER_URL}/api/game/${gameId}/hint`
    )
    .then((response) => {
      toast.info(<div>{response.data.map((string) => { return (<p>{string}</p>) })}</div>);
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
    .get(`${process.env.REACT_APP_SERVER_URL}/api/game/${gameId}/result`
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
    .get(`${process.env.REACT_APP_SERVER_URL}/api/game/${gameId}/history`
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