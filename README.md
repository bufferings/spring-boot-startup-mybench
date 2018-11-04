# spring-boot-startup-mybench

I watched a session of Dave and tried it by myself in this repo.

https://springoneplatform.io/2018/sessions/how-fast-is-spring-

## JDK

I used JDK 11 and didn't check other versions.

```
❯ java --version
openjdk 11.0.1 2018-10-16
OpenJDK Runtime Environment 18.9 (build 11.0.1+13)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.1+13, mixed mode)
```

## How to use

```
❯ ./mvnw clean package
❯ (cd benchmarks/; java -jar target/benchmarks.jar)
```

Then it shows results.
