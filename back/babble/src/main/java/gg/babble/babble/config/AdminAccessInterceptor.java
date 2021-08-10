package gg.babble.babble.config;

import gg.babble.babble.service.auth.AdminAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAccessInterceptor implements HandlerInterceptor {

    private final AdminAuthService adminAuthService;

    public AdminAccessInterceptor(final AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if ("OPTIONS".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
            return true;
        }
        String clientIp = request.getRemoteAddr();
        adminAuthService.validateIp(clientIp);
        return true;
    }
}