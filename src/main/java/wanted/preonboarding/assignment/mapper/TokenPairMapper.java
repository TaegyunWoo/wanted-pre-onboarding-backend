/**
 * packageName    : wanted.preonboarding.assignment.mapper
 * fileName       : TokenPairMapper
 * author         : 우태균
 * description    : 토큰쌍 Entity·DTO 객체 간 매핑 로직 자동화
 */
package wanted.preonboarding.assignment.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.domain.User;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface TokenPairMapper {
  TokenPairMapper INSTANCE = Mappers.getMapper(TokenPairMapper.class);

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  TokenResponse toResponseDto(TokenPair entity);

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(target = "id", ignore = true)
  TokenPair toEntity(String accessToken, String refreshToken, User user);

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  void updateEntity(String accessToken, String refreshToken, @MappingTarget TokenPair entity);
}
