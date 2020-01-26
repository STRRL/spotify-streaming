package com.strrl.spotify.streaming.core.exception;

import java.io.File;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Invalid pipe which connect spotify server part and streaming part.
 *
 * @author strrl
 * @date 2020/1/26 22:53
 */
public class InvalidPipeException extends SpotifyStreamingException {

  public InvalidPipeException() {}

  public InvalidPipeException(String message) {
    super(message);
  }

  public InvalidPipeException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidPipeException(Throwable cause) {
    super(cause);
  }

  public InvalidPipeException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Nonnull
  public static InvalidPipeException notExist(@Nonnull File pipePath) {
    return new InvalidPipeException(String.format("Pipe file not exist %s", pipePath.getPath()));
  }

  @Nonnull
  public static InvalidPipeException isNull() {
    return new InvalidPipeException("Pipe file from config is null.");
  }

  @Nonnull
  public static InvalidPipeException createFiled(@Nonnull String path, @Nullable Throwable cause) {
    return new InvalidPipeException(String.format("Failed to create a new pipe %s", path), cause);
  }
}
