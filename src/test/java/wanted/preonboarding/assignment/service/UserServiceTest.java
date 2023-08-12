package wanted.preonboarding.assignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.dto.TokenDto;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.repository.TokenPairRepository;
import wanted.preonboarding.assignment.repository.UserRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenIssuer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static wanted.preonboarding.assignment.dto.TokenDto.*;
import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

class UserServiceTest {
  private UserService userService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtTokenIssuer jwtTokenIssuer;
  @Mock
  private UserRepository userRepository;
  @Mock
  private TokenPairRepository tokenPairRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(passwordEncoder, jwtTokenIssuer, userRepository, tokenPairRepository);
  }

  @Test
  void 중복되지_않는_계정ID로_새_사용자_등록() {
    //GIVEN
    String validAccountId = "not duplicated account id";
    String rawPw = "my raw pw";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(validAccountId)
        .rawPassword(rawPw)
        .build();

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(validAccountId))).willReturn(Optional.empty());

    //WHEN
    try {
      userService.registerNewUser(userRequestDto);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception thrown\n" + e.getMessage());
    }
    then(passwordEncoder).should(times(1)).encode(eq(rawPw));
    then(userRepository).should(times(1)).save(any(User.class));
  }

  @Test
  void 중복된_계정ID로_새_사용자_등록() {
    //GIVEN
    String duplicatedAccountId = "duplicated account id";
    String rawPw = "my raw pw";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(duplicatedAccountId)
        .rawPassword(rawPw)
        .build();

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(duplicatedAccountId))).willReturn(Optional.of(new User()));

    //WHEN
    try {
      userService.registerNewUser(userRequestDto);

      //THEN
      fail("must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.DUPLICATED_ACCOUNT_ID, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception thrown\n" + e.getMessage());
    }
    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  void 토큰을_아직_발급받지_않은_상태에서_로그인() {
    //GIVEN
    String validAccountId = "valid account id";
    String validRawPw = "valid raw password";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(validAccountId)
        .rawPassword(validRawPw)
        .build();
    String validEncodedPw = "valid encoded password";
    long savedUserId = 1;
    User savedUserEntity = User.builder()
        .id(savedUserId)
        .accountId(validAccountId)
        .password(validEncodedPw)
        .build();
    String accessToken = "new access token";
    String refreshToken = "new refresh token";

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(validAccountId))).willReturn(Optional.of(savedUserEntity));
    //PasswordEncoder
    given(passwordEncoder.matches(eq(validRawPw), eq(validEncodedPw))).willReturn(true);
    //JwtTokenIssuer
    given(jwtTokenIssuer.createToken(savedUserId, false)).willReturn(accessToken);
    given(jwtTokenIssuer.createToken(savedUserId, true)).willReturn(refreshToken);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(savedUserId)).willReturn(Optional.empty());

    //WHEN
    TokenResponse tokenResponse = null;
    try {
      tokenResponse = userService.signIn(userRequestDto);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertNotNull(tokenResponse);
    assertEquals(accessToken, tokenResponse.getAccessToken());
    assertEquals(refreshToken, tokenResponse.getRefreshToken());
    then(tokenPairRepository).should(times(1)).save(any(TokenPair.class));
  }

  @Test
  void 이미_토큰을_발급받은_상태에서_로그인() {
    //GIVEN
    String validAccountId = "valid account id";
    String validRawPw = "valid raw password";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(validAccountId)
        .rawPassword(validRawPw)
        .build();
    String validEncodedPw = "valid encoded password";
    long savedUserId = 1;
    User savedUserEntity = User.builder()
        .id(savedUserId)
        .accountId(validAccountId)
        .password(validEncodedPw)
        .build();
    TokenPair oldTokenPairEntity = TokenPair.builder()
        .accessToken("old access token")
        .refreshToken("old refresh token")
        .build();
    String newAccessToken = "new access token";
    String newRefreshToken = "new refresh token";

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(validAccountId))).willReturn(Optional.of(savedUserEntity));
    //PasswordEncoder
    given(passwordEncoder.matches(eq(validRawPw), eq(validEncodedPw))).willReturn(true);
    //JwtTokenIssuer
    given(jwtTokenIssuer.createToken(savedUserId, false)).willReturn(newAccessToken);
    given(jwtTokenIssuer.createToken(savedUserId, true)).willReturn(newRefreshToken);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(savedUserId)).willReturn(Optional.of(oldTokenPairEntity));

    //WHEN
    TokenResponse tokenResponse = null;
    try {
      tokenResponse = userService.signIn(userRequestDto);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertNotNull(tokenResponse);
    assertEquals(newAccessToken, tokenResponse.getAccessToken());
    assertEquals(newRefreshToken, tokenResponse.getRefreshToken());
    then(tokenPairRepository).should(never()).save(any(TokenPair.class));
  }

  @Test
  void 존재하지_않는_계정ID로_로그인() {
    //GIVEN
    String notExistedAccountId = "not existed account id";
    String validRawPw = "valid raw password";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(notExistedAccountId)
        .rawPassword(validRawPw)
        .build();

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(notExistedAccountId))).willReturn(Optional.empty());

    //WHEN
    try {
      userService.signIn(userRequestDto);

      //THEN
      fail("must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.INVALID_SIGN_IN_INFO, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    then(jwtTokenIssuer).shouldHaveNoInteractions();
  }

  @Test
  void 잘못된_비밀번호로_로그인() {
    //GIVEN
    String validAccountId = "valid account id";
    String invalidRawPw = "invalid raw password";
    UserRequest userRequestDto = UserRequest.builder()
        .accountId(validAccountId)
        .rawPassword(invalidRawPw)
        .build();
    String validEncodedPw = "valid encoded password";
    long savedUserId = 1;
    User savedUserEntity = User.builder()
        .id(savedUserId)
        .accountId(validAccountId)
        .password(validEncodedPw)
        .build();

    //(mock)
    //UserRepository
    given(userRepository.findByAccountId(eq(validAccountId))).willReturn(Optional.empty());
    //PasswordEncoder
    given(passwordEncoder.matches(eq(invalidRawPw), eq(validEncodedPw))).willReturn(false);

    //WHEN
    try {
      userService.signIn(userRequestDto);

      //THEN
      fail("must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.INVALID_SIGN_IN_INFO, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    then(jwtTokenIssuer).shouldHaveNoInteractions();
  }
}