package ch.diedreifragezeichen.exama;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //list_users is protected and needs an authorisationRequest that is provided from Login
        http
            .authorizeRequests()
                //.antMatchers("/", "/css/**", "/images/**", "/js/**")
                //.permitAll()
                .anyRequest()
                .permitAll()
                //.authenticated()
            
            .and()
            
            .formLogin()
                //parameter entity for login
                //.loginPage("/login.html")
                //.loginProcessingUrl("/login")
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                //.defaultSuccessUrl("/users")
            
            .and()

            .logout()
                .logoutSuccessUrl("/login")
                .permitAll()
            ;
    }
    
}
