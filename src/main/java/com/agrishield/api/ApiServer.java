package com.agrishield.api;

import com.agrishield.exception.AgriShieldException;
import com.agrishield.exception.ValidationException;
import com.agrishield.model.Field;
import com.agrishield.model.ProblemType;
import com.agrishield.model.Report;
import com.agrishield.model.ReportStatus;
import com.agrishield.model.User;
import com.agrishield.service.AuthService;
import com.agrishield.service.FieldService;
import com.agrishield.service.ReportService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/** Maps HTTP routes to the services. The frontend in resources/public calls these routes. */
public class ApiServer {
    private final AuthService auth;
    private final FieldService fields;
    private final ReportService reports;

    public ApiServer(AuthService auth, FieldService fields, ReportService reports) {
        this.auth = auth;
        this.fields = fields;
        this.reports = reports;
    }

    public Javalin start(int port) {
        Javalin app = Javalin.create(cfg -> cfg.staticFiles.add("/public", Location.CLASSPATH));

        app.exception(AgriShieldException.class, (e, ctx) -> {
            ctx.status(e.getStatus());
            ctx.json(Map.of("error", e.getMessage()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.json(Map.of("error", "Something went wrong on the server."));
        });

        app.post("/api/register", ctx -> {
            Map<String, Object> b = body(ctx);
            AuthService.Session s = auth.register(text(b, "name"), text(b, "phone"), text(b, "district"), raw(b, "password"));
            ctx.status(201).json(Map.of("token", s.token(), "user", s.user().toView()));
        });
        app.post("/api/login", ctx -> {
            Map<String, Object> b = body(ctx);
            AuthService.Session s = auth.login(text(b, "phone"), raw(b, "password"));
            ctx.json(Map.of("token", s.token(), "user", s.user().toView()));
        });
        app.post("/api/logout", ctx -> {
            auth.logout(token(ctx));
            ctx.json(Map.of("ok", true));
        });
        app.get("/api/me", ctx -> ctx.json(current(ctx).toView()));
        app.get("/api/users", ctx ->
                ctx.json(auth.listUsers(current(ctx)).stream().map(User::toView).toList()));

        app.get("/api/fields", ctx -> ctx.json(fields.list(current(ctx)).stream().map(Field::toView).toList()));
        app.post("/api/fields", ctx -> {
            Map<String, Object> b = body(ctx);
            Field f = fields.create(current(ctx), text(b, "name"), text(b, "crop"), number(b, "size"));
            ctx.status(201).json(f.toView());
        });
        app.delete("/api/fields/{id}", ctx -> {
            fields.delete(current(ctx), ctx.pathParam("id"));
            ctx.json(Map.of("ok", true));
        });

        app.get("/api/reports", ctx -> ctx.json(reports.list(current(ctx)).stream().map(Report::toView).toList()));
        app.post("/api/reports", ctx -> {
            Map<String, Object> b = body(ctx);
            Report r = reports.create(current(ctx), text(b, "field"), ProblemType.fromLabel(text(b, "type")),
                    (int) number(b, "sev"), date(text(b, "date")), text(b, "desc"), text(b, "action"));
            ctx.status(201).json(r.toView());
        });
        app.delete("/api/reports/{id}", ctx -> {
            reports.delete(current(ctx), ctx.pathParam("id"));
            ctx.json(Map.of("ok", true));
        });
        app.patch("/api/reports/{id}/status", ctx -> {
            Map<String, Object> b = body(ctx);
            Report r = reports.changeStatus(current(ctx), ctx.pathParam("id"), ReportStatus.fromLabel(text(b, "status")));
            ctx.json(r.toView());
        });

        return app.start(port);
    }

    // ----- helpers -----
    private String token(Context ctx) {
        String h = ctx.header("Authorization");
        return (h != null && h.startsWith("Bearer ")) ? h.substring(7) : null;
    }

    private User current(Context ctx) {
        return auth.authenticate(token(ctx));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> body(Context ctx) {
        try {
            Map<String, Object> m = ctx.bodyAsClass(Map.class);
            return m == null ? new HashMap<>() : m;
        } catch (Exception e) {
            throw new ValidationException("Invalid request body.");
        }
    }

    private String raw(Map<String, Object> b, String key) {
        Object v = b.get(key);
        return v == null ? "" : v.toString();
    }

    private String text(Map<String, Object> b, String key) {
        return raw(b, key).trim();
    }

    private double number(Map<String, Object> b, String key) {
        Object v = b.get(key);
        if (v instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (NumberFormatException e) {
            throw new ValidationException("Enter a valid number for " + key + ".");
        }
    }

    private LocalDate date(String s) {
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Choose a valid date.");
        }
    }
}
