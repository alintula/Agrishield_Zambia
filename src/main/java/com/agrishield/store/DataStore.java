package com.agrishield.store;

import com.agrishield.exception.StorageException;
import com.agrishield.model.Field;
import com.agrishield.model.Report;
import com.agrishield.model.User;
import com.agrishield.repository.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Holds the three repositories and saves them to one file so data survives a restart. */
public class DataStore {
    public final Repository<User> users = new Repository<>();
    public final Repository<Field> fields = new Repository<>();
    public final Repository<Report> reports = new Repository<>();

    private final Path file;

    public DataStore(Path file) {
        this.file = file;
        load();
    }

    public synchronized void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(new ArrayList<>(users.findAll()));
            out.writeObject(new ArrayList<>(fields.findAll()));
            out.writeObject(new ArrayList<>(reports.findAll()));
        } catch (IOException e) {
            throw new StorageException("Could not save data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        if (!Files.exists(file)) return;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            ((List<User>) in.readObject()).forEach(users::save);
            ((List<Field>) in.readObject()).forEach(fields::save);
            ((List<Report>) in.readObject()).forEach(reports::save);
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Could not read data file: " + e.getMessage());
        }
    }
}
