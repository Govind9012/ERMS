package com.example.Admin.service;

public class RelationMapping {

    private final String reportsToName;
    private final int rowNumber;

    public RelationMapping(String reportsToName, int rowNumber) {
        this.reportsToName = reportsToName;
        this.rowNumber = rowNumber;
    }

    public String getReportsToName() {
        return reportsToName;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
