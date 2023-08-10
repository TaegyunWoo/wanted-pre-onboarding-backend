/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : InterceptorConfig
 * author         : 우태균
 * description    : 인터셉터 관련 Config
 */
package wanted.preonboarding.assignment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wanted.preonboarding.assignment.interceptor.AuthInterceptor;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  private final AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor)
        .order(0)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/user",
            "/user/sign-in",
            "/user/token",
            "/swagger",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/error");
  }
}
