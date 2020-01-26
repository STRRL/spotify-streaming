package com.strrl.spotify.streaming.core;

import java.util.Locale;
public class OSUtils {
    protected static OSType detectedOS;
    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    public OSType getOS(){
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                detectedOS = OSType.MacOS;
            } else if (OS.contains("win")) {
                detectedOS = OSType.Windows;
            } else if (OS.contains("nux")) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}
