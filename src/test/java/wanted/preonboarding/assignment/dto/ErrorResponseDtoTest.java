package wanted.preonboarding.assignment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import wanted.preonboarding.assignment.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ErrorResponseDtoTest {

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void BindingResult로_객체생성() {
    //GIVEN
    String errorFieldName = "myErrorField";
    String errorMessage = "This is wrong value";
    FieldError fieldError = new FieldError("errorObject", errorFieldName, "wrong value", true, null, null, errorMessage);
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.add(fieldError);

    //(Mock)
    BindingResult bindingResult = mock(BindingResult.class);
    given(bindingResult.getFieldErrors()).willReturn(fieldErrors);

    //WHEN
    ErrorResponseDto result = ErrorResponseDto.of(bindingResult);

    //THEN
    assertAll( //기본 필드값 확인
        () -> assertEquals(ErrorCode.INVALID_INPUT_VALUE.getMessage(), result.getMessage()),
        () -> assertEquals(ErrorCode.INVALID_INPUT_VALUE.getCode(), result.getCode())
    );
    assertAll( //필드오류 확인
        () -> assertEquals(1, result.getFieldErrors().size()),
        () -> assertEquals(errorFieldName, result.getFieldErrors().get(0).getField()),
        () -> assertEquals(errorMessage, result.getFieldErrors().get(0).getReason())
    );
  }

  @Test
  void MethodArgumentTypeMismatchException으로_객체생성() {
    //GIVEN
    String errorParameterName = "myErrorField";
    Class requiredType = Integer.class;
    String wrongInput = "string type value";

    //(Mock)
    MethodParameter errorParameter = mock(MethodParameter.class);
    given(errorParameter.getParameterName()).willReturn(errorParameterName);
    given(errorParameter.getParameterType()).willReturn(requiredType);

    MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
    given(exception.getParameter()).willReturn(errorParameter);
    given(exception.getValue()).willReturn(wrongInput);

    //WHEN
    ErrorResponseDto result = ErrorResponseDto.of(exception);

    //THEN
    assertAll( //기본 필드값 확인
        () -> assertEquals(ErrorCode.INVALID_INPUT_TYPE.getMessage(), result.getMessage()),
        () -> assertEquals(ErrorCode.INVALID_INPUT_TYPE.getCode(), result.getCode())
    );
    assertAll( //필드(파라미터)오류 확인
        () -> assertEquals(1, result.getFieldErrors().size()),
        () -> assertEquals(errorParameterName, result.getFieldErrors().get(0).getField()),
        () -> assertEquals("fieldType=" + requiredType.getName() + ", yourValue=" + wrongInput, result.getFieldErrors().get(0).getReason())
    );
  }
}