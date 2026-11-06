package com.agrishield.service;

import com.agrishield.exception.AccessDeniedException;
import com.agrishield.exception.AuthenticationException;
import com.agrishield.exception.DuplicateException;
import com.agrishield.model.Admin;
import com.agrishield.model.Farmer;
import com.agrishield.model.User;
import com.agrishield.store.DataStore;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Registration, login and token sessions. */
public class AuthService {
    public record Session(String token, User user) {}

    private final DataStore store;
    private final Map<String, String> tokens = new ConcurrentHashMap<>(); // token -> user id

    public AuthService(DataStore store) {
        this.store = store;
    }

    public Session register(String name, String phone, String district, String password) {
        User user = new Farmer(name, phone, district, password); // validates every value
        if (store.users.exists(u -> u.getPhone().equals(user.getPhone())))
            throw new DuplicateException("That phone number is already registered.");
        store.users.save(user);
        store.save();
        return open(user);
    }

    public Session login(String phone, String password) {
        String p = phone == null ? "" : phone.replaceAll("\\s", "");
        User user = store.users.findBy(u -> u.getPhone().equals(p)).stream().findFirst()
                .filter(u -> u.checkPassword(password))
                .orElseThrow(() -> new AuthenticationException("Phone number or password is incorrect."));
        return open(user);
    }

    public void logout(String token) {
        if (token != null) tokens.remove(token);
    }

    public User authenticate(String token) {
        String id = token == null ? null : tokens.get(token);
        if (id == null) throw new AuthenticationException("Please log in to continue.");
        return store.users.findById(id).orElseThrow(() -> new AuthenticationException("Please log in to continue."));
    }

    public List<User> listUsers(User requester) {
        if (!requester.canManageUsers()) throw new AccessDeniedException("Administrators only.");
        return store.users.findAll();
    }

    /** Creates the first administrator if none exists with this phone number. */
    public void seedAdmin(String phone, String password) {
        if (store.users.exists(u -> u.getPhone().equals(phone))) return;
        store.users.save(new Admin("Agrishield Admin", phone, "Lusaka", password));
        store.save();
    }

    private Session open(User user) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, user.getId());
        return new Session(token, user);
    }
}
