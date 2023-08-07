/**
 * packageName    : wanted.preonboarding.assignment.controller.advice
 * fileName       : GlobalControllerAdvice
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 공통 오류응답 처리 ControllerAdvice
 */
package wanted.preonboarding.assignment.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wanted.preonboarding.assignment.dto.ErrorResponseDto;
import wanted.preonboarding.assignment.exception.BusinessException;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

  /**
   * Spring Validation의 애너테이션 @Valid에 의해 발생하는 필드 바인딩 오류 처리
   * @param e @Valid에 의해 발생한 예외
   * @return 오류 응답
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);
    ErrorResponseDto dto = ErrorResponseDto.of(e.getBindingResult());
    return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
  }

  /**
   * 비즈니스 로직 상에서 발생한 모든 예외를 처리
   * @param e 비즈니스 로직 상에서 발생한 예외
   * @return 오류 응답
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
    log.error("handleBusinessException", e);
    ErrorResponseDto dto = new ErrorResponseDto(e.getErrorCode());
    return new ResponseEntity<>(dto, HttpStatus.valueOf(e.getErrorCode().getStatus()));
  }


}
