/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : SecurityConfig
 * author         : 우태균
 * description    : Spring Security 설정
 */
package wanted.preonboarding.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
  /**
   * 비밀번호 암호화에 사용되는 객체 Bean 등록
   * @return 비밀번호 암호화를 할 때 필요한 객체
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Spring Security 설정 메서드
   * 모든 설정을 꺼둠 (BCryptPasswordEncoder를 직접 사용하는 것이 목적이기 때문에)
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().disable()
        .csrf().disable()
        .formLogin().disable()
        .headers().frameOptions().disable();
    return http.build();
  }

}
