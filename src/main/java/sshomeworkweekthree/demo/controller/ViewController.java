package sshomeworkweekthree.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sshomeworkweekthree.demo.model.WebUser;
import sshomeworkweekthree.demo.service.WebUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static sshomeworkweekthree.demo.utils.MagicWordsUtils.*;


@Controller
public class ViewController {

    private WebUserService webUserService;

    @Autowired
    public ViewController(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return LOGIN.getValue();
    }

    @RequestMapping(value = "/login-error")
    public String loginError() {
        return LOGIN_ERROR.getValue();
    }

    @RequestMapping(value = "/register-error")
    public String registerError() {
        return REGISTER_ERROR.getValue();
    }

    @RequestMapping("/register")
    public ModelAndView signUp() {
        return new ModelAndView(REGISTRATION.getValue(), USER_FROM.getValue(), new WebUser());
    }

    @RequestMapping("/register-success")
    public String register(WebUser user, HttpServletRequest request) {
        Optional<WebUser> webUser = webUserService.findWebUserByUsername(user.getUsername());
        return webUserService.checkUserExist(user, request, webUser);
    }

    @RequestMapping("/verify-token")
    public ModelAndView register(@RequestParam String token) {
        webUserService.verifyToken(token);
        return new ModelAndView(REDIRECT_LOGIN.getValue());
    }

    @RequestMapping("/verify-admin-token")
    public ModelAndView adminAccessVerify(@RequestParam String token) {
        webUserService.grantAdminAccess(token);
        return new ModelAndView(REDIRECT_LOGIN.getValue());
    }

}
