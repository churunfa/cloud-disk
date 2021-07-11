package org.churunfa.security.autoconfigure;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;
import java.util.Random;

public class SecurityServiceFactoryBean implements FactoryBean<SecurityService> {

    DiscoveryClient discoveryClient;
    Contract contract;
    Decoder decoder;
    Encoder encoder;

    public SecurityServiceFactoryBean(DiscoveryClient discoveryClient, Contract contract, Decoder decoder, Encoder encoder) {
        this.discoveryClient = discoveryClient;
        this.contract = contract;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public SecurityService getObject() {
        List<ServiceInstance> instances = discoveryClient.getInstances("service-security");
        int len = instances.size();

        Random random = new Random();
        int i = random.nextInt(len);

        ServiceInstance instance = instances.get(i);

        String host = instance.getHost();
        int port = instance.getPort();

        SecurityService target = Feign.builder().contract(contract).encoder(encoder).decoder(decoder).target(SecurityService.class, "http://" + host + ":" +port);
        return target;
    }

    @Override
    public Class<?> getObjectType() {
        return SecurityService.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
