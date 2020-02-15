package sshomeworkweekthree.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class UserExistAdvice {

    @ResponseBody
    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String userExistHandler(UserExistException ex) {
        return ex.getMessage();
    }
}
