package com.strrl.spotify.streaming.core;

import java.util.Locale;

/** detect current os. */
public class OSUtils {
  protected static OSType detectedOS;

  /**
   * detect current os.
   *
   * @return
   */
  public static OSType getOS() {
    if (detectedOS == null) {
      final String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
      if ((OS.contains("mac")) || (OS.contains("darwin"))) {
        detectedOS = OSType.MacOS;
      } else if (OS.contains("win")) {
        detectedOS = OSType.Windows;
      } else if (OS.contains("nux")) {
        detectedOS = OSType.Linux;
      } else if (OS.toLowerCase().contains("bsd")) {
        detectedOS = OSType.BSD;
      } else {
        detectedOS = OSType.Other;
      }
    }
    return detectedOS;
  }

  public enum OSType {
    Windows,
    MacOS,
    Linux,
    BSD,
    Other
  }
}
