package com.emeraldhieu.vinci.shipping.logic;

import com.emeraldhieu.vinci.shipping.grpc.ShippingDetailResponse;
import com.emeraldhieu.vinci.shipping.logic.mapping.ShippingDetailRequestMapper;
import com.emeraldhieu.vinci.shipping.logic.mapping.ShippingDetailResponseMapper;
import com.emeraldhieu.vinci.shipping.logic.sort.SortOrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultShippingDetailService implements ShippingDetailService {

    private final ShippingDetailRepository shippingDetailRepository;
    private final ShippingDetailRequestMapper shippingDetailRequestMapper;
    private final ShippingDetailResponseMapper shippingDetailResponseMapper;
    private final SortOrderValidator sortOrderValidator;

    @Override
    public Page<ShippingDetailResponse> getShippingDetails(int offset, int limit, List<String> sortOrders) {
        sortOrderValidator.validate(sortOrders);

        List<Sort.Order> theSortOrders = sortOrders.stream()
            .map(sortOrder -> {
                String[] tokens = sortOrder.split(",");
                String propertyName = tokens[0];
                String direction = tokens[1];
                return Sort.Order.by(propertyName)
                    .with(Sort.Direction.fromString(direction));
            })
            .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(theSortOrders));
        Page<ShippingDetail> page = shippingDetailRepository.getShippings(pageable);
        Page<ShippingDetailResponse> response = page.map(shippingDetailResponseMapper::toDto);
        return response;
    }
}
