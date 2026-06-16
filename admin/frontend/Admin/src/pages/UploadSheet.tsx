import { useState } from "react";
import "../styles/uploadSheet.css";

export default function UploadSheet() {
  const [fileName, setFileName] = useState("");
  const [message, setMessage] = useState("");

  const handleFileChange = async (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setFileName(file.name);
    setMessage("Uploading...");

    const formData = new FormData();
    formData.append("file", file); // 👈 MUST BE "file"

    try {
      const response = await fetch(
        "http://localhost:8080/excel/upload",
        {
          method: "POST",
          body: formData,
        }
      );

      const text = await response.text();
      setMessage(text);
    } catch (error) {
      setMessage("Upload failed");
    }
  };

  return (
    <div className="upload-sheet-page">
      <h2 className="upload-sheet-title">Upload Employee Sheet</h2>
      <p className="upload-sheet-subtitle">
        Upload Excel file (.xlsx / .xls) to add or update employees
      </p>

      <div className="upload-card">
        <label className="upload-drop">
          <input
            type="file"
            accept=".xlsx,.xls"
            onChange={handleFileChange}
          />
          <div className="upload-drop-text">
            Drag & drop Excel file here or <strong>browse</strong>
          </div>
        </label>

        {fileName && (
          <div className="upload-success">
            📄 {fileName}
          </div>
        )}

        {message && (
          <p style={{ marginTop: 12 }}>{message}</p>
        )}
      </div>
    </div>
  );
}
