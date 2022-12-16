package com.zzangmin.gesipan.config;

import com.zzangmin.gesipan.component.basiccrud.argumentresolver.RequestIpArgumentResolver;
import com.zzangmin.gesipan.component.login.argumentresolver.OptionalUserIdArgumentResolver;
import com.zzangmin.gesipan.component.login.argumentresolver.UserIdArgumentResolver;
import com.zzangmin.gesipan.component.login.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final UserIdArgumentResolver userIdArgumentResolver;
    private final OptionalUserIdArgumentResolver optionalUserIdArgumentResolver;
    private final RequestIpArgumentResolver requestIpArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/post/{postId}", "/posts", "/oauth/github_url", "/comments", "/post/search", "")
                .order(1);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
        resolvers.add(optionalUserIdArgumentResolver);
        resolvers.add(requestIpArgumentResolver);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
