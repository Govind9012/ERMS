import { useEffect, useState } from "react";
import { toast } from "react-toastify";

type Employee = {
  employeeId: string;
  name: string;
  role: string;
};

export default function ExportTeams() {
  const [showConfirm, setShowConfirm] = useState(true);

  const handleConfirmExport = async () => {
    setShowConfirm(false);

    try {
      // 1️⃣ Fetch employees to find manager
      const empRes = await fetch("http://localhost:8080/api/employees");
      if (!empRes.ok) throw new Error();

      const employees: Employee[] = await empRes.json();

      const manager = employees.find(
        (e) => e.role === "MANAGER"
      );

      if (!manager) {
        toast.error("Manager not found ❌");
        return;
      }

      const managerName = manager.name.replace(/\s+/g, "_");

      // 2️⃣ Fetch CSV
      const csvRes = await fetch("http://localhost:8080/api/employees/export");
      if (!csvRes.ok) throw new Error();

      const blob = await csvRes.blob();
      const url = window.URL.createObjectURL(blob);

      // 3️⃣ Download
      const a = document.createElement("a");
      a.href = url;
      a.download = `${managerName}_team.csv`;
      a.click();

      window.URL.revokeObjectURL(url);
    } catch {
      toast.error("Failed to export teams ❌");
    }
  };

  return (
    <>
      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Export Teams</h3>
            <p>
              This will download the team data as a CSV file.
              Do you want to continue?
            </p>

            <div className="modal-actions">
              <button
                className="btn-cancel"
                onClick={() => setShowConfirm(false)}
              >
                Cancel
              </button>

              <button
                className="btn-confirm"
                onClick={handleConfirmExport}
              >
                Export
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
