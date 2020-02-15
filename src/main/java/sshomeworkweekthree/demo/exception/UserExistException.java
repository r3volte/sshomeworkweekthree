package sshomeworkweekthree.demo.exception;

import org.apache.tomcat.websocket.AuthenticationException;

import static sshomeworkweekthree.demo.utils.MagicWordsUtils.USER_EXIST_EXCEPTION_MSG;

public class UserExistException extends AuthenticationException {

    public UserExistException() {
        super(USER_EXIST_EXCEPTION_MSG.getValue());
    }
}
