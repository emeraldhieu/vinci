package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.shipping.grpc.ShippingRequest;
import com.emeraldhieu.vinci.shipping.grpc.ShippingResponse;
import com.emeraldhieu.vinci.shipping.grpc.ShippingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * A service for shipping's gRPC client.
 */
@Service
public class ShippingClientService {

    private final ShippingServiceGrpc.ShippingServiceBlockingStub shippingServiceBlockingStub;

    /**
     * Inject gRPC client as a constructor dependency instead of a field dependency.
     * See https://yidongnan.github.io/grpc-spring-boot-starter/en/client/getting-started.html#accessing-the-client
     */
    public ShippingClientService(
        @GrpcClient("shipping") ShippingServiceGrpc.ShippingServiceBlockingStub shippingServiceBlockingStub) {
        this.shippingServiceBlockingStub = shippingServiceBlockingStub;
    }

    public ShippingResponse getShipping(String shippingId) {
        ShippingRequest shippingRequest = ShippingRequest.newBuilder()
            .setId(shippingId)
            .build();
        return shippingServiceBlockingStub.getShipping(shippingRequest);
    }
}
