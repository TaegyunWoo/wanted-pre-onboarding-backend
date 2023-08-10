/**
 * packageName    : wanted.preonboarding.assignment.service
 * fileName       : UserService
 * author         : 우태균
 * description    : 사용자 Service
 */
package wanted.preonboarding.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.mapper.TokenPairMapper;
import wanted.preonboarding.assignment.mapper.UserMapper;
import wanted.preonboarding.assignment.repository.TokenPairRedisRepository;
import wanted.preonboarding.assignment.repository.UserRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenIssuer;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;
import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

@RequiredArgsConstructor
@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenIssuer jwtTokenIssuer;
  private final UserRepository userRepository;
  private final TokenPairRedisRepository tokenPairRedisRepository;

  /**
   * 새로운 사용자를 등록(생성)하는 메서드
   * @param request 등록할 사용자 정보 (DTO)
   */
  @Transactional
  public void registerNewUser(UserRequest request) {
    //아이디 중복 확인
    userRepository.findByAccountId(request.getAccountId()).ifPresent(user -> {
          throw new InvalidValueException(ErrorCode.DUPLICATED_ACCOUNT_ID);
    });

    //비밀번호 암호화
    String encodedPw = passwordEncoder.encode(request.getRawPassword());

    //DTO -> Entity
    User userEntity = UserMapper.INSTANCE.toEntity(request, encodedPw);

    //Save Entity
    userRepository.save(userEntity);
  }

  @Transactional(readOnly = true)
  public TokenResponse signIn(UserRequest request) {
    //사용자 로그인 정보 검증
    User user = validateSignInUser(request);

    //토큰 발급
    String accessToken = jwtTokenIssuer.createToken(user.getId(), false);
    String refreshToken = jwtTokenIssuer.createToken(user.getId(), true);

    //토큰 저장
    TokenPair tokenPair = TokenPairMapper.INSTANCE.toEntity(accessToken, refreshToken, user.getId());
    tokenPairRedisRepository.save(tokenPair);

    return TokenPairMapper.INSTANCE.toResponseDto(tokenPair);
  }

  private User validateSignInUser(UserRequest request) {
    //아이디 검증
    User user = userRepository.findByAccountId(request.getAccountId()).orElseThrow(
        () -> new InvalidValueException(ErrorCode.INVALID_SIGN_IN_INFO)
    );

    //비밀번호 검증
    String requestedRawPw = request.getRawPassword();
    String savedEncodedPw = user.getPassword();
    boolean isValidPw = passwordEncoder.matches(requestedRawPw, savedEncodedPw);
    if (!isValidPw) throw new InvalidValueException(ErrorCode.INVALID_SIGN_IN_INFO);
    return user;
  }
}
