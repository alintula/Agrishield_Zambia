package com.agrishield.service;

import com.agrishield.exception.AccessDeniedException;
import com.agrishield.model.Field;
import com.agrishield.model.User;
import com.agrishield.store.DataStore;

import java.util.List;

public class FieldService {
    private final DataStore store;

    public FieldService(DataStore store) {
        this.store = store;
    }

    public List<Field> list(User user) {
        return store.fields.findBy(user::canViewField);
    }

    public Field create(User user, String name, String crop, double size) {
        Field field = new Field(user.getId(), name, crop, size);
        store.fields.save(field);
        store.save();
        return field;
    }

    /** Deleting a field also deletes its reports. */
    public void delete(User user, String id) {
        Field field = store.fields.getOrThrow(id, "Field");
        if (!field.getOwnerId().equals(user.getId()))
            throw new AccessDeniedException("You can only delete your own fields.");
        store.reports.deleteIf(r -> r.getFieldId().equals(id));
        store.fields.delete(id);
        store.save();
    }
}
