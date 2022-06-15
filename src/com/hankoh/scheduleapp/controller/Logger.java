package com.hankoh.scheduleapp.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

/**
 * User login attempts.
 */
public class Logger {
    private static final String logFile = "login_activity.txt";
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
            writer.write(LocalTime.now() + "\t" + username + "\t" + status + "\n");
            writer.close();
            System.out.println("Login attempt logged");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
