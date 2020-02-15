package sshomeworkweekthree.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sshomeworkweekthree.demo.service.WebUserService;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private WebUserService webUserService;

    @Autowired
    public WebSecurityConfig(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(webUserService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signUp").permitAll()
                .antMatchers("helloAdmin").hasRole(ADMIN)
                .antMatchers("helloUser").hasAnyRole(ADMIN, USER)
                .and().formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/default")
                .failureUrl("/login-error")
                .and()
                .logout().logoutSuccessUrl("/bye")
                .and()
                .rememberMe();
    }

}
