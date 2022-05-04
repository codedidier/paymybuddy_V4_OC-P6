package com.codedidier.paymybuddy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration for Spring security. ["/", "/showLoginPage",
 * "/registration/**"] == permitAll any other request must be authenticated.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .antMatchers("/registration/**")
                .permitAll()
                .antMatchers("/showLoginPage")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/showLoginPage")
                .permitAll()
                .loginProcessingUrl("/authenticateTheUser")
                .defaultSuccessUrl("/home")
                .and()
                .httpBasic();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        // BCrypt permet de hasher le mdp. la valeur de hash par default est 10.
        return new BCryptPasswordEncoder();
    }
}
