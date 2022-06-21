package com.hankoh.scheduleapp.controller;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Internationalizable {

    protected final ResourceBundle msg = ResourceBundle.getBundle(
            "com.hankoh.scheduleapp.properties.MessagesBundle",
            Locale.getDefault()
    );
}
