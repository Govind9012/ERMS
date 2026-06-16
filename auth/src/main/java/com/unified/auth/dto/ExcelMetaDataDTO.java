package com.unified.auth.dto;

public class ExcelMetaDataDTO {

    private String fileName;
    private String uploadedBy;
    private int totalRecords;
    private int skippedRows;
    private int duplicateRows;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getSkippedRows() {
        return skippedRows;
    }

    public void setSkippedRows(int skippedRows) {
        this.skippedRows = skippedRows;
    }

    public int getDuplicateRows() {
        return duplicateRows;
    }

    public void setDuplicateRows(int duplicateRows) {
        this.duplicateRows = duplicateRows;
    }
}
