package pl.schoolmanagementsystem.exception.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public static final int RESPONSE_STATUS = 401;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                                        throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);
        response.setStatus(RESPONSE_STATUS);
    }
}
