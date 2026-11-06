package com.agrishield.model;

import com.agrishield.exception.ValidationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract parent of Farmer and Admin.
 * Encapsulation: all fields are private and only change through validating setters.
 * Polymorphism: the abstract methods are answered differently by each subclass.
 */
public abstract class User extends Entity {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String district;
    private final String salt = UUID.randomUUID().toString();
    private String passwordHash;

    protected User(String name, String phone, String district, String password) {
        setName(name);
        setPhone(phone);
        setDistrict(district);
        setPassword(password);
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDistrict() { return district; }

    public final void setName(String name) {
        if (name == null || name.trim().length() < 2) throw new ValidationException("Enter your full name.");
        this.name = name.trim();
    }

    public final void setPhone(String phone) {
        String p = phone == null ? "" : phone.replaceAll("\\s", "");
        if (!p.matches("^\\+260\\d{9}$"))
            throw new ValidationException("Enter your phone number starting with +260, e.g. +260971234567.");
        this.phone = p;
    }

    public final void setDistrict(String district) {
        if (district == null || district.trim().isEmpty()) throw new ValidationException("Enter your district.");
        this.district = district.trim();
    }

    public final void setPassword(String password) {
        if (password == null || password.length() < 6)
            throw new ValidationException("Password must be at least 6 characters.");
        this.passwordHash = hash(password);
    }

    public boolean checkPassword(String password) {
        if (password == null) return false;
        return MessageDigest.isEqual(
                passwordHash.getBytes(StandardCharsets.UTF_8), hash(password).getBytes(StandardCharsets.UTF_8));
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest((salt + password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    // ----- behaviour that differs per role (polymorphism) -----
    public abstract String getRole();
    public abstract boolean canViewField(Field field);
    public abstract boolean canViewReport(Report report);
    public abstract boolean canDeleteReport(Report report);
    public abstract boolean canChangeStatus();
    public abstract boolean canManageUsers();

    @Override
    public Map<String, Object> toView() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", getId());
        m.put("name", name);
        m.put("phone", phone);
        m.put("district", district);
        m.put("role", getRole());
        return m;
    }
}
