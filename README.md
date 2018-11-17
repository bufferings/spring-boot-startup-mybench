# spring-boot-startup-mybench

I watched a session which Dave presented at Spring One Platform 2018.

https://springoneplatform.io/2018/sessions/how-fast-is-spring-

Then I tried it by myself in this repo to understand his session.

## BlogPost

https://dev.to/bufferings/lets-make-springboot-app-start-faster-k9m

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
