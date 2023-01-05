package pl.schoolmanagementsystem.exception.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String PREFIX = "Bearer ";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CLAIM = "roles";
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    @Value("${jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withClaim(CLAIM, new ArrayList<>(principal.getAuthorities()))
                .sign(Algorithm.HMAC256(secret));
        String jsonToken = objectMapper.writeValueAsString(new Token(PREFIX + token));
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(jsonToken);
    }

    @Getter
    @AllArgsConstructor
    public static class Token {
        private String token;
    }
}
