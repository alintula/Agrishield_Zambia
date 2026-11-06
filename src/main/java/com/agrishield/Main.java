package com.agrishield;

import com.agrishield.api.ApiServer;
import com.agrishield.service.AuthService;
import com.agrishield.service.FieldService;
import com.agrishield.service.ReportService;
import com.agrishield.store.DataStore;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));
        Path dataFile = Path.of(System.getenv().getOrDefault("DATA_FILE", "agrishield-data.ser"));

        DataStore store = new DataStore(dataFile);
        AuthService auth = new AuthService(store);
        auth.seedAdmin(System.getenv().getOrDefault("ADMIN_PHONE", "+260970000000"),
                System.getenv().getOrDefault("ADMIN_PASSWORD", "admin123"));

        new ApiServer(auth, new FieldService(store), new ReportService(store)).start(port);

        System.out.println("Agrishield Zambia is running at http://localhost:" + port);
    }
}
