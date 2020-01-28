package com.strrl.spotify.streaming.application.config;

import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration entity.
 *
 * @author strrl
 * @date 2020/1/25 16:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PipeProperties {
  private Path path;
}
