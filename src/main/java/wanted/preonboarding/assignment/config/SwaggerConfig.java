/**
 * packageName    : wanted.preonboarding.assignment.config
 * fileName       : SwaggerConfig
 * author         : 우태균
 * description    : Swagger 적용을 위한 Spring Docs 라이브러리 관련 설정
 */
package wanted.preonboarding.assignment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .components(components())
        .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("Wanted Pre-Onboarding Assignment")
        .version("1.0.0");
  }

  private Components components() {
    return new Components()
        .addSecuritySchemes(SECURITY_SCHEME_NAME,
            securitySchemesItem()
        );
  }

  private SecurityScheme securitySchemesItem() {
    return new SecurityScheme()
        .name(SECURITY_SCHEME_NAME)
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
  }
}
