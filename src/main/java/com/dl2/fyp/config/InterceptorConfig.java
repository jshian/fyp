package com.dl2.fyp.config;

import com.dl2.fyp.aop.PrincipalInterceptor;
import com.dl2.fyp.aop.RedirectInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor()).addPathPatterns("/auth/admin");
        registry.addInterceptor(new PrincipalInterceptor()).excludePathPatterns("/auth/login","/auth/register","/auth/refresh","/auth/admin").order(1);
    }
}
