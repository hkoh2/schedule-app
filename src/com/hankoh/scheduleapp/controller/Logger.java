package com.hankoh.scheduleapp.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * Logging user logins. Saves login attempts to file.
 */
public class Logger {
    private static final String logFile = "login_activity.txt";

    /**
     * Logs login attempts.
     *
     * @param username   username attempting to login.
     * @param isLoggedIn validation for login.
     */
    public static void loginAttempted(String username, boolean isLoggedIn) {
        try {
            File file = new File(logFile);
            if (file.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File exists");
            }

            String status = isLoggedIn ? "SUCCESS" : "FAILED";
            FileWriter writer = new FileWriter(logFile, true);
            //ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            writer.write(Instant.now() + "\t" + username + "\t" + status + "\n");
            writer.close();
            System.out.println("Login attempt logged");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
