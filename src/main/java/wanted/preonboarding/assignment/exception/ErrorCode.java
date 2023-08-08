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
  INVALID_INPUT_VALUE(400, "C001", "Input value is rejected"),

  //인증
  NO_AUTH_HEADER(401, "A001", "Authorization header is empty"),
  INVALID_AUTH_VALUE_FORMAT(401, "A002", "Invalid format of authorization header's value"),
  EXPIRED_TOKEN(401, "A003", "Access token is expired"),
  SIGNATURE_FAIL(401, "A004", "Token's signature is invalid"),
  NOT_ISSUED_TOKEN(401, "A005", "Token is not issued by server"),
  BAD_TOKEN(401, "A006", "Token is bad"),

  //사용자
  DUPLICATED_ACCOUNT_ID(400, "U001", "That account ID is already exists"),
  INVALID_SIGN_IN_INFO(401, "U002", "Requested sign-in information is not valid")
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
