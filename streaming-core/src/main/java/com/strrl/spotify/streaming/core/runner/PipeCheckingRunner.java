package com.strrl.spotify.streaming.core.runner;

import com.google.common.io.CharStreams;
import com.strrl.spotify.streaming.core.config.PipeProperties;
import com.strrl.spotify.streaming.core.exception.InvalidPipeException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create pipe file if not exists.
 *
 * @author strrl
 * @date 2020/1/26 22:58
 */
@Slf4j
@Component
@Order(1)
public class PipeCheckingRunner implements CommandLineRunner {
  @Nonnull private final PipeProperties pipeProperties;

  public PipeCheckingRunner(@Nonnull PipeProperties pipeProperties) {
    this.pipeProperties = pipeProperties;
  }

  private void createPipeFile(String pipePath) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command("mkfifo", pipePath);
    Process process = null;
    try {
      process = processBuilder.start();
      final boolean exited = process.waitFor(5, TimeUnit.SECONDS);
      if (exited) {
        final int exitVal = process.exitValue();
        if (exitVal == 0) {
          log.info("Pipe file automated created successfully.");
        } else {
          throw InvalidPipeException.createFiled(
              pipePath,
              new RuntimeException(
                  String.format(
                      "detail: %s%s",
                      CharStreams.toString(
                          new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)),
                      CharStreams.toString(
                          new InputStreamReader(
                              process.getErrorStream(), StandardCharsets.UTF_8)))));
        }
      } else {
        throw InvalidPipeException.createFiled(pipePath, new TimeoutException());
      }
    } catch (IOException | InterruptedException e) {
      throw InvalidPipeException.createFiled(pipePath, e);
    } finally {
      if (process != null) {
        process.destroy();
      }
    }
  }

  @Override
  public void run(String... args) throws Exception {
    if (this.pipeProperties.getPath().toFile().exists()) {
      log.info("Pipe file exists.");
    } else {
      this.createPipeFile(this.pipeProperties.getPath().toString());
    }
  }
}
