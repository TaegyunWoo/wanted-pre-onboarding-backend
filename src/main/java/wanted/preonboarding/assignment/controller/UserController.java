/**
 * packageName    : wanted.preonboarding.assignment.controller
 * fileName       : UserController
 * author         : 우태균
 * date           : 2023/08/07
 * description    : User 관련 컨트롤러. 애너테이션 정보는 UserApi 인터페이스 참고
 */
package wanted.preonboarding.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.assignment.service.UserService;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;
import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
  private final UserService userService;

  @Override
  public void postSignUp(UserRequest userRequest) {
    userService.registerNewUser(userRequest);
  }

  @Override
  public TokenResponse postSignIn(UserRequest userRequest) {

    return null;
  }
}
