/**
 * packageName    : wanted.preonboarding.assignment.dto
 * fileName       : ErrorResponseDto
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 공통 오류 응답 DTO
 */
package wanted.preonboarding.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import wanted.preonboarding.assignment.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto {
  private String message;
  private String code;
  private List<FieldError> fieldErrors;

  public static ErrorResponseDto of(BindingResult bindingResult) {
    final ErrorResponseDto dto = new ErrorResponseDto(ErrorCode.INVALID_INPUT_VALUE);
    bindingResult.getFieldErrors().stream()
        .map(f -> new FieldError(f))
        .forEach(dto.fieldErrors::add);
    return dto;
  }

  public ErrorResponseDto(ErrorCode errorCode) {
    this.message = errorCode.getMessage();
    this.code = errorCode.getCode();
    this.fieldErrors = new ArrayList<>();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class FieldError {
    private String field;
    private String reason;

    public FieldError(org.springframework.validation.FieldError info) {
      this.field = info.getField();
      this.reason = info.getDefaultMessage();
    }
  }

}
