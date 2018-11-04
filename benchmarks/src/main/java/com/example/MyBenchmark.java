package com.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;

@Measurement(iterations = 5)
@Warmup(iterations = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.SingleShotTime)
public class MyBenchmark {

  @Benchmark
  public void case01_FluxBaseline(FluxState state) throws Exception {
    state.run();
  }

  public static class FluxState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
       super.init(JarPath.FLUX.path());
    }
  }

  @Benchmark
  public void case02_Web(WebState state) throws Exception {
    state.run();
  }

  public static class WebState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.WEB.path());
    }
  }

  @Benchmark
  public void case03_WithContextIndexer(WithContextIndexerState state) throws Exception {
    state.run();
  }

  public static class WithContextIndexerState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX_WITH_CONTEXT_INDEXER.path());
    }
  }

  @Benchmark
  public void case04_WithLazyInit(WithLazyInitState state) throws Exception {
    state.run();
  }

  public static class WithLazyInitState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX_LAZY_INIT.path());
    }
  }

  @Benchmark
  public void case05_WithNoVerifyOption(WithNoVerifyOptionState state) throws Exception {
    state.run();
  }

  public static class WithNoVerifyOptionState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX.path(), "-noverify");
    }
  }

  @Benchmark
  public void case06_WithTieredStopAtLevel1Option(WithTieredStopAtLevel1OptionState state) throws Exception {
    state.run();
  }

  public static class WithTieredStopAtLevel1OptionState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX.path(),  "-XX:TieredStopAtLevel=1");
    }
  }

  @Benchmark
  public void case07_WithSpringConfigLocationOption(WithSpringConfigLocationOptionState state) throws Exception {
    state.run();
  }

  public static class WithSpringConfigLocationOptionState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX.path(),  "-Dspring.config.location=classpath:/application.properties");
    }
  }

  @Benchmark
  public void case08_WithJmxDisabledOption(WithJmxDisabledOptionState state) throws Exception {
    state.run();
  }

  public static class WithJmxDisabledOptionState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX.path(),  "-Dspring.jmx.enabled=false");
    }
  }

  @Benchmark
  public void case09_WithoutLogback(WithoutLogbackState state) throws Exception {
    state.run();
  }

  public static class WithoutLogbackState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX_WO_LOGBACK.path());
    }
  }

  @Benchmark
  public void case10_WithoutJackson(WithoutJacksonState state) throws Exception {
    state.run();
  }

  public static class WithoutJacksonState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX_WO_JACKSON.path());
    }
  }

  @Benchmark
  public void case11_WithoutHibernateValidator(WithoutHibernateValidatorState state) throws Exception {
    state.run();
  }

  public static class WithoutHibernateValidatorState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.init(JarPath.FLUX_WO_HIBERNATE_VALIDATOR.path());
    }
  }


  @Benchmark
  public void case12_WithAppCds(WithAppCdsState state) throws Exception {
    state.run();
  }

  public static class WithAppCdsState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.initWithCds(JarPath.FLUX.path());
    }
  }

  @Benchmark
  public void case13_Exploded(ExplodedState state) throws Exception {
    state.run();
  }

  public static class ExplodedState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.initThinJar(JarPath.FLUX_THIN.path());
    }
  }

  @Benchmark
  public void case14_ExplodedWithAppCds(ExplodedWithAppCdsState state) throws Exception {
    state.run();
  }

  public static class ExplodedWithAppCdsState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.initThinJarWithCds(JarPath.FLUX_THIN.path());
    }
  }

  @Benchmark
  public void case15_AllApplied(AllAppliedState state) throws Exception {
    state.run();
  }

  public static class AllAppliedState extends SimpleProcessLauncherState {
    @Setup(Level.Trial)
    public void beforeBenchmark() {
      super.initThinJarWithCds(JarPath.FLUX_THIN_ALL.path(),
          "-noverify",
          "-XX:TieredStopAtLevel=1",
          "-Dspring.config.location=classpath:/application.properties",
          "-Dspring.jmx.enabled=false");
    }
  }
}
