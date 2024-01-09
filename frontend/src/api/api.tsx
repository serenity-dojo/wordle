import axios from "axios";

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