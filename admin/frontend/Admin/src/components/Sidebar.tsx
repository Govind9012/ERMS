import { NavLink } from "react-router-dom";
import "../styles/sidebar.css";

/* ICONS */
import {
  MdDashboard,
  MdGroups,          // 👈 NEW ICON
  MdUploadFile,
  MdPersonAdd,
  MdEdit,
  MdSecurity,
  MdFileDownload,
} from "react-icons/md";

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <h2 className="logo">Admin Panel</h2>

      <nav>
        {/* MAIN */}
        <p className="nav-section">MAIN</p>

        <NavLink to="/" end>
          <MdDashboard className="nav-icon" />
          <span>Dashboard</span>
        </NavLink>

        <NavLink to="/teams">
          <MdGroups className="nav-icon" />
          <span>Teams</span>
        </NavLink>

        {/* EMPLOYEE */}
        <p className="nav-section">EMPLOYEE</p>

        <NavLink to="/upload-sheet">
          <MdUploadFile className="nav-icon" />
          <span>Upload Sheet</span>
        </NavLink>

        <NavLink to="/add-employee">
          <MdPersonAdd className="nav-icon" />
          <span>Add Employee</span>
        </NavLink>

        <NavLink to="/update-employee">
          <MdEdit className="nav-icon" />
          <span>Update Employee</span>
        </NavLink>

        {/* ADMIN */}
        <p className="nav-section">ADMIN</p>

        <NavLink to="/permissions">
          <MdSecurity className="nav-icon" />
          <span>Permissions</span>
        </NavLink>

        <NavLink to="/export-teams">
          <MdFileDownload className="nav-icon" />
          <span>Export Teams</span>
        </NavLink>
      </nav>
    </aside>
  );
}
