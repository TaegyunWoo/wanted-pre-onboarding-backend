/**
 * packageName    : wanted.preonboarding.assignment.service
 * fileName       : UserService
 * author         : 우태균
 * date           : 2023/08/06
 * description    : 사용자 Service
 */
package wanted.preonboarding.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.mapper.UserMapper;
import wanted.preonboarding.assignment.repository.UserRepository;

import java.util.Optional;

import static wanted.preonboarding.assignment.dto.UserDto.UserRequest;

@RequiredArgsConstructor
@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

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
}
