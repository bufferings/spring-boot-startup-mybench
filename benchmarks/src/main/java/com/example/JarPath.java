package com.example;

import java.io.File;
import java.io.IOException;

public enum JarPath {
  WEB("demo-web", "0.0.1-SNAPSHOT"),
  FLUX("demo-flux", "0.0.1-SNAPSHOT"),
  FLUX_WITH_CONTEXT_INDEXER("demo-flux-context-indexer", "0.0.1-SNAPSHOT"),
  FLUX_LAZY_INIT("demo-flux-lazy-init", "0.0.1-SNAPSHOT"),
  FLUX_WO_LOGBACK("demo-flux-without-logback", "0.0.1-SNAPSHOT"),
  FLUX_WO_JACKSON("demo-flux-without-jackson", "0.0.1-SNAPSHOT"),
  FLUX_WO_HIBERNATE_VALIDATOR("demo-flux-without-hibernate-validator", "0.0.1-SNAPSHOT"),
  FLUX_THIN("demo-flux-thin", "0.0.1-SNAPSHOT"),
  FLUX_THIN_ALL("demo-flux-thin-all", "0.0.1-SNAPSHOT");

  private String path;

  JarPath(String artifactId, String version) {
    try {
      path = new File("..").getAbsoluteFile().getCanonicalPath() + File.separator
          + artifactId + File.separator + "target" + File.separator + artifactId
          + "-" + version + ".jar";
    } catch (IOException e) {
      throw new IllegalStateException("Cannot find benchmarks", e);
    }
  }

  public String path() {
    return path;
  }

}
