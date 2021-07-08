package org.churunfa.security.autoConfigaration;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecurityServiceFactoryBean implements FactoryBean<SecurityService> {

    DiscoveryClient discoveryClient;
    Contract contract;
    Decoder decoder;
    Encoder encoder;
    List<SecurityService> lists;

    public SecurityServiceFactoryBean(DiscoveryClient discoveryClient, Contract contract, Decoder decoder, Encoder encoder) {
        this.discoveryClient = discoveryClient;
        this.contract = contract;
        this.decoder = decoder;
        this.encoder = encoder;
        lists = new ArrayList<>();
        
        List<ServiceInstance> instances = discoveryClient.getInstances("service-security");

        instances.forEach((instance)->{
            String host = instance.getHost();
            int port = instance.getPort();
            SecurityService target = Feign.builder().contract(contract).encoder(encoder).decoder(decoder).target(SecurityService.class, "http://" + host + ":" +port);
            lists.add(target);
        });
    }

    @Override
    public SecurityService getObject() {
        List<ServiceInstance> instances = discoveryClient.getInstances("service-security");

        instances.forEach((instance)->{
            String host = instance.getHost();
            int port = instance.getPort();
            SecurityService target = Feign.builder().contract(contract).encoder(encoder).decoder(decoder).target(SecurityService.class, "http://" + host + ":" +port);
            lists.add(target);
        });
        int len = lists.size();
        if (len == 0) return null;
        Random random = new Random();
        int i = random.nextInt(len);
        return lists.get(i);
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
