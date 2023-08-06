package wanted.preonboarding.assignment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;
import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
  @Override
  public void postSignUp(UserRequest userRequest) {

  }

  @Override
  public TokenResponse postSignIn(UserRequest userRequest) {

    return null;
  }
}
