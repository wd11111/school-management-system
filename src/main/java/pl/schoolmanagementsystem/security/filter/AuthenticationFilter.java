package pl.schoolmanagementsystem.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.schoolmanagementsystem.security.dto.LoginCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            LoginCredentials authRequest = objectMapper.readValue(sb.toString(), LoginCredentials.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.email(), authRequest.password());

            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            response.setStatus(UNAUTHORIZED.value());
            return null;
        }
    }
}
