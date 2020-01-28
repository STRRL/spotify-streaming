package com.strrl.spotify.streaming.core.audio.supplier;

import com.google.common.util.concurrent.RateLimiter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Read and forwarding pcm data from a input stream.
 *
 * @author strrl
 * @date 2020/1/28 13:42
 */
@Slf4j
public class InputStreamPcmSupplier implements PcmSupplier {
  private int cap;
  @Nonnull private InputStream in;
  private Flux<ByteBuf> cache;
  @Nonnull private RateLimiter rateLimiter;

  public InputStreamPcmSupplier(
      int cap, @Nonnull RateLimiter rateLimiter, @Nonnull InputStream in) {
    this.cap = cap;
    this.in = in;
    this.rateLimiter = rateLimiter;
  }

  @Nonnull
  @Override
  public synchronized Flux<ByteBuf> attach() {
    if (cache == null) {
      final byte[] buffer = new byte[cap];
      this.cache =
          Flux.generate(
              sink -> {
                final ByteBuf result = Unpooled.buffer(cap, cap);
                final int read;
                try {
                  read = this.in.read(buffer);
                  log.trace("Read from input stream {}", read);
                  rateLimiter.acquire(read);
                  if (read > 0) {
                    result.writeBytes(buffer, 0, read);
                    sink.next(result);
                  } else {
                    sink.complete();
                  }
                } catch (IOException e) {
                  log.error("Can not fetch data from input stream.", e);
                  sink.error(e);
                }
              });
    }
    return cache;
  }

  @Override
  public void close() throws Exception {
    this.in.close();
  }
}
