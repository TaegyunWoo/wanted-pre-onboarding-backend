/**
 * packageName    : wanted.preonboarding.assignment.exception
 * fileName       : ErrorCode
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 오류 응답과 관련된 상세 오류 코드 Enum, 모든 오류 코드를 여기서 관
 */
package wanted.preonboarding.assignment.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {
  //공통
  INVALID_INPUT_VALUE(400, "C001", "Input value is rejected")

  ;


  private int status;
  private String code;
  private String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
