package com.portpolio.beach_saver_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // 이 어노테이션을 추가하여 @ConfigurationProperties 클래스를 찾도록 합니다.
public class BeachSaverBackendApplication {

  public static void main(String[] args) {
    // spring.profiles.active 값 읽기 (없으면 dev)
    String profile =
        System.getProperty(
            "spring.profiles.active",
            System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev"));

    String dotenvFile = ".env." + profile;

    Dotenv dotenv = Dotenv.configure().filename(dotenvFile).ignoreIfMissing().load();

    // 모든 키를 시스템 환경변수로 등록
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    SpringApplication.run(BeachSaverBackendApplication.class, args);
  }
}
