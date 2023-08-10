/**
 * packageName    : wanted.preonboarding.assignment.exception
 * fileName       : InvalidValueException
 * author         : 우태균
 * description    : 비즈니스 로직 상, 올바르지 않은 값이 들어오는 경우에 대한 예외
 */
package wanted.preonboarding.assignment.exception;

public class InvalidValueException extends BusinessException {

  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
