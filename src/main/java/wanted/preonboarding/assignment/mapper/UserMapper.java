/**
 * packageName    : wanted.preonboarding.assignment.mapper
 * fileName       : UserMapper
 * author         : 우태균
 * date           : 2023/08/06
 * description    : 사용자 엔티티·DTO 객체 간 매핑 로직 자동화
 */
package wanted.preonboarding.assignment.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.dto.UserDto;

@Mapper(builder = @Builder(disableBuilder = true))
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "dto.accountId", target = "accountId")
  @Mapping(source = "encodedPassword", target = "password")
  User toEntity(UserDto.UserRequest dto, String encodedPassword);
}
