package com.strrl.spotify.streaming.core.audio.supplier;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import reactor.core.publisher.Flux;

/**
 * Interface: supply audio PCM stream.
 *
 * @author strrl
 * @date 2020/1/28 13:40
 */
public interface PcmSupplier extends AutoCloseable {
  @Nonnull
  Flux<ByteBuf> attach();
}
