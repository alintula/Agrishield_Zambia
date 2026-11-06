package com.agrishield.model;

import com.agrishield.exception.ValidationException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/** A crop damage report for one field. */
public class Report extends Entity {
    private static final long serialVersionUID = 1L;

    private final String ownerId;
    private final String fieldId;
    private ProblemType type;
    private int severity;
    private LocalDate date;
    private String description;
    private String action;
    private ReportStatus status = ReportStatus.PENDING;

    public Report(String ownerId, String fieldId, ProblemType type, int severity,
                  LocalDate date, String description, String action) {
        this.ownerId = ownerId;
        this.fieldId = fieldId;
        this.type = type;
        setSeverity(severity);
        setDate(date);
        setDescription(description);
        setAction(action);
    }

    public String getOwnerId() { return ownerId; }
    public String getFieldId() { return fieldId; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public final void setSeverity(int severity) {
        if (severity < 1 || severity > 5) throw new ValidationException("Severity must be from 1 to 5.");
        this.severity = severity;
    }

    public final void setDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now()))
            throw new ValidationException("Choose a date that is not in the future.");
        this.date = date;
    }

    public final void setDescription(String text) {
        if (text == null || text.trim().length() < 10)
            throw new ValidationException("Describe the problem and its cause in at least 10 characters.");
        this.description = text.trim();
    }

    public final void setAction(String text) {
        if (text == null || text.trim().length() < 5)
            throw new ValidationException("Explain the action you took in at least 5 characters.");
        this.action = text.trim();
    }

    @Override
    public Map<String, Object> toView() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", getId());
        m.put("field", fieldId);
        m.put("owner", ownerId);
        m.put("type", type.getLabel());
        m.put("sev", severity);
        m.put("date", date.toString());
        m.put("desc", description);
        m.put("action", action);
        m.put("status", status.getLabel());
        return m;
    }
}
