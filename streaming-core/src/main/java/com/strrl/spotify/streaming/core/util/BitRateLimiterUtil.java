package com.strrl.spotify.streaming.core.util;

import com.google.common.util.concurrent.RateLimiter;
import javax.annotation.Nonnull;

/**
 * @author strrl
 * @date 2020/1/28 14:41
 */
public class BitRateLimiterUtil {
  @Nonnull
  public static RateLimiter create(int bitRate) {
    return RateLimiter.create(bitRate);
  }

  @Nonnull
  public static RateLimiter create(int sampleRate, int bitDepth, int channel) {
    return create(sampleRate * bitDepth * channel / 8);
  }
}
