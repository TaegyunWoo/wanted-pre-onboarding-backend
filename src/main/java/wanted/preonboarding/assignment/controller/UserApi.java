/**
 * packageName    : wanted.preonboarding.assignment.controller
 * fileName       : UserApi
 * author         : 우태균
 * description    : User 관련 API 인터페이스.
 *                  모든 애너테이션을 인터페이스에 모아둬, 실제 컨트롤러 가독성 개선
 */
package wanted.preonboarding.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wanted.preonboarding.assignment.dto.TokenDto;
import wanted.preonboarding.assignment.dto.UserDto;

import javax.validation.Valid;

@Tag(name = "사용자 API")
@RequestMapping("/user")
public interface UserApi {
  @Operation(summary = "회원가입")
  @ApiResponse(responseCode = "200", description = "회원가입 성공")
  @ApiResponse(responseCode = "400", description = "입력 형식 오류, 조건사항 비만족, 아이디 중복")
  @PostMapping
  void postSignUp(
      @RequestBody @Valid UserDto.UserRequest userRequest
  );

  @Operation(summary = "로그인")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @ApiResponse(responseCode = "400", description = "입력 형식 오류, 조건사항 비만족")
  @ApiResponse(responseCode = "401", description = "잘못된 로그인 정보")
  @PostMapping("/sign-in")
  TokenDto.TokenResponse postSignIn(
      @RequestBody @Valid UserDto.UserRequest userRequest
  );

  @Operation(summary = "토큰 재발급", description = "Access Token 만료시, Refresh Token을 통해 토큰 갱신")
  @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
  @ApiResponse(responseCode = "401", description = "비정상 토큰, 발급한 적이 없는 토큰으로 요청, 아직 Access Token이 만료되지 않은 경우, Refresh Token이 만료된 경우")
  @PostMapping("/token")
  TokenDto.TokenResponse postToken(
      @RequestBody TokenDto.TokenRequest tokenRequest
  );
}
