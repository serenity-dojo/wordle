import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { AlertProvider } from './context/AlertContext'

ReactDOM.createRoot(document.getElementById('root')).render(
  <AlertProvider>
    <App />
  </AlertProvider>,
)
