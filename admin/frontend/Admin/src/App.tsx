import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import TeamsPage from "./pages/Teams";
import Dashboard from "./pages/Dashboard";
import UploadSheet from "./pages/UploadSheet";
import UpdateEmployee from "./pages/UpdateEmployee";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AddEmployee from "./pages/AddEmployee";
import ExportTeams from "./pages/ExportTeams";

export default function App() {
  return (
    <BrowserRouter>
      <div className="app-layout">
        <Sidebar />

        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/teams" element={<TeamsPage />} />
          <Route path="/upload-sheet" element={<UploadSheet />} />
          <Route path="/update-employee" element={<UpdateEmployee />} />
          <Route path="/add-employee" element={<AddEmployee />} />
          <Route path="/export-teams" element={<ExportTeams />} />

        </Routes>

        {/* 🔥 TOAST CONTAINER – ADD THIS */}
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop
          closeOnClick
          pauseOnHover
          theme="light"
        />
      </div>
    </BrowserRouter>
  );
}
