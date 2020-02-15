package sshomeworkweekthree.demo.utils;

public enum MagicWordsUtils {

    ROOT_USER_LOGIN(""),
    ROOT_USER_PASSWORD(""),
    REDIRECT_ERROR("redirect:/login-error"),
    REDIRECT_LOGIN("redirect:/login"),
    REDIRECT_REGISTER_ERROR("redirect:/register-error"),
    REDIRECT_ADMIN_PAGE("redirect:/helloAdmin"),
    REDIRECT_USER_PAGE("redirect:/helloUser"),
    LOGIN("login"),
    LOGIN_ERROR("login-error"),
    REGISTER_ERROR("register-error"),
    REGISTRATION("registration"),
    USER_FROM("user"),
    VERIFICATION_TOKEN_SUBJECT("Verification Token"),
    HTTP_TOKEN("/verify-token?token="),
    HTTP_STRING("http://"),
    BYE("Pa pa"),
    HI_USER("Czesc Admin: "),
    USER_EXIST_EXCEPTION_MSG("User already exist.");

    final String value;

    MagicWordsUtils(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
