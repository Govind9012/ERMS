import { useEffect, useState } from "react";
import "../styles/dashboard.css";

type Employee = {
  employeeId?: string;
  name?: string;
  jobTitle?: string;
  pocEmployeeId?: string;
  location?: string;
};

export default function Dashboard() {
  const [totalEmployees, setTotalEmployees] = useState<number>(0);

  // placeholders (until backend provides these stats)
  const [availableToday] = useState<string>("--");
  const [absentToday] = useState<string>("--");
  const [onLeave] = useState<string>("--");
  const [teams] = useState<string>("--");

  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    const controller = new AbortController();

    const fetchEmployees = async () => {
      try {
        setLoading(true);
        setError("");

        const res = await fetch("http://localhost:8080/api/employees", {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          signal: controller.signal,
        });

        if (!res.ok) {
          throw new Error(`Failed to fetch employees: ${res.status}`);
        }

        const employees: Employee[] = await res.json();
        setTotalEmployees(Array.isArray(employees) ? employees.length : 0);
      } catch (err: unknown) {
        // ✅ Ignore abort errors (common in React 18 dev strict mode)
        if (err instanceof DOMException && err.name === "AbortError") return;

        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("Something went wrong while fetching employees");
        }
      } finally {
        // ✅ Avoid setting state after abort/unmount
        if (!controller.signal.aborted) setLoading(false);
      }
    };

    fetchEmployees();

    return () => {
      controller.abort();
    };
  }, []);

  if (loading) {
    return (
      <div className="dashboard">
        <div className="stats-grid">
          <div className="stat-card primary">
            <div className="stat-title">Total Employees</div>
            <div className="stat-value">...</div>
          </div>

          <div className="stat-card success">
            <div className="stat-title">Available Today</div>
            <div className="stat-value">...</div>
          </div>

          <div className="stat-card danger">
            <div className="stat-title">Absent Today</div>
            <div className="stat-value">...</div>
          </div>

          <div className="stat-card warning">
            <div className="stat-title">On Leave</div>
            <div className="stat-value">...</div>
          </div>

          <div className="stat-card primary">
            <div className="stat-title">Teams</div>
            <div className="stat-value">...</div>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard">
        <p style={{ color: "red" }}>Error: {error}</p>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <div className="stats-grid">
        <div className="stat-card primary">
          <div className="stat-title">Total Employees</div>
          <div className="stat-value">{totalEmployees}</div>
        </div>

        <div className="stat-card success">
          <div className="stat-title">Available Today</div>
          <div className="stat-value">{availableToday}</div>
        </div>

        <div className="stat-card danger">
          <div className="stat-title">Absent Today</div>
          <div className="stat-value">{absentToday}</div>
        </div>

        <div className="stat-card warning">
          <div className="stat-title">On Leave</div>
          <div className="stat-value">{onLeave}</div>
        </div>

        <div className="stat-card primary">
          <div className="stat-title">Teams</div>
          <div className="stat-value">{teams}</div>
        </div>
      </div>
    </div>
  );
}