package org.churunfa.security.autoconfigure;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
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