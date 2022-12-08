package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.exception.NotFoundException;
import com.emeraldhieu.vinci.order.logic.sort.SortOrderValidator;
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
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final SortOrderValidator sortOrderValidator;

    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        Order orderToSave = orderRequestMapper.toEntity(orderRequest);
        Order savedOrder = orderRepository.save(orderToSave);
        return orderResponseMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponse update(Long id, OrderRequest orderRequest) {
        Order orderToUpdate = orderRepository
            .findById(id)
            .map(currentOrder -> {
                orderRequestMapper.partialUpdate(currentOrder, orderRequest);
                return currentOrder;
            })
            .orElseThrow(() -> new NotFoundException("Order %s not found".formatted(id)));
        Order updatedOrder = orderRepository.save(orderToUpdate);
        return orderResponseMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> list(int offset, int limit, List<String> sortOrders) {
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
        return orderRepository.findAll(pageable)
            .map(orderResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return orderRepository.findById(id)
            .map(orderResponseMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Order %s not found".formatted(id)));
    }

    @Override
    public void delete(Long id) {
        orderRepository.findById(id)
            .ifPresent(order -> orderRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        //return orderRepository.search(query, pageable).map(orderMapper::toDto);
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
