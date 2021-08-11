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
        String requestUrl = request.getRequestURI();
        if ("OPTIONS".equals(request.getMethod()) || (!UrlParser.isAuthenticationUrl(requestUrl) && "GET".equals(request.getMethod()))) {
            return true;
        }
        String clientIp = request.getRemoteAddr();
        administratorService.validateIp(clientIp);
        return true;
    }
}