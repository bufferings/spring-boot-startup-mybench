/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@State(Scope.Benchmark)
public abstract class SimpleProcessLauncherState {

  private File home = new File("target");

  private List<String> args;

  private Process started;

  public void init(String jarPath, String... additionalArgs) {
    args = new ArrayList<>();
    args.addAll(Arrays.asList("java", "-Xmx128m", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=0"));
    args.addAll(Arrays.asList(additionalArgs));
    args.addAll(Arrays.asList("-jar", jarPath));
  }

  public void initWithCds(String jarPath, String... additionalArgs) {
    args = new ArrayList<>();
    args.addAll(Arrays.asList("java", "-Xmx128m", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=0"));
    args.addAll(Arrays.asList("-Xshare:on", "-XX:SharedArchiveFile=app.jsa"));
    args.addAll(Arrays.asList(additionalArgs));
    args.addAll(Arrays.asList("-jar", jarPath));

    generateCdsClassList(home, jarPath, "org.springframework.boot.loader.JarLauncher");
    generateCdsClassDataArchive(home, jarPath);
  }

  public void initThinJar(String thinJarPath, String... additionalArgs) {
    var classPath = generateThinJarClassPath(home, thinJarPath);
    var mainClass = "com.example.DemoApplication";

    args = new ArrayList<>();
    args.addAll(Arrays.asList("java", "-Xmx128m", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=0"));
    args.addAll(Arrays.asList(additionalArgs));
    args.addAll(Arrays.asList("-cp", classPath, mainClass));
  }

  public void initThinJarWithCds(String thinJarPath, String... additionalArgs) {
    var classPath = generateThinJarClassPath(home, thinJarPath);
    var mainClass = "com.example.DemoApplication";

    args = new ArrayList<>();
    args.addAll(Arrays.asList("java", "-Xmx128m", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=0"));
    args.addAll(Arrays.asList("-Xshare:on", "-XX:SharedArchiveFile=app.jsa"));
    args.addAll(Arrays.asList(additionalArgs));
    args.addAll(Arrays.asList("-cp", classPath, mainClass));

    generateCdsClassList(home, classPath, mainClass);
    generateCdsClassDataArchive(home, classPath);
  }

  public void run() throws Exception {
    var builder = new ProcessBuilder(args);
    builder.directory(home);
    builder.redirectErrorStream(true);
    started = builder.start();
    System.out.println(output(started.getInputStream(), "Started"));
  }

  @TearDown(Level.Iteration)
  public void afterIteration() throws Exception {
    if (started != null && started.isAlive()) {
      started.destroyForcibly().waitFor();
    }
  }

  private static String output(InputStream inputStream, String marker) throws IOException {
    var sb = new StringBuilder();
    var br = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    while ((line = br.readLine()) != null && (marker == null || !line.contains(marker))) {
      sb.append(line).append(System.getProperty("line.separator"));
    }
    if (line != null) {
      sb.append(line).append(System.getProperty("line.separator"));
    }
    return sb.toString();
  }

  private static void generateCdsClassList(File home, String classPath, String mainClass) {
    try {
      var builder = new ProcessBuilder("java",
          "-Xshare:off", "-XX:DumpLoadedClassList=app.classlist", "-Dserver.port=0",
          "-cp", classPath, mainClass);
      builder.directory(home);
      builder.redirectErrorStream(true);

      var process = builder.start();
      System.out.println(output(process.getInputStream(), "Started"));
      process.destroyForcibly();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate CDS ClassList.", e);
    }
  }

  private static void generateCdsClassDataArchive(File home, String classPath) {
    try {
      var builder = new ProcessBuilder("java",
          "-Xshare:dump", "-XX:SharedClassListFile=app.classlist",
          "-XX:SharedArchiveFile=app.jsa", "-cp", classPath);
      builder.directory(home);
      builder.redirectErrorStream(true);

      var process = builder.start();
      System.out.println(output(process.getInputStream(), null));
      process.waitFor();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate CDS ClassDataArchive.", e);
    }
  }

  private static String generateThinJarClassPath(File home, String thinJarPath) {
    try {
      var builder = new ProcessBuilder("java", "-Dthin.classpath", "-jar", thinJarPath);
      builder.directory(home);
      builder.redirectErrorStream(true);

      var process = builder.start();
      String classPath = output(process.getInputStream(), null);
      process.destroyForcibly();
      return classPath;
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate ThinJar classpath.", e);
    }
  }
}