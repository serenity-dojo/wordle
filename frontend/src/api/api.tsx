import axios from "axios";

const gameId = localStorage.getItem("gameId");

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
  const config = {
    headers: {
      'Content-Length': 0,
      'Content-Type': 'text/plain'
    }
  }
  await axios
    .post(`http://localhost:9000/api/game/${gameId}/word`, word, config
    )
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log(error);
    });
}