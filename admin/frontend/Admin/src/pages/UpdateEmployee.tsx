import { useEffect, useState } from "react";
import "../styles/updateEmployee.css";
import { toast } from "react-toastify";

interface Employee {
  employeeId: string;
  name: string;
  jobTitle: string;
  location: string;
  pocEmployeeId?: string;
  pocName?: string;
}

interface PocOption {
  employeeId: string;
  name: string;
  role: string;
}

export default function UpdateEmployee() {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [pocs, setPocs] = useState<PocOption[]>([]);
  const [search, setSearch] = useState("");

  const [editingEmp, setEditingEmp] = useState<Employee | null>(null);
  const [deletingEmp, setDeletingEmp] = useState<Employee | null>(null);

  /* =====================
     FETCH EMPLOYEES
     ===================== */
  useEffect(() => {
    fetchEmployees();
    fetchPocs();
  }, []);

  const fetchEmployees = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/employees");
      const data = await res.json();
      setEmployees(data);
    } catch {
      toast.error("Failed to fetch employees");
    }
  };

  const fetchPocs = async () => {
    try {
      const res = await fetch(
        "http://localhost:8080/api/employees/assignable-pocs"
      );
      const data = await res.json();
      setPocs(data);
    } catch {
      toast.error("Failed to load POCs");
    }
  };

  /* =====================
     FILTER
     ===================== */
  const filteredEmployees = employees.filter((emp) =>
    `${emp.name} ${emp.location}`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  /* =====================
     EDIT HANDLERS
     ===================== */
  const handleChange = (field: keyof Employee, value: string) => {
    if (!editingEmp) return;
    setEditingEmp({ ...editingEmp, [field]: value });
  };

  const handleSaveEmployee = async () => {
    if (!editingEmp) return;

    try {
      const res = await fetch(
        `http://localhost:8080/api/employees/${editingEmp.employeeId}?pocEmployeeId=${editingEmp.pocEmployeeId ?? ""}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: editingEmp.name,
            jobTitle: editingEmp.jobTitle,
            location: editingEmp.location,
          }),
        }
      );

      if (!res.ok) throw new Error();

      const updated = await res.json();

      setEmployees((prev) =>
        prev.map((e) =>
          e.employeeId === updated.employeeId ? updated : e
        )
      );

      toast.success("Employee updated successfully 🎉");
      setEditingEmp(null);
    } catch {
      toast.error("Update failed ❌");
    }
  };

  /* =====================
     DELETE HANDLER
     ===================== */
  const handleDeleteEmployee = async () => {
    if (!deletingEmp) return;

    try {
      const res = await fetch(
        `http://localhost:8080/api/employees/${deletingEmp.employeeId}`,
        { method: "DELETE" }
      );

      if (!res.ok) throw new Error();

      setEmployees((prev) =>
        prev.filter(
          (e) => e.employeeId !== deletingEmp.employeeId
        )
      );

      toast.success("Employee deleted 🗑️");
      setDeletingEmp(null);
    } catch {
      toast.error("Delete failed ❌");
    }
  };

  /* =====================
     JSX
     ===================== */
  return (
    <div className="update-employee-page">
      <h2>Update Employees</h2>

      <div className="employee-toolbar">
        <input
          type="text"
          placeholder="Search by name or location..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="employee-table-wrapper">
        <div className="employee-table-scroll">
          <table className="employee-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Job Title</th>
                <th>Location</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {filteredEmployees.length === 0 && (
                <tr>
                  <td colSpan={5} className="no-data">
                    No employees found
                  </td>
                </tr>
              )}

              {filteredEmployees.map((emp) => (
                <tr key={emp.employeeId}>
                  <td>{emp.employeeId}</td>
                  <td>{emp.name}</td>
                  <td>{emp.jobTitle}</td>
                  <td>{emp.location}</td>
                  <td>
                    <button
                      className="edit-btn"
                      onClick={() => setEditingEmp(emp)}
                    >
                      Edit
                    </button>

                    <button
                      className="delete-btn"
                      onClick={() => setDeletingEmp(emp)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* =====================
         EDIT MODAL
         ===================== */}
      {editingEmp && (
        <div className="modal-overlay">
          <div className="edit-modal">
            <h3>Edit Employee</h3>

            <label>
              Name
              <input
                value={editingEmp.name}
                onChange={(e) =>
                  handleChange("name", e.target.value)
                }
              />
            </label>

            <label>
              Job Title
              <input
                value={editingEmp.jobTitle}
                onChange={(e) =>
                  handleChange("jobTitle", e.target.value)
                }
              />
            </label>

            <label>
              POC
              <select
                value={editingEmp.pocEmployeeId ?? ""}
                onChange={(e) =>
                  handleChange("pocEmployeeId", e.target.value)
                }
              >
                <option value="">Select POC</option>
                {pocs.map((poc) => (
                  <option
                    key={poc.employeeId}
                    value={poc.employeeId}
                  >
                    {poc.name} ({poc.role})
                  </option>
                ))}
              </select>
            </label>

            <label>
              Location
              <input
                value={editingEmp.location}
                onChange={(e) =>
                  handleChange("location", e.target.value)
                }
              />
            </label>

            <div className="modal-actions">
              <button
                className="cancel-btn"
                onClick={() => setEditingEmp(null)}
              >
                Cancel
              </button>
              <button
                className="save-btn"
                onClick={handleSaveEmployee}
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}

      {/* =====================
         DELETE MODAL
         ===================== */}
      {deletingEmp && (
        <div className="modal-overlay">
          <div className="delete-modal">
            <h3>Delete Employee</h3>
            <p>
              Are you sure you want to delete{" "}
              <strong>{deletingEmp.name}</strong>?
            </p>

            <div className="modal-actions">
              <button
                className="cancel-btn"
                onClick={() => setDeletingEmp(null)}
              >
                Cancel
              </button>
              <button
                className="confirm-delete-btn"
                onClick={handleDeleteEmployee}
              >
                Yes, Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
