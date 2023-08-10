/**
 * packageName    : wanted.preonboarding.assignment.mapper
 * fileName       : PostMapper
 * author         : 우태균
 * description    : 게시글 Entity·DTO 객체 간 매핑 로직 자동화
 */
package wanted.preonboarding.assignment.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import wanted.preonboarding.assignment.domain.Post;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.dto.PostDto;

@Mapper(uses = {UserMapper.class}, builder = @Builder(disableBuilder = true))
public interface PostMapper {
  PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

  @Mapping(source = "requestDto.title", target = "title")
  @Mapping(source = "requestDto.body", target = "body")
  @Mapping(source = "authorEntity", target = "author")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "views", constant = "0")
  Post toEntity(PostDto.PostRequest requestDto, User authorEntity);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "views", target = "views")
  @Mapping(source = "author", target = "author", qualifiedByName = {"toUserResponseDtoByUser"})
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "modifiedDate", target = "modifiedDate")
  PostDto.PostSimpleResponse toSimpleResponseDto(Post entity);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "body", target = "body")
  @Mapping(source = "views", target = "views")
  @Mapping(source = "author", target = "author", qualifiedByName = {"toUserResponseDtoByUser"})
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "modifiedDate", target = "modifiedDate")
  PostDto.PostResponse toResponseDto(Post entity);

  @InheritConfiguration
  void updateEntity(PostDto.PostRequest requestDto, @MappingTarget Post entity);
}
