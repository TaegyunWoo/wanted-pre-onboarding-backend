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

  }

  @Override
  public TokenResponse postSignIn(UserRequest userRequest) {

    return null;
  }
}
