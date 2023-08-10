/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : ResolverConfig
 * author         : 우태균
 * description    : 리졸버 관련 Config
 */
package wanted.preonboarding.assignment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wanted.preonboarding.assignment.controller.resolver.LoginUserArgumentResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ResolverConfig implements WebMvcConfigurer {
  private final LoginUserArgumentResolver loginUserArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserArgumentResolver);
  }
}
