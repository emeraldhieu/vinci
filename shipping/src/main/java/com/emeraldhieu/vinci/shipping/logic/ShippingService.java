package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.shipping.grpc.ShippingRequest;
import com.emeraldhieu.vinci.shipping.grpc.ShippingResponse;
import com.emeraldhieu.vinci.shipping.grpc.ShippingServiceGrpc;
import com.emeraldhieu.vinci.shipping.logic.exception.ShippingNotFoundException;
import com.emeraldhieu.vinci.shipping.logic.mapping.ShippingRequestMapper;
import com.emeraldhieu.vinci.shipping.logic.mapping.ShippingResponseMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

@Service
@GrpcService
@RequiredArgsConstructor
public class ShippingService extends ShippingServiceGrpc.ShippingServiceImplBase {

    private final ShippingRepository shippingRepository;
    private final ShippingRequestMapper shippingRequestMapper;
    private final ShippingResponseMapper shippingResponseMapper;

    @Override
    public void getShipping(ShippingRequest request, StreamObserver<ShippingResponse> responseObserver) {
        // TODO Validate request or modify gRPC #getShipping to takes only "id".
        // ...

        ShippingResponse shippingResponse = shippingRepository.findByExternalId(request.getId())
            .map(shippingResponseMapper::toDto)
            .orElseThrow(() -> new ShippingNotFoundException(request.getId()));
        responseObserver.onNext(shippingResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void saveShipping(ShippingRequest request, StreamObserver<ShippingResponse> responseObserver) {
        Shipping shippingToSave = shippingRequestMapper.toEntity(request);
        Shipping savedShipping = shippingRepository.save(shippingToSave);
        ShippingResponse shippingResponse = shippingResponseMapper.toDto(savedShipping);
        responseObserver.onNext(shippingResponse);
        responseObserver.onCompleted();
    }
}
