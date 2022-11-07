package pl.schoolmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.secret:secretkey123}")
    private String secretKey;
    private final UserService userService;
    private final SuccessHandler successHandler;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final FailureHandler failureHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

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
                .antMatchers("/register").permitAll()
                .antMatchers("/students/**").permitAll()
                .antMatchers("/classes/**").permitAll()
                .antMatchers("/teachers/**").permitAll()
                .antMatchers("/subjects/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilter(filter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userService, secretKey));
    }

    public AuthenticationFilter filter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return authenticationFilter;
    }
}
