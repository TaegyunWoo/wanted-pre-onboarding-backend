/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : EmbeddedRedisConfig
 * author         : 우태균
 * date           : 2023/08/06
 * description    : Redis 연결 및 사용을 위한 Config 클래스
 */
package wanted.preonboarding.assignment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@Configuration
public class RedisRepositoryConfig {
  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  /**
   * Redis 연결 객체 생성용 Factory 객체를 Bean으로 등록
   * @return Redis 연결 객체 생성용 Factory 객체
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(redisHost, redisPort);
  }

  /**
   * Redis에 객체를 저장·조회할 때, 직렬화·역직렬화를 도와주는 템플릿 객체를 Bean으로 등록
   * @return Redis 템플릿 객체
   */
  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    return redisTemplate;
  }
}
