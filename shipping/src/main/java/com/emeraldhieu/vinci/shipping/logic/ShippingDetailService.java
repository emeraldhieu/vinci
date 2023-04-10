package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.shipping.grpc.ShippingDetailResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShippingDetailService {
    Page<ShippingDetailResponse> getShippingDetails(int offset, int limit, List<String> sortOrders);
}
