package pl.schoolmanagementsystem.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import pl.schoolmanagementsystem.security.filter.AuthenticationFilter;
import pl.schoolmanagementsystem.security.filter.JwtAuthorizationFilter;
import pl.schoolmanagementsystem.security.handler.FailureHandler;
import pl.schoolmanagementsystem.security.handler.SuccessHandler;
import pl.schoolmanagementsystem.security.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final SuccessHandler successHandler;
    private final ObjectMapper objectMapper;
    private final FailureHandler failureHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    String secretKey;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/account/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/teacher/**").hasRole("TEACHER")
                .antMatchers("/student/**").hasRole("STUDENT")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), secretKey));
    }

    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return authenticationFilter;
    }

}
