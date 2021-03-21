package ch.diedreifragezeichen.exama._config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import ch.diedreifragezeichen.exama.users.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @SuppressWarnings("unused")
    @Autowired
    private DataSource dataSource;

    @Autowired
    UserRepository userRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceExama();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().and()

                .authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**", "/install", "/error").permitAll()
                .and().authorizeRequests().antMatchers("/", "/fragments/**", "/generalTemplates/**").hasAnyAuthority("SYSTEMADMIN", "ADMIN", "TEACHER", "REFERENCESTUDENT", "STUDENT")
                .antMatchers("/systemadminTemplates/**").hasAnyAuthority("SYSTEMADMIN")
                .antMatchers("/adminTemplates/**").hasAnyAuthority("SYSTEMADMIN", "ADMIN")
                .antMatchers("/teacherTemplates/**").hasAnyAuthority("SYSTEMADMIN", "TEACHER")
                .antMatchers("/rstudentTemplates/**").hasAnyAuthority("SYSTEMADMIN", "REFERENCESTUDENT")
                .antMatchers("/studentTemplates/**").hasAnyAuthority("SYSTEMADMIN", "STUDENT").and()

                .authorizeRequests().anyRequest().authenticated().and()

                // .authorizeRequests().antMatchers("/**").permitAll().and()
                // .authorizeRequests().anyRequest().permitAll().and()

                .formLogin().loginPage("/login").usernameParameter("email").permitAll().and()

                .exceptionHandling().accessDeniedPage("/403").and()

                .httpBasic();

        http.logout().logoutSuccessHandler(new LogoutSuccessHandler() {

            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                if (!(authentication instanceof AnonymousAuthenticationToken)) {
                    String currentUserName = authentication.getName();
                    User user = userRepo.findUserByEmail(currentUserName);
                    user.setLoggedIn(false);
                    userRepo.save(user);
                }
                response.sendRedirect("/login");
            }
        });
    }

}