package com.zzangmin.gesipan.config.interceptor;

import com.zzangmin.gesipan.web.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final LoginCheckInterceptor loginCheckInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/post/{postId}", "/posts", "/oauth/github_url")
                .order(1);
    }
}
