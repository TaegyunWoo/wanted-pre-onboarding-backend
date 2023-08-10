/**
 * packageName    : wanted.preonboarding.assignment.controller
 * fileName       : UserController
 * author         : 우태균
 * description    : User 관련 컨트롤러. 애너테이션 정보는 UserApi 인터페이스 참고
 */
package wanted.preonboarding.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.assignment.service.TokenService;
import wanted.preonboarding.assignment.service.UserService;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenRequest;
import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;
import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
  private final UserService userService;
  private final TokenService tokenService;

  /**
   * 회원가입 핸들러
   * @param userRequest 회원가입 정보
   */
  @Override
  public void postSignUp(UserRequest userRequest) {
    userService.registerNewUser(userRequest);
  }

  /**
   * 로그인 핸들러
   * @param userRequest 로그인 정보
   * @return Access·Refresh 토큰쌍
   */
  @Override
  public TokenResponse postSignIn(UserRequest userRequest) {
    TokenResponse response = userService.signIn(userRequest);
    return response;
  }

  /**
   * 토큰 갱신 핸들러
   * @param tokenRequest 만료된 AccessToken 및 유효한 RefreshToken
   * @return 새로 발급된 토큰 쌍
   */
  @Override
  public TokenResponse postToken(TokenRequest tokenRequest) {
    TokenResponse response = tokenService.reissue(tokenRequest);
    return response;
  }
}
