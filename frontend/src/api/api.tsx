import axios from "axios";
import { toast } from "react-toastify";

export const start_new_game = async () => {
  await axios
    .post("http://localhost:9000/api/game"
    )
    .then((response) => {
      localStorage.setItem("gameId", response.data);
    })
    .catch((error) => {
      console.log(error);
    });
}

export const attempt_word = async (word: string) => {
  const gameId = localStorage.getItem("gameId");
  const config = {
    headers: {
      'Content-Type': 'text/plain'
    }
  }
  let result;
  await axios
    .post(`http://localhost:9000/api/game/${gameId}/word`, word, config
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
    .get(`http://localhost:9000/api/game/${gameId}/answer`
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
    .get(`http://localhost:9000/api/game/${gameId}/hint`
    )
    .then((response) => {
      toast.info(<div>{response.data.map((string: string) => {return (<p>{string}</p>)})}</div>);
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
    .get(`http://localhost:9000/api/game/${gameId}/result`
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
    .get(`http://localhost:9000/api/game/${gameId}/history`
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