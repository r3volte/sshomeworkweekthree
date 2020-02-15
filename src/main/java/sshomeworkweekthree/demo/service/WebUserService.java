package sshomeworkweekthree.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import sshomeworkweekthree.demo.exception.UserExistException;
import sshomeworkweekthree.demo.model.Role;
import sshomeworkweekthree.demo.model.WebUser;
import sshomeworkweekthree.demo.repository.WebUserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Stream;

import static sshomeworkweekthree.demo.utils.MagicWordsUtils.*;


@Service
public class WebUserService implements UserDetailsService {

    private final WebUserRepository webUserRepository;
    private final VerificationTokenService verificationTokenService;
    private final MailSenderService mailSenderService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    private PasswordEncoder getEncodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public WebUserService(WebUserRepository webUserRepository,
                          VerificationTokenService verificationTokenService,
                          MailSenderService mailSenderService) {
        this.verificationTokenService = verificationTokenService;
        this.webUserRepository = webUserRepository;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<WebUser> user = findWebUserByUsername(username);
        return user.orElseGet(() -> {
            new ModelAndView(REDIRECT_ERROR.getValue());
            return new WebUser();
        });
    }

    public Optional<WebUser> findWebUserByUsername(String username) {
        return webUserRepository.findWebUserByUsername(username);
    }

    public void addUser(WebUser user, HttpServletRequest request) {
        if (user.getPassword() != null) {
            user.setPassword(getEncodePassword()
                    .encode(user.getPassword()));
            webUserRepository.save(user);
            String buildURL = tokenInitializer(user, request);
            mailSenderService.sendSimpleMessage(user.getUsername(), VERIFICATION_TOKEN_SUBJECT.getValue(), buildURL);
            adminVerify(user, request);
        } else {
            new ModelAndView(REDIRECT_REGISTER_ERROR.getValue());
        }
    }

    public String checkUserExist(WebUser user, HttpServletRequest request, Optional<WebUser> webUser) {
        if (webUser.isPresent()) {
            try {
                throw new UserExistException();
            } catch (UserExistException e) {
                logger.error(e.getMessage());
            }
            return REDIRECT_REGISTER_ERROR.getValue();
        } else {
            addUser(user, request);
            return REDIRECT_LOGIN.getValue();
        }
    }

    private String tokenInitializer(WebUser user, HttpServletRequest request) {
        String token = verificationTokenService.initToken();
        verificationTokenService.addToken(user, token);
        return buildURL(request, token);
    }

    private void adminVerify(WebUser user, HttpServletRequest request) {
        Stream.of(user)
                .filter(role -> role.getRole().equals(Role.ROLE_ADMIN))
                .forEach(some -> {
                    user.setRole(Role.ROLE_USER);
                    String buildURL = tokenInitializer(user, request);
                    mailSenderService.sendSimpleMessage(getRootWebUser().getUsername(),
                            VERIFICATION_TOKEN_SUBJECT.getValue(),
                            buildURL);
                });
    }

    private String buildURL(HttpServletRequest request, String token) {
        return HTTP_STRING.getValue() + request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath() +
                HTTP_TOKEN.getValue() + token;
    }

    public void verifyToken(String token) {
        WebUser appUser = verificationTokenService.findByValue(token).getWebUser();
        appUser.setEnabled(true);
        webUserRepository.save(appUser);
        verificationTokenService.deleteTokenAfterVerify(token);
    }

    public void grantAdminAccess(String token) {
        WebUser appUser = verificationTokenService.findByValue(token).getWebUser();
        appUser.setRole(Role.ROLE_ADMIN);
        webUserRepository.save(appUser);
        verificationTokenService.deleteTokenAfterVerify(token);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        WebUser webUser = getRootWebUser();
        webUserRepository.save(webUser);
    }

    public WebUser getRootWebUser() {
        WebUser webUser = new WebUser();
        webUser.setRole(Role.ROLE_ADMIN);
        webUser.setUsername(ROOT_USER_LOGIN.getValue());
        webUser.setPassword(getEncodePassword().encode(ROOT_USER_PASSWORD.getValue()));
        webUser.setEnabled(true);
        return webUser;
    }
}
