/**
 * packageName    : wanted.preonboarding.assignment.dto
 * fileName       : ErrorResponseDto
 * author         : 우태균
 * description    : 공통 오류 응답 DTO
 */
package wanted.preonboarding.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import wanted.preonboarding.assignment.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto {
  @Schema(description = "발생한 에러의 메시지")
  private String message;
  @Schema(description = "서비스 오류코드")
  private String code;
  @Schema(description = "관련된 input 필드")
  private List<FieldError> fieldErrors;

  public static ErrorResponseDto of(BindingResult bindingResult) {
    final ErrorResponseDto dto = new ErrorResponseDto(ErrorCode.INVALID_INPUT_VALUE);
    bindingResult.getFieldErrors().stream()
        .map(f -> new FieldError(f))
        .forEach(dto.fieldErrors::add);
    return dto;
  }

  public static ErrorResponseDto of(MethodArgumentTypeMismatchException e) {
    ErrorResponseDto dto = new ErrorResponseDto(ErrorCode.INVALID_INPUT_TYPE);
    String parameterName = e.getParameter().getParameterName();
    String parameterType = e.getParameter().getParameterType().getName();
    FieldError fieldError = new FieldError(parameterName, "fieldType=" + parameterType + ", yourValue=" + e.getValue());
    dto.getFieldErrors().add(fieldError);
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
    @Schema(description = "오류가 발생한 input 필드명")
    private String field;
    @Schema(description = "오류 발생 이유")
    private String reason;

    public FieldError(org.springframework.validation.FieldError info) {
      this.field = info.getField();
      this.reason = info.getDefaultMessage();
    }
  }

}
