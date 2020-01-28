package com.strrl.spotify.streaming.application;

import com.strrl.spotify.streaming.core.util.OSUtils;
import com.strrl.spotify.streaming.core.util.OSUtils.OSType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot application main class.
 *
 * @author strrl
 * @date 2020/1/24 21:51
 */
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    // detect if linux or mac, if windows, decline to run.
    OSType osName = OSUtils.getOS();
    if (osName == OSType.Windows || osName == OSType.Other) {
      System.out.println("We only support MAC and Linux");
      System.exit(-1);
    }
    // Start up spring app.
    SpringApplication.run(Application.class, args);
  }
}
