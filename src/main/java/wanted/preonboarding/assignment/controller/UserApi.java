package wanted.preonboarding.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wanted.preonboarding.assignment.dto.TokenDto;
import wanted.preonboarding.assignment.dto.UserDto;

@Tag(name = "사용자 API")
@RequestMapping("/user")
public interface UserApi {
  @Operation(summary = "회원가입")
  @ApiResponse(responseCode = "200", description = "회원가입 성공")
  @PostMapping
  void postSignUp(
      @RequestBody @Validated UserDto.UserRequest userRequest
  );

  @Operation(summary = "로그인")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @PostMapping("/sign-in")
  TokenDto.TokenResponse postSignIn(
      @RequestBody @Validated UserDto.UserRequest userRequest
  );
}
