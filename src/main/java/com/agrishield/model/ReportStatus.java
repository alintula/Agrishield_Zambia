package com.agrishield.model;

import com.agrishield.exception.ValidationException;

public enum ReportStatus {
    PENDING("Pending"), UNDER_REVIEW("Under review"), RESOLVED("Resolved");

    private final String label;

    ReportStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ReportStatus fromLabel(String text) {
        for (ReportStatus s : values()) {
            if (s.label.equalsIgnoreCase(text)) return s;
        }
        throw new ValidationException("Choose a valid status.");
    }
}
