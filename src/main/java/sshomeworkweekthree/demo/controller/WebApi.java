package sshomeworkweekthree.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static sshomeworkweekthree.demo.model.Role.ROLE_ADMIN;
import static sshomeworkweekthree.demo.utils.MagicWordsUtils.*;

@RestController
public class WebApi {


    @GetMapping("/helloAdmin")
    public String helloAdmin(Principal principal) {
        return "Czesc Admin: " + principal.getName();
    }

    @GetMapping("/helloUser")
    public String helloUser(Principal principal) {
        return HI_USER.getValue() + principal.getName();
    }

    @GetMapping("/bye")
    public String bye() {
        return BYE.getValue();
    }

    @RequestMapping("/default")
    public ModelAndView defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole(ROLE_ADMIN.getAuthority())) {
            return new ModelAndView(REDIRECT_ADMIN_PAGE.getValue());
        }
        return new ModelAndView(REDIRECT_USER_PAGE.getValue());
    }
}
