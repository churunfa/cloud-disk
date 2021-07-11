package org.churunfa.security.autoconfigure;

import org.churunfa.security.resolver.AuthorizationUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


public class AuthorizationUserConfig implements WebMvcConfigurer {

    SecurityServiceFactoryBean securityServiceFactoryBean;

    @Autowired
    public void setSecurityServiceFactoryBean(SecurityServiceFactoryBean securityServiceFactoryBean) {
        this.securityServiceFactoryBean = securityServiceFactoryBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        AuthorizationUserArgumentResolver authorizationUserArgumentResolver = new AuthorizationUserArgumentResolver();
        authorizationUserArgumentResolver.setSecurityServiceFactoryBean(securityServiceFactoryBean);
        resolvers.add(authorizationUserArgumentResolver);
    }
}
