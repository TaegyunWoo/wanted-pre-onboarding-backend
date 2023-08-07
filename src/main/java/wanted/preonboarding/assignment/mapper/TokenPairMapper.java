/**
 * packageName    : wanted.preonboarding.assignment.mapper
 * fileName       : TokenPairMapper
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 토큰쌍 Entity·DTO 객체 간 매핑 로직 자동화
 */
package wanted.preonboarding.assignment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import wanted.preonboarding.assignment.domain.TokenPair;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;

@Mapper
public interface TokenPairMapper {
  TokenPairMapper INSTANCE = Mappers.getMapper(TokenPairMapper.class);

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  TokenResponse toDto(TokenPair entity);

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "userPk", target = "userPk")
  TokenPair toEntity(String accessToken, String refreshToken, long userPk);
}
