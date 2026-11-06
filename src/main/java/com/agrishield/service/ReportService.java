package com.agrishield.service;

import com.agrishield.exception.AccessDeniedException;
import com.agrishield.model.Field;
import com.agrishield.model.ProblemType;
import com.agrishield.model.Report;
import com.agrishield.model.ReportStatus;
import com.agrishield.model.User;
import com.agrishield.store.DataStore;

import java.time.LocalDate;
import java.util.List;

public class ReportService {
    private final DataStore store;

    public ReportService(DataStore store) {
        this.store = store;
    }

    public List<Report> list(User user) {
        return store.reports.findBy(user::canViewReport);
    }

    public Report create(User user, String fieldId, ProblemType type, int severity,
                         LocalDate date, String description, String action) {
        Field field = store.fields.getOrThrow(fieldId, "Field");
        if (!field.getOwnerId().equals(user.getId()))
            throw new AccessDeniedException("You can only report damage on your own fields.");
        Report report = new Report(user.getId(), fieldId, type, severity, date, description, action);
        store.reports.save(report);
        store.save();
        return report;
    }

    public void delete(User user, String id) {
        Report report = store.reports.getOrThrow(id, "Report");
        if (!user.canDeleteReport(report)) throw new AccessDeniedException("You cannot delete this report.");
        store.reports.delete(id);
        store.save();
    }

    public Report changeStatus(User user, String id, ReportStatus status) {
        if (!user.canChangeStatus()) throw new AccessDeniedException("Only administrators can change a status.");
        Report report = store.reports.getOrThrow(id, "Report");
        report.setStatus(status);
        store.save();
        return report;
    }
}
