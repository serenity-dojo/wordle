import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'

import AuthProvider from "./provider/authProvider";
import Routes from "./routes";
import './App.css';

function App() {
  return (
    <>
      <AuthProvider>
        <Routes />
      </AuthProvider>
      <ToastContainer
        autoClose={5000}
        hideProgressBar
        pauseOnHover={false}
        pauseOnFocusLoss={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
      />
    </>
  )
}

export default App
