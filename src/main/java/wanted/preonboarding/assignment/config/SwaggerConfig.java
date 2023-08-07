/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : SwaggerConfig
 * author         : 우태균
 * date           : 2023/08/07
 * description    : Swagger 적용을 위한 Spring Docs 라이브러리 관련 설정
 */
package wanted.preonboarding.assignment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("Wanted Pre-Onboarding Assignment")
        .version("1.0.0");
  }
}
