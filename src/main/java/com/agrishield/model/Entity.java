package com.agrishield.model;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/** Parent of every stored object (User, Field, Report). Gives each one an id and a JSON view. */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id = UUID.randomUUID().toString().substring(0, 12);

    public String getId() {
        return id;
    }

    /** Each subclass decides how it is shown to the frontend (polymorphism). */
    public abstract Map<String, Object> toView();
}
