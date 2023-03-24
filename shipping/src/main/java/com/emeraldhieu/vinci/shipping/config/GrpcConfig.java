package com.emeraldhieu.vinci.shipping.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * A workaround to make gRPC works with Spring Boot 3.0
 * See https://github.com/yidongnan/grpc-spring-boot-starter/pull/775#issuecomment-1329023335
 * TODO Remove this class when upgrading to grpc-spring-boot 2.15
 */
@Configuration
@ImportAutoConfiguration({
    net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientMetricAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientSecurityAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientTraceAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcDiscoveryClientAutoConfiguration.class,

    net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration.class,
    net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration.class,

    net.devh.boot.grpc.server.autoconfigure.GrpcAdviceAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcMetadataConsulConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcMetadataEurekaConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcMetadataNacosConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcMetadataZookeeperConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcReflectionServiceAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcServerMetricAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration.class,
    net.devh.boot.grpc.server.autoconfigure.GrpcServerTraceAutoConfiguration.class
})
class GrpcConfig {
}