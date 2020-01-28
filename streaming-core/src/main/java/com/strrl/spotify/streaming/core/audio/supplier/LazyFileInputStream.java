package com.strrl.spotify.streaming.core.audio.supplier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

/**
 * @author strrl
 * @date 2020/1/28 15:02
 */
public class LazyFileInputStream extends InputStream {

  @Nonnull private File target;
  private boolean initialized;
  private InputStream backend;

  public LazyFileInputStream(@Nonnull File target) {
    this.target = target;
    this.initialized = false;
  }

  private synchronized void acquireInitialized() {
    if (!this.initialized) {
      try {
        this.backend = new FileInputStream(target);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
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
    this.acquireInitialized();
    this.backend.close();
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
