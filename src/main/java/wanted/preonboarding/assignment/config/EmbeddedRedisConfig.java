/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : EmbeddedRedisConfig
 * author         : 우태균
 * date           : 2023/08/06
 * description    : 로컬 환경에서 내장 Redis를 실행하고 중단하는 Config 클래스
 */
package wanted.preonboarding.assignment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@Slf4j
@Profile("local") //local properties 사용시, 활성화
@Configuration
public class EmbeddedRedisConfig {
  @Value("${spring.redis.port}")
  private int redisPort;

  private RedisServer redisServer;

  /**
   * 내장 Redis 실행 메서드
   * @throws IOException
   */
  @PostConstruct
  public void redisServer() throws IOException, URISyntaxException {
    if (isArmMac()) { //만약 실행 환경이 Mac M1이라면
      redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()),
          redisPort);
    } else { //실행 환경이 Mac M1이 아니라면
      redisServer = RedisServer.builder()
          .port(redisPort)
          .setting("maxmemory 128M")
          .build();
    }

    redisServer.start();
  }

  /**
   * 내장 Redis 중단 메서드
   */
  @PreDestroy
  public void stopRedis() {
    if (redisServer != null) {
      redisServer.stop();
    }
  }

  /**
   * 실행 환경이 Mac OS M1 인지 확인하는 메서드
   * @return
   */
  private boolean isArmMac() {
    return (Objects.equals(System.getProperty("os.arch"), "aarch64") ||
        Objects.equals(System.getProperty("os.arch"), "x86_64")) &&
        Objects.equals(System.getProperty("os.name"), "Mac OS X");
  }

  /**
   * Mac OS M1용 Redis 실행 바이너리 파일을 가져오는 메서드
   * @return M1용 Redis 바이너리 파일
   * @throws IOException
   */
  private File getRedisFileForArcMac() throws IOException {
    return new ClassPathResource("binary/redis-server-6.2.5-mac-arm64").getFile();
  }
}
