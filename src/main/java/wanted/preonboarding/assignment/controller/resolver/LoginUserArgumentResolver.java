/**
 * packageName    : wanted.preonboarding.assignment.controller.resolver
 * fileName       : LoginUserArgumentResolver
 * author         : 우태균
 * date           : 2023/08/08
 * description    : 로그인 사용자 객체를 컨트롤러 파라미터에 넘겨주는 Argument Resolver
 */
package wanted.preonboarding.assignment.controller.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wanted.preonboarding.assignment.dto.LoginUser;
import wanted.preonboarding.assignment.interceptor.AuthInterceptor;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(LoginUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
    long userPk = (long) httpServletRequest.getAttribute(AuthInterceptor.USER_PK_ATTRIBUTE_NAME);
    return new LoginUser(userPk);
  }
}
