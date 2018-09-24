package com.risi.mvc.data.demo.config;

import com.risi.mvc.data.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity
public class CRMSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override // Don't use it in production.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        RequestMatcher csrfRequestMatcher = new RequestMatcher() {

            private RegexRequestMatcher matcher = new RegexRequestMatcher("^(?!/api/)", null);

            @Override
            public boolean matches(HttpServletRequest request) {
                return matcher.matches(request);
            }
        };

        http
                .csrf().requireCsrfProtectionMatcher(csrfRequestMatcher) // disable csrf for rest api
                .and()
                .authorizeRequests()
                .antMatchers("/").hasAuthority("EMPLOYEE")
                .antMatchers("/customer/**")
                .hasAnyAuthority("MANAGER", "ADMIN")
                //.hasAnyRole("asd") //only Manager and Admin are allowed
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
        .and().exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
