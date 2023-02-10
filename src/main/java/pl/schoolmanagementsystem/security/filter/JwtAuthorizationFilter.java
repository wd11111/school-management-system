package pl.schoolmanagementsystem.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String CLAIM = "roles";
    public static final String REPLACEMENT = "";

    private String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.secret=secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authorization = getAuthorization(request);
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authorization);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthorization(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, REPLACEMENT));

            String username = jwt.getSubject();
            Collection<SimpleGrantedAuthority> authorities = getAuthorities(jwt);

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(DecodedJWT jwt) {
        String[] roles = jwt.getClaim(CLAIM).asArray(String.class);
        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}