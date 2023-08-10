/**
 * packageName    : wanted.preonboarding.assignment.controller
 * fileName       : PostApi
 * author         : 우태균
 * description    : Post 관련 API 인터페이스.
 *                  모든 애너테이션을 인터페이스에 모아둬, 실제 컨트롤러 가독성 개선
 */
package wanted.preonboarding.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.assignment.dto.LoginUser;
import wanted.preonboarding.assignment.dto.PostDto;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "게시글 API")
@RequestMapping("/posts")
public interface PostApi {
  @Operation(summary = "새 게시글 생성")
  @ApiResponse(responseCode = "200", description = "게시글 생성 성공")
  @PostMapping
  void postNewPost(
      @RequestBody @Valid PostDto.PostRequest postRequest,
      @Parameter(hidden = true) LoginUser loginUser
      );

  @Operation(summary = "게시글 목록 조회")
  @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
  @GetMapping
  List<PostDto.PostSimpleResponse> getPostsList(
      @ModelAttribute @Valid PostDto.PaginationRequest paginationRequest
  );

  @Operation(summary = "특정 게시글 조회")
  @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
  @GetMapping("/{postId}")
  PostDto.PostResponse getPost(
      @Parameter(description = "조회할 게시글 ID") @PathVariable long postId
  );

  @Operation(summary = "특정 게시글 수정")
  @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
  @PatchMapping("/{postId}")
  void patchPost(
      @Parameter(description = "수정할 게시글 ID") @PathVariable long postId,
      @Parameter(description = "수정할 내용") @RequestBody PostDto.PostRequest postRequest,
      @Parameter(hidden = true) LoginUser loginUser
  );

  @Operation(summary = "특정 게시글 삭제")
  @ApiResponse(responseCode = "200", description = "게시글 삭제 성공")
  @DeleteMapping("/{postId}")
  void deletePost(
      @Parameter(description = "삭제할 게시글 ID") @PathVariable long postId,
      @Parameter(hidden = true) LoginUser loginUser
  );
}
