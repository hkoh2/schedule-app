package com.hankoh.scheduleapp.controller;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract class to handle locale.
 */
public abstract class Internationalizable {

    /**
     * MessageBundle for locale.
     */
    protected final ResourceBundle msg = ResourceBundle.getBundle(
            "com.hankoh.scheduleapp.properties.MessagesBundle",
            Locale.getDefault()
    );
}
