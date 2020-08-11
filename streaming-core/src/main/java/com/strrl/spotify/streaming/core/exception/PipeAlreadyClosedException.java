package com.strrl.spotify.streaming.core.exception;

/**
 * @author strrl
 * @date 2020/8/12 00:23
 */
public class PipeAlreadyClosedException extends SpotifyStreamingException {

  public PipeAlreadyClosedException() {}

  public PipeAlreadyClosedException(String message) {
    super(message);
  }

  public PipeAlreadyClosedException(String message, Throwable cause) {
    super(message, cause);
  }

  public PipeAlreadyClosedException(Throwable cause) {
    super(cause);
  }

  public PipeAlreadyClosedException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static PipeAlreadyClosedException of() {
    return new PipeAlreadyClosedException("fifo file already closed");
  }
}
