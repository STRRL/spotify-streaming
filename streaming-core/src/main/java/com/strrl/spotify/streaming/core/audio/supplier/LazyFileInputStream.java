package com.strrl.spotify.streaming.core.audio.supplier;

import com.strrl.spotify.streaming.core.exception.PipeAlreadyClosedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

/**
 * @author strrl
 * @date 2020/1/28 15:02
 */
public class LazyFileInputStream extends InputStream {

  private static final ExecutorService FIFO_FILE_INIT_THREAD_POOL =
      new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

  @Nonnull private File target;
  private boolean initialized;
  private InputStream backend;
  private boolean closed;

  public LazyFileInputStream(@Nonnull File target) {
    this.target = target;
    this.initialized = false;
    this.closed = false;
  }

  private synchronized void acquireInitialized() {
    if (this.closed) {
      throw PipeAlreadyClosedException.of();
    }
    if (!this.initialized) {
      final Future<?> future =
          FIFO_FILE_INIT_THREAD_POOL.submit(
              () -> {
                try {
                  this.backend = new FileInputStream(target);
                  this.initialized = true;
                } catch (FileNotFoundException e) {
                  throw new RuntimeException(e);
                }
              });
      for (; ; ) {
        if (!future.isDone()) {
          if (this.closed) {
            break;
          } else {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }
      }
    }
  }

  @Override
  public synchronized int read() throws IOException {
    this.acquireInitialized();
    return this.backend.read();
  }

  @Override
  public int read(@NotNull byte[] b) throws IOException {
    this.acquireInitialized();
    return this.backend.read(b);
  }

  @Override
  public int read(@NotNull byte[] b, int off, int len) throws IOException {
    this.acquireInitialized();
    return this.backend.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    this.acquireInitialized();
    return this.backend.skip(n);
  }

  @Override
  public int available() throws IOException {
    this.acquireInitialized();
    return this.backend.available();
  }

  @Override
  public void close() throws IOException {
    if (this.initialized) {
      this.backend.close();
    }
    this.closed = true;
    FIFO_FILE_INIT_THREAD_POOL.shutdownNow();
  }

  @Override
  public synchronized void mark(int readlimit) {
    this.acquireInitialized();
    this.backend.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    this.acquireInitialized();
    this.backend.reset();
  }

  @Override
  public boolean markSupported() {
    this.acquireInitialized();
    return this.backend.markSupported();
  }
}
