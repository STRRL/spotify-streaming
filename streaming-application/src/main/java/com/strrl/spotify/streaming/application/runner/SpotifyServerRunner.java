package com.strrl.spotify.streaming.application.runner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.gianlu.librespot.AbsConfiguration;
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
@Slf4j
@Component
@Order(2)
public class SpotifyServerRunner implements CommandLineRunner, AutoCloseable {
  private static final ExecutorService SPOTIFY_POOL =
      new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

  private final AbsConfiguration conf;
  private ZeroconfServer zeroconfServer;
  private Session session;

  public SpotifyServerRunner(AbsConfiguration configuration) {
    this.conf = configuration;
  }

  @Override
  public void run(String... args) throws Exception {
    SPOTIFY_POOL.submit(
        () -> {
          try {
            if (conf.authStrategy() == AuthConfiguration.Strategy.ZEROCONF) {
              this.zeroconfServer = ZeroconfServer.create(conf);
            } else {
              this.session = new Session.Builder(conf).create();
            }
          } catch (IOException
              | MercuryClient.MercuryException
              | GeneralSecurityException
              | Session.SpotifyAuthenticationException e) {
            e.printStackTrace();
          }
        });
  }

  @Override
  public void close() throws Exception {
    log.info("closing librespot.");
    if (this.zeroconfServer != null) {
      this.zeroconfServer.close();
    }
    if (this.session != null) {
      this.session.close();
    }
    SPOTIFY_POOL.shutdown();
  }
}
