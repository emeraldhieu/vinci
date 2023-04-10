package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.shipping.grpc.ShippingDetailResponse;
import com.emeraldhieu.vinci.shipping.grpc.ShippingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ShippingDetailController {

    private final ShippingDetailService shippingDetailService;
    private final ShippingClientService shippingClientService;

    @QueryMapping
    public List<ShippingDetailResponse> shippingDetails(@Argument int offset, @Argument int limit,
                                                           @Argument List<String> sortOrders) {
        Page<ShippingDetailResponse> shippingDetailResponsePage =
            shippingDetailService.getShippingDetails(offset, limit, sortOrders);
        List<ShippingDetailResponse> responses = shippingDetailResponsePage.stream()
            .collect(Collectors.toList());
        return responses;
    }

    @SchemaMapping
    public ShippingResponse shipping(ShippingDetailResponse shippingDetailResponse) {
        return shippingClientService.getShipping(shippingDetailResponse.getShippingId());
    }
}
