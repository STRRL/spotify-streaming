package com.strrl.spotify.streaming.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.gianlu.librespot.AbsConfiguration;
import xyz.gianlu.librespot.FileConfiguration;
import xyz.gianlu.librespot.core.AuthConfiguration;
import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.core.ZeroconfServer;
import xyz.gianlu.librespot.mercury.MercuryClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author strrl
 * @date 2020/1/24 22:04
 */
@Component
public class SpotifyServerRunner implements CommandLineRunner {
  private static final ExecutorService SPOTIFY_POOL =
      new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

  @Override
  public void run(String... args) throws Exception {
    SPOTIFY_POOL.submit(
        () -> {
          try {
            AbsConfiguration conf = new FileConfiguration(args);
            if (conf.authStrategy() == AuthConfiguration.Strategy.ZEROCONF) {
              ZeroconfServer.create(conf);
            } else {
              new Session.Builder(conf).create();
            }
          } catch (IOException
              | MercuryClient.MercuryException
              | GeneralSecurityException
              | Session.SpotifyAuthenticationException e) {
            e.printStackTrace();
          }
        });
  }
}
