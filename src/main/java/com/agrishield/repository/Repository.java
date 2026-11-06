package com.agrishield.repository;

import com.agrishield.exception.NotFoundException;
import com.agrishield.model.Entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** A generic in-memory collection of entities, keyed by id. */
public class Repository<T extends Entity> {
    private final Map<String, T> items = new LinkedHashMap<>();

    public synchronized T save(T item) {
        items.put(item.getId(), item);
        return item;
    }

    public synchronized Optional<T> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public synchronized T getOrThrow(String id, String what) {
        return findById(id).orElseThrow(() -> new NotFoundException(what + " not found."));
    }

    public synchronized List<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public synchronized List<T> findBy(Predicate<T> test) {
        return items.values().stream().filter(test).collect(Collectors.toList());
    }

    public synchronized boolean exists(Predicate<T> test) {
        return items.values().stream().anyMatch(test);
    }

    public synchronized boolean delete(String id) {
        return items.remove(id) != null;
    }

    public synchronized int deleteIf(Predicate<T> test) {
        int before = items.size();
        items.values().removeIf(test);
        return before - items.size();
    }
}
