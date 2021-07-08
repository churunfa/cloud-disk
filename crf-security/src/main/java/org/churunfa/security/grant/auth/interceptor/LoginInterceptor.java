package org.churunfa.security.grant.auth.interceptor;

import com.cloud.common.pojo.User;
import org.churunfa.security.autoConfigaration.SecurityService;
import org.churunfa.security.autoConfigaration.SecurityServiceFactoryBean;
import org.churunfa.security.grant.auth.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    SecurityServiceFactoryBean securityServiceFactoryBean;

    public void setSecurityServiceFactoryBean(SecurityServiceFactoryBean securityServiceFactoryBean) {
        this.securityServiceFactoryBean = securityServiceFactoryBean;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityService securityService = securityServiceFactoryBean.getObject();
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(Login.class) || method.getDeclaringClass().isAnnotationPresent(Login.class)) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            Map map = securityService.verifyJwtToken(token);
            if ((Boolean) map.get("success")) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }else {
                response.sendRedirect("/error/PermissionDenied/");
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}