package com.strrl.spotify.streaming.core.config;

import com.strrl.spotify.streaming.core.exception.InvalidPipeException;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.gianlu.librespot.FileConfiguration;
import xyz.gianlu.librespot.player.AudioOutput;
import xyz.gianlu.librespot.player.Player;

/**
 * Application spring bean configuration.
 *
 * @author strrl
 * @date 2020/1/26 23:00
 */
@Slf4j
@Configuration
public class StreamServerConfiguration {

  /**
   * Load pipe path from librespot's config.
   *
   * @param configuration
   * @return
   */
  @Bean
  @Nonnull
  public PipeProperties pipeProperties(@Nonnull Player.Configuration configuration) {
    final File pipeFile = configuration.outputPipe();
    if (pipeFile == null) {
      throw InvalidPipeException.isNull();
    } else {
      return new PipeProperties(pipeFile.toPath());
    }
  }

  /**
   * Load librespot's configuration from file.
   *
   * @param args
   * @return
   * @throws IOException can not load configuration, thrown from librespot.
   */
  @Bean
  @Nonnull
  public FileConfiguration fileConfiguration(@Nonnull ApplicationArguments args)
      throws IOException {
    final FileConfiguration fileConfiguration = new FileConfiguration(args.getSourceArgs());
    if (fileConfiguration.output().equals(AudioOutput.PIPE)) {
      return fileConfiguration;
    } else {
      throw new InvalidPipeException(
          "We only support with pipe mode, please setup player.output=\"PIPE\" and specific a pipe file in config.toml");
    }
  }
}
