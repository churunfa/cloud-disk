package org.churunfa.security.autoConfigaration;

import org.churunfa.security.grant.auth.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("org.churunfa.security.grant.auth.error")
public class LoginConfig implements WebMvcConfigurer {

    SecurityServiceFactoryBean securityServiceFactoryBean;

    @Autowired
    public void setSecurityServiceFactoryBean(SecurityServiceFactoryBean securityServiceFactoryBean) {
        this.securityServiceFactoryBean = securityServiceFactoryBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        loginInterceptor.setSecurityServiceFactoryBean(securityServiceFactoryBean);
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor);
        registration.addPathPatterns("/**");
    }
}
