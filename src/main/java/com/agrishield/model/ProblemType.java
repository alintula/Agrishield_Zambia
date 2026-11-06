package com.agrishield.model;

import com.agrishield.exception.ValidationException;

public enum ProblemType {
    DROUGHT("Drought"), PESTS("Pests"), FLOOD("Flood"), DISEASE("Disease");

    private final String label;

    ProblemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ProblemType fromLabel(String text) {
        for (ProblemType t : values()) {
            if (t.label.equalsIgnoreCase(text)) return t;
        }
        throw new ValidationException("Choose a valid problem type.");
    }
}
