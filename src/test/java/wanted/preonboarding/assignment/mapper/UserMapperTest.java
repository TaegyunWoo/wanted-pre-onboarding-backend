package wanted.preonboarding.assignment.mapper;

import org.junit.jupiter.api.Test;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static wanted.preonboarding.assignment.dto.UserDto.*;

class UserMapperTest {
  @Test
  void 요청DTO와_암호화PW로_엔티티_만들기() {
    //GIVEN
    String accountId = "myfoo@gmail.com";
    String rawPw = "myRawPassword";
    UserRequest requestDto = UserRequest.builder()
        .accountId(accountId)
        .rawPassword(rawPw)
        .build();
    String encodedPw = "myEncodedPassword";

    //WHEN
    User result = UserMapper.INSTANCE.toEntity(requestDto, encodedPw);

    //THEN
    assertNull(result.getId());
    assertEquals(accountId, result.getAccountId());
    assertEquals(encodedPw, result.getPassword());
    assertSame(0, result.getPostList().size());
    assertNull(result.getTokenPair());
  }

  @Test
  void 엔티티로_ResponseDTO_만들기() {
    //GIVEN
    long userId = 1;
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPw)
        .build();

    //WHEN
    UserResponse result = UserMapper.INSTANCE.toResponseDto(userEntity);

    //THEN
    assertSame(userId, result.getId());
    assertEquals(accountId, result.getAccountId());
  }
}