package com.teamsolution.common.grpc.config;

import com.teamsolution.common.grpc.config.properties.GrpcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.teamsolution.common.grpc")
@EnableConfigurationProperties(GrpcProperties.class)
public class CommonGrpcAutoConfiguration {}
