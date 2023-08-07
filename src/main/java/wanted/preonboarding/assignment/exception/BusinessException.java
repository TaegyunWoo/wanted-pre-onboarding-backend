/**
 * packageName    : wanted.preonboarding.assignment.exception
 * fileName       : BusinessException
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 비즈니스 로직 상, 발생해야 하는 공통 예외 클래스
 */
package wanted.preonboarding.assignment.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.toString());
    this.errorCode = errorCode;
  }
}
