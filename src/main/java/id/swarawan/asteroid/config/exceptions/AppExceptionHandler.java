package id.swarawan.asteroid.config.exceptions;

import id.swarawan.asteroid.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse> responseStatusException(ResponseStatusException exception) {
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, exception.getStatusCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse> badRequestException(BadRequestException exception) {
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
