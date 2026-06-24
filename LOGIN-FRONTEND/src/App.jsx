import { Routes, Route } from "react-router-dom";
import "./index.css";
import AuthPage from "./pages/Authpage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<AuthPage />} />
      
    </Routes>
  );
}

export default App;