import * as XLSX from "xlsx";

/* =========================
   TYPES
   ========================= */

export interface EmployeeRow {
  "Employee ID": string | number;
  Name: string;
  "Job Title": string;
  POC?: string;
  Location?: string;
}

export interface HierarchyEmployee {
  name: string;
  role: string;
  location?: string;
}

export interface HierarchyPOC extends HierarchyEmployee {
  employees: HierarchyEmployee[];
}

export interface HierarchyManager extends HierarchyEmployee {
  pocs: HierarchyPOC[];
}

/* =========================
   READ EXCEL FILE
   ========================= */

export function readExcelFile(
  file: File,
  managerName: string
): Promise<HierarchyManager | null> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      try {
        const data = new Uint8Array(e.target?.result as ArrayBuffer);
        const workbook = XLSX.read(data, { type: "array" });
        const sheet = workbook.Sheets[workbook.SheetNames[0]];

        const rows = XLSX.utils.sheet_to_json<EmployeeRow>(sheet, {
          defval: "",
        });

        const hierarchy = buildHierarchy(rows, managerName);

        if (!hierarchy) {
          console.warn(`Manager "${managerName}" not found in Excel`);
        }

        resolve(hierarchy);
      } catch (err) {
        reject(err);
      }
    };

    reader.onerror = reject;
    reader.readAsArrayBuffer(file);
  });
}

/* =========================
   BUILD HIERARCHY
   ========================= */

function buildHierarchy(
  rows: EmployeeRow[],
  managerName: string
): HierarchyManager | null {
  const normalize = (v: string) => v.trim().toLowerCase();

  const managerRow = rows.find(
    (r) => normalize(r.Name) === normalize(managerName)
  );

  if (!managerRow) return null;

  const pocs: HierarchyPOC[] = rows
    .filter((r) => normalize(r.POC || "") === normalize(managerName))
    .map((poc) => {
      const employees: HierarchyEmployee[] = rows
        .filter(
          (emp) => normalize(emp.POC || "") === normalize(poc.Name)
        )
        .map((emp) => ({
          name: emp.Name,
          role: emp["Job Title"],
          location: emp.Location,
        }));

      return {
        name: poc.Name,
        role: "POC",
        location: poc.Location,
        employees,
      };
    });

  return {
    name: managerRow.Name,
    role: "Manager",
    location: managerRow.Location,
    pocs,
  };
}
