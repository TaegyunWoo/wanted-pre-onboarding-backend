/**
 * packageName    : wanted.preonboarding.assignment.mapper
 * fileName       : UserMapper
 * author         : 우태균
 * description    : 사용자 엔티티·DTO 객체 간 매핑 로직 자동화
 */
package wanted.preonboarding.assignment.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.dto.UserDto;

@Mapper(builder = @Builder(disableBuilder = true))
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "dto.accountId", target = "accountId")
  @Mapping(source = "encodedPassword", target = "password")
  User toEntity(UserDto.UserRequest dto, String encodedPassword);

  @Named("toUserResponseDtoByUser")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "accountId", target = "accountId")
  UserDto.UserResponse toResponseDto(User entity);
}
