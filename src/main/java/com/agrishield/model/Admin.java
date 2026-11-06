package com.agrishield.model;

/** An administrator can see every report, change its status and list all users. */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String name, String phone, String district, String password) {
        super(name, phone, district, password);
    }

    @Override public String getRole() { return "admin"; }
    @Override public boolean canViewField(Field f) { return true; }
    @Override public boolean canViewReport(Report r) { return true; }
    @Override public boolean canDeleteReport(Report r) { return true; }
    @Override public boolean canChangeStatus() { return true; }
    @Override public boolean canManageUsers() { return true; }
}
