package wanted.preonboarding.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static wanted.preonboarding.assignment.dto.UserDto.UserResponse;

public class PostDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PostRequest {
    @Schema(description = "게시글 제목")
    @Length(min = 1, max = 255)
    private String title;
    @Schema(description = "게시글 본문")
    @NotBlank
    private String body;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PostResponse {
    @Schema(description = "게시글 PK")
    private long id;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 본문")
    private String body;
    @Schema(description = "게시글 조회수")
    private int views;
    @Schema(description = "게시글 작성자")
    private UserResponse author;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PostSimpleResponse {
    @Schema(description = "게시글 PK")
    private long id;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 조회수")
    private int views;
    @Schema(description = "게시글 작성자")
    private UserResponse author;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaginationRequest {
    @Schema(description = "생략할 기준 게시글 ID (-1 이상, -1인 경우 가장 최근 게시글부터 조회)")
    @Min(-1)
    private long cursorId;
    @Schema(description = "한 페이지에 포함시킬 게시글 개수 (1~100)")
    @Min(1) @Max(100)
    private int pageSize;
  }
}
