import { useEffect, useState } from "react";
import "../styles/addEmployee.css";
import { toast } from "react-toastify";

type Employee = {
  employeeId: string;
  name: string;
  jobTitle: string;
  location: string;
};

type PocOption = {
  employeeId: string;
  name: string;
  role: string;
};

export default function AddEmployee() {
  const [form, setForm] = useState({
    employeeId: "",
    name: "",
    jobTitle: "",
    location: "",
    pocEmployeeId: "",
  });

  const [showConfirm, setShowConfirm] = useState(false);
  const [jobTitles, setJobTitles] = useState<string[]>([]);
  const [locations, setLocations] = useState<string[]>([]);
  const [pocs, setPocs] = useState<PocOption[]>([]);

  // =========================
  // FETCH DATA ON LOAD
  // =========================
  useEffect(() => {
    fetchEmployeesForMeta();
    fetchAssignablePocs();
  }, []);

  // Job titles & locations from employees
  const fetchEmployeesForMeta = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/employees");
      const data: Employee[] = await res.json();

      setJobTitles(
        Array.from(new Set(data.map(e => e.jobTitle).filter(Boolean)))
      );

      setLocations(
        Array.from(new Set(data.map(e => e.location).filter(Boolean)))
      );
    } catch {
      toast.error("Failed to load job titles / locations ❌");
    }
  };

  // POCs & Managers ONLY
  const fetchAssignablePocs = async () => {
    try {
      const res = await fetch(
        "http://localhost:8080/api/employees/assignable-pocs"
      );
      const data: PocOption[] = await res.json();
      setPocs(data);
    } catch {
      toast.error("Failed to load POCs ❌");
    }
  };

  // =========================
  // HANDLERS
  // =========================
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (!form.employeeId || !form.name || !form.jobTitle || !form.location) {
      toast.error("Please fill all fields");
      return;
    }

    try {
      const res = await fetch(
        `http://localhost:8080/api/employees?pocEmployeeId=${form.pocEmployeeId}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            employeeId: form.employeeId,
            name: form.name,
            jobTitle: form.jobTitle,
            location: form.location,
          }),
        }
      );

      if (!res.ok) throw new Error();

      toast.success("Employee added successfully 🎉");

      setForm({
        employeeId: "",
        name: "",
        jobTitle: "",
        location: "",
        pocEmployeeId: "",
      });
    } catch {
      toast.error("Failed to add employee ❌");
    }
  };

  // =========================
  // UI
  // =========================
  return (
    <div className="add-employee-page">
      <h2>Add Employee</h2>

      <div className="add-employee-card">
        <label>
          Employee ID
          <input
            name="employeeId"
            value={form.employeeId}
            onChange={handleChange}
            placeholder="ex: EMPN1973"
            
          />
        </label>

        <label>
          Name
          <input name="name" value={form.name} onChange={handleChange} />
        </label>

        <label>
          Job Title
          <select
            name="jobTitle"
            value={form.jobTitle}
            onChange={handleChange}
          >
            <option value="">Select Job Title</option>
            {jobTitles.map(title => (
              <option key={title} value={title}>
                {title}
              </option>
            ))}
          </select>
        </label>

        <label>
          POC
          <select
            name="pocEmployeeId"
            value={form.pocEmployeeId}
            onChange={handleChange}
          >
            <option value="">Select POC</option>
            {pocs.map(poc => (
              <option key={poc.employeeId} value={poc.employeeId}>
                {poc.name} ({poc.role})
              </option>
            ))}
          </select>
        </label>

        <label>
          Location
          <select
            name="location"
            value={form.location}
            onChange={handleChange}
          >
            <option value="">Select Location</option>
            {locations.map(loc => (
              <option key={loc} value={loc}>
                {loc}
              </option>
            ))}
          </select>
        </label>

        <button className="add-btn" onClick={() => setShowConfirm(true)}>
          ➕ Add Employee
        </button>
      </div>

      {/* CONFIRM MODAL */}
      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Confirm Add Employee</h3>
            <p>Are you sure you want to add this employee?</p>

            <div className="modal-actions">
              <button
                className="btn-cancel"
                onClick={() => setShowConfirm(false)}
              >
                Cancel
              </button>

              <button
                className="btn-confirm"
                onClick={() => {
                  setShowConfirm(false);
                  handleSubmit();
                }}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
