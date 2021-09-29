package gg.babble.babble.config;

import gg.babble.babble.service.auth.AdministratorService;
import gg.babble.babble.util.UrlParser;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAccessInterceptor implements HandlerInterceptor {

    private static final String X_FORWARDED_FOR_HEADER = "x-forwarded-for";
    private final AdministratorService administratorService;

    public AdminAccessInterceptor(final AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (needsAuthentication(request)) {
            String clientIp = getClientIpFrom(request);
            administratorService.validateIp(clientIp);
        }
        return true;
    }

    private String getClientIpFrom(final HttpServletRequest request) {
        String clientIp = request.getHeader(X_FORWARDED_FOR_HEADER);

        if (Objects.isNull(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private boolean needsAuthentication(final HttpServletRequest request) {
        return !"OPTIONS".equals(request.getMethod()) &&
            (UrlParser.isAuthenticationUrl(request.getRequestURI()) || !"GET".equals(request.getMethod()));
    }
}
