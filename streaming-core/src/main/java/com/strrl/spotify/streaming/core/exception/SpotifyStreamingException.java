package com.strrl.spotify.streaming.core.exception;

/**
 * @author strrl
 * @date 2020/1/26 22:53
 */
public abstract class SpotifyStreamingException extends RuntimeException {

  public SpotifyStreamingException() {}

  public SpotifyStreamingException(String message) {
    super(message);
  }

  public SpotifyStreamingException(String message, Throwable cause) {
    super(message, cause);
  }

  public SpotifyStreamingException(Throwable cause) {
    super(cause);
  }

  public SpotifyStreamingException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
