package org.churunfa.security.autoConfigaration;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.lettuce.core.ScriptOutputType;
import org.churunfa.security.grant.auth.error.PermissionDenied;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignClientConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public SecurityServiceFactoryBean securityServiceFactoryBean(Contract contract, Decoder decoder, Encoder encoder) {
        return new SecurityServiceFactoryBean(discoveryClient,contract,decoder,encoder);
    }
}