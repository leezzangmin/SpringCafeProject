package com.zzangmin.gesipan.component.login.argumentresolver;


import com.zzangmin.gesipan.component.login.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OptionalUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasOptionalAuthAnnotation = parameter.hasParameterAnnotation(OptionalAuth.class);
        boolean isOptionalType = Optional.class.isAssignableFrom(parameter.getParameterType());

        return hasOptionalAuthAnnotation && isOptionalType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Optional<String> jwt = jwtProvider.resolveToken(httpServletRequest);
        if (jwt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(jwtProvider.getUserIdFromToken(jwt.get()));
    }
}
