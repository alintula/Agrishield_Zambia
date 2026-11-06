package com.agrishield.model;

/** A farmer only works with their own fields and reports. */
public class Farmer extends User {
    private static final long serialVersionUID = 1L;

    public Farmer(String name, String phone, String district, String password) {
        super(name, phone, district, password);
    }

    @Override public String getRole() { return "farmer"; }
    @Override public boolean canViewField(Field f) { return f.getOwnerId().equals(getId()); }
    @Override public boolean canViewReport(Report r) { return r.getOwnerId().equals(getId()); }
    @Override public boolean canDeleteReport(Report r) { return r.getOwnerId().equals(getId()); }
    @Override public boolean canChangeStatus() { return false; }
    @Override public boolean canManageUsers() { return false; }
}
