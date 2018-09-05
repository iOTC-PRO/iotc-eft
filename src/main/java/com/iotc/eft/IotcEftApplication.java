package com.iotc.eft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IotcEftApplication {

    public static void main(String[] args) {

        SpringApplication.run(IotcEftApplication.class, args);
    }
}
