package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.userService.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public void setUserService(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll() //при запросе доступ имеют все (permitAll)
                .antMatchers("/api/**")
                .authenticated()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .and()
                .formLogin()
                .successHandler(successUserHandler)
                .loginProcessingUrl("/hellologin")
                .and()
                .logout().logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // аутентификация inMemory
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        return authenticationProvider;
    }
}
