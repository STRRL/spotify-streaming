package com.strrl.spotify.streaming.core.runner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.gianlu.librespot.AbsConfiguration;
import xyz.gianlu.librespot.FileConfiguration;
import xyz.gianlu.librespot.core.AuthConfiguration;
import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.core.ZeroconfServer;
import xyz.gianlu.librespot.mercury.MercuryClient;

/**
 * Startup spotify server.
 *
 * @author strrl
 * @date 2020/1/24 22:04
 */
@Component
@Order(2)
public class SpotifyServerRunner implements CommandLineRunner {
  private static final ExecutorService SPOTIFY_POOL =
      new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

  private final AbsConfiguration conf;

  public SpotifyServerRunner(AbsConfiguration configuration) {
    this.conf = configuration;
  }

  @Override
  public void run(String... args) throws Exception {
    SPOTIFY_POOL.submit(
        () -> {
          try {
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
