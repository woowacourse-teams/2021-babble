package gg.babble.babble.config;

import gg.babble.babble.service.auth.AdministratorService;
import gg.babble.babble.util.UrlParser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAccessInterceptor implements HandlerInterceptor {

    private final AdministratorService administratorService;

    public AdminAccessInterceptor(final AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (needsAuthentication(request)) {
            String clientIp = request.getRemoteAddr();
            administratorService.validateIp(clientIp);
        }
        return true;
    }

    private boolean needsAuthentication(final HttpServletRequest request) {
        return !"OPTIONS".equals(request.getMethod()) &&
            (UrlParser.isAuthenticationUrl(request.getRequestURI()) ||
                !"GET".equals(request.getMethod()));
    }
}
