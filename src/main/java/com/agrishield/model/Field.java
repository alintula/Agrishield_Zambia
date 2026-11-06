package com.agrishield.model;

import com.agrishield.exception.ValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

/** A farm field owned by one farmer. */
public class Field extends Entity {
    private static final long serialVersionUID = 1L;

    private final String ownerId;
    private String name;
    private String crop;
    private double size;

    public Field(String ownerId, String name, String crop, double size) {
        this.ownerId = ownerId;
        setName(name);
        setCrop(crop);
        setSize(size);
    }

    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getCrop() { return crop; }
    public double getSize() { return size; }

    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new ValidationException("Field name and crop are required.");
        this.name = name.trim();
    }

    public final void setCrop(String crop) {
        if (crop == null || crop.trim().isEmpty()) throw new ValidationException("Field name and crop are required.");
        this.crop = crop.trim();
    }

    public final void setSize(double size) {
        if (!(size > 0 && size <= 10000)) throw new ValidationException("Enter a size in hectares greater than 0.");
        this.size = size;
    }

    @Override
    public Map<String, Object> toView() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", getId());
        m.put("owner", ownerId);
        m.put("name", name);
        m.put("crop", crop);
        m.put("size", size);
        return m;
    }
}
