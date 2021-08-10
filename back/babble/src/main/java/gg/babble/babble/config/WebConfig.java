package gg.babble.babble.config;

import gg.babble.babble.service.auth.AdministratorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdministratorService administratorService;

    public WebConfig(final AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAccessInterceptor(administratorService))
            .addPathPatterns("/api/games/**", "/api/tags/**", "/api/sliders/**", "/api/admins/**");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedOriginPatterns("*");
    }
}