package org.churunfa.security.resolver;

import com.cloud.common.pojo.AuthorizationUser;
import com.cloud.common.pojo.User;
import org.churunfa.security.autoconfigure.SecurityService;
import org.churunfa.security.autoconfigure.SecurityServiceFactoryBean;
import org.churunfa.security.grant.auth.Authorization;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationUserArgumentResolver implements HandlerMethodArgumentResolver {

    SecurityServiceFactoryBean securityServiceFactoryBean;

    public void setSecurityServiceFactoryBean(SecurityServiceFactoryBean securityServiceFactoryBean) {
        this.securityServiceFactoryBean = securityServiceFactoryBean;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasMethodAnnotation(Authorization.class)) return true;
        if (methodParameter.getParameterType().equals(AuthorizationUser.class)) return true;
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        AuthorizationUser authorizationUser = new AuthorizationUser();
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String jwt_token = request.getHeader("Authorization");
        if (jwt_token == null) return authorizationUser;

        SecurityService securityService = securityServiceFactoryBean.getObject();

        authorizationUser.setUser(securityService.getAllInfo(jwt_token));

        return authorizationUser;
    }
}
