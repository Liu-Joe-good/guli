package com.atguigu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liuzixuan
 * @ClassName ApiGatewayApplication
 * @Date 20211101
 */
@SpringBootApplication
@EnableDiscoveryClient // 被注册中心发现（发现不加也可）
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }
}
