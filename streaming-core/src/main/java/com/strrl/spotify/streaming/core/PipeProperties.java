package com.strrl.spotify.streaming.core;

import java.nio.file.Path;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration entity.
 *
 * @author strrl
 * @date 2020/1/25 16:05
 */
@Data
@Component
@ConfigurationProperties("pipe")
public class PipeProperties {
  private Path path;
}
