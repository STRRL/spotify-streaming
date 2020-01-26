package com.strrl.spotify.streaming.core;

import com.google.common.util.concurrent.RateLimiter;
import com.strrl.spotify.streaming.core.config.PipeProperties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Listen controller.
 *
 * @author strrl
 * @date 2020/1/24 22:36
 */
@Slf4j
@RestController
public class ListenController {
  private final PipeProperties pipeProperties;
  DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
  private Flux<DataBuffer> originCache;
  private FileInputStream fileInputStream;

  /**
   * Default constructor.
   *
   * @param pipeProperties config contains pipe path.
   */
  public ListenController(PipeProperties pipeProperties) {
    this.pipeProperties = pipeProperties;
  }

  /** attaching audio streaming. */
  @GetMapping(value = "/listen", produces = "audio/wav")
  public Flux<DataBuffer> listen() throws FileNotFoundException {
    return this.waveHeader().concatWith(this.origin().doOnTerminate(() -> {}));
  }

  private synchronized Flux<DataBuffer> origin() throws FileNotFoundException {
    if (this.originCache != null) {
      return originCache;
    } else {
      this.fileInputStream = new FileInputStream(pipeProperties.getPath().toString());
      final RateLimiter rateLimiter = RateLimiter.create(44100 * 2 * 2);
      final Flux<DataBuffer> generated =
          Flux.generate(
              sink -> {
                final DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
                final OutputStream outputStream = dataBuffer.asOutputStream();
                byte[] cache = new byte[1024 * 4];
                final int read;
                try {
                  read = this.fileInputStream.read(cache);
                  log.trace("Read from pipe {}", read);
                  rateLimiter.acquire(read);
                  if (read > 0) {
                    outputStream.write(cache, 0, read);
                    sink.next(dataBuffer);
                  } else {
                    sink.complete();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                  sink.error(e);
                }
              });
      this.originCache = generated.subscribeOn(Schedulers.elastic()).share();
      this.originCache.subscribeOn(Schedulers.elastic()).subscribe();

      return this.originCache;
    }
  }

  private byte[] wavHeader() {
    byte[] header = new byte[44];
    long srate = 44100;
    long channel = 2;
    long format = 16;
    final long length = Integer.MAX_VALUE - 36;
    final long totalDataLen = length;
    final long bitrate = srate * channel * format;

    header[0] = 'R';
    header[1] = 'I';
    header[2] = 'F';
    header[3] = 'F';
    header[4] = (byte) (totalDataLen & 0xff);
    header[5] = (byte) ((totalDataLen >> 8) & 0xff);
    header[6] = (byte) ((totalDataLen >> 16) & 0xff);
    header[7] = (byte) ((totalDataLen >> 24) & 0xff);
    header[8] = 'W';
    header[9] = 'A';
    header[10] = 'V';
    header[11] = 'E';
    header[12] = 'f';
    header[13] = 'm';
    header[14] = 't';
    header[15] = ' ';
    header[16] = (byte) format;
    header[17] = 0;
    header[18] = 0;
    header[19] = 0;
    header[20] = 1;
    header[21] = 0;
    header[22] = (byte) channel;
    header[23] = 0;
    header[24] = (byte) (srate & 0xff);
    header[25] = (byte) ((srate >> 8) & 0xff);
    header[26] = (byte) ((srate >> 16) & 0xff);
    header[27] = (byte) ((srate >> 24) & 0xff);
    header[28] = (byte) ((bitrate / 8) & 0xff);
    header[29] = (byte) (((bitrate / 8) >> 8) & 0xff);
    header[30] = (byte) (((bitrate / 8) >> 16) & 0xff);
    header[31] = (byte) (((bitrate / 8) >> 24) & 0xff);
    header[32] = (byte) ((channel * format) / 8);
    header[33] = 0;
    header[34] = 16;
    header[35] = 0;
    header[36] = 'd';
    header[37] = 'a';
    header[38] = 't';
    header[39] = 'a';
    header[40] = (byte) (length & 0xff);
    header[41] = (byte) ((length >> 8) & 0xff);
    header[42] = (byte) ((length >> 16) & 0xff);
    header[43] = (byte) ((length >> 24) & 0xff);

    return header;
  }

  private Mono<DataBuffer> waveHeader() {
    final DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
    dataBuffer.write(this.wavHeader());
    return Mono.just(dataBuffer);
  }
}
