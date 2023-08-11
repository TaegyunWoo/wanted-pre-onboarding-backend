package wanted.preonboarding.assignment.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.service.TokenService;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

class AuthInterceptorTest {

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void Authorization_헤더가_비어있는_경우() {
    //GIVEN
    String tokenHeader = "Authorization";

    //(Mock)
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getHeader(eq(tokenHeader))).willReturn(null);

    TokenService tokenService = mock(TokenService.class);

    //WHEN
    AuthInterceptor authInterceptor = new AuthInterceptor(tokenService);
    try {
      authInterceptor.preHandle(request, null, null);

      //THEN
      fail("preHandle() must throw InvalidValueException");
    } catch (InvalidValueException e) {
      assertEquals(ErrorCode.NO_AUTH_HEADER, e.getErrorCode());
    } catch (Exception e) {
      fail(); //Must throws InvalidValueException
    }
  }

  @Test
  void Authorization_헤더값이_Bearer로_시작하지_않는_경우() {
    //GIVEN
    String tokenHeader = "Authorization";
    String wrongFormatHeaderValue = "Not Started by Bearer";

    //(Mock)
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getHeader(eq(tokenHeader))).willReturn(wrongFormatHeaderValue);

    TokenService tokenService = mock(TokenService.class);

    //WHEN
    AuthInterceptor authInterceptor = new AuthInterceptor(tokenService);
    try {
      authInterceptor.preHandle(request, null, null);

      //THEN
      fail("preHandle() must throw InvalidValueException");
    } catch (InvalidValueException e) {
      assertEquals(ErrorCode.INVALID_AUTH_VALUE_FORMAT, e.getErrorCode());
    } catch (Exception e) {
      fail(); //Must throws InvalidValueException
    }
  }

  @Test
  void 전달받은_토큰이_유효하지_않은_경우() {
    //GIVEN
    String tokenHeader = "Authorization";
    String rightHeaderValue = "Bearer TOKEN-VALUE...";
    InvalidValueException exception = new InvalidValueException(ErrorCode.EXPIRED_ACCESS_TOKEN);

    //(Mock)
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getHeader(eq(tokenHeader))).willReturn(rightHeaderValue);

    TokenService tokenService = mock(TokenService.class);
    given(
        tokenService.validateToken(
            eq(rightHeaderValue.substring("Bearer ".length()))
        )
    ).willThrow(exception);

    //WHEN
    AuthInterceptor authInterceptor = new AuthInterceptor(tokenService);
    try {
      authInterceptor.preHandle(request, null, null);

      //THEN
      fail("preHandle() must throw InvalidValueException");
    } catch (InvalidValueException e) {
      assertEquals(exception.getErrorCode(), e.getErrorCode());
    } catch (Exception e) {
      fail(); //Must throws InvalidValueException
    }
  }

  @Test
  void 정상처리() {
    //GIVEN
    String tokenHeader = "Authorization";
    String rightHeaderValue = "Bearer TOKEN-VALUE...";
    long userPk = 1;

    //(Mock)
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getHeader(eq(tokenHeader))).willReturn(rightHeaderValue);

    TokenService tokenService = mock(TokenService.class);
    given(
        tokenService.validateToken(
            eq(rightHeaderValue.substring("Bearer ".length()))
        )
    ).willReturn(userPk);

    //WHEN
    AuthInterceptor authInterceptor = new AuthInterceptor(tokenService);
    boolean result = false;
    try {
      result = authInterceptor.preHandle(request, null, null);

      //THEN
    } catch (Exception e) {
      fail(); //Must No Exception
    }
    assertTrue(result);
    then(request).should().setAttribute(AuthInterceptor.USER_PK_ATTRIBUTE_NAME, userPk);
  }
}