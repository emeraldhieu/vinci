package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.logic.event.OrderCreatedEvent;
import com.emeraldhieu.vinci.order.logic.exception.OrderNotFoundException;
import com.emeraldhieu.vinci.order.logic.mapping.OrderRequestMapper;
import com.emeraldhieu.vinci.order.logic.mapping.OrderResponseMapper;
import com.emeraldhieu.vinci.order.logic.sort.SortOrder;
import com.emeraldhieu.vinci.order.logic.sort.SortOrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        Order orderToSave = orderRequestMapper.toEntity(orderRequest);
        Order savedOrder = orderRepository.save(orderToSave);
        sendEvent(savedOrder);
        return orderResponseMapper.toDto(savedOrder);
    }

    public void sendEvent(Order order) {
        log.info("Sending %s...".formatted(OrderCreatedEvent.class.getSimpleName()));
        OrderCreatedEvent event = new OrderCreatedEvent(order.getExternalId());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public OrderResponse update(String id, OrderRequest orderRequest) {
        Order orderToUpdate = orderRepository.findByExternalId(id)
            .map(currentOrder -> {
                orderRequestMapper.partialUpdate(currentOrder, orderRequest);
                return currentOrder;
            })
            .orElseThrow(() -> new OrderNotFoundException(id));
        Order updatedOrder = orderRepository.save(orderToUpdate);
        return orderResponseMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> list(int offset, int limit, List<String> sortOrders) {
        sortOrderValidator.validate(sortOrders);

        List<Sort.Order> theSortOrders = getSortOrders(sortOrders);
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(theSortOrders));
        return orderRepository.findAll(pageable)
            .map(orderResponseMapper::toDto);
    }

    List<Sort.Order> getSortOrders(List<String> sortOrderStrs) {
        return sortOrderStrs.stream()
            .map(sortOrderStr -> {
                SortOrder sortOrder = SortOrder.from(sortOrderStr);
                String propertyName = sortOrder.getPropertyName();
                String direction = sortOrder.getDirection();
                return Sort.Order.by(propertyName)
                    .with(Sort.Direction.fromString(direction));
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(String id) {
        return orderRepository.findByExternalId(id)
            .map(orderResponseMapper::toDto)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void delete(String id) {
        orderRepository.findByExternalId(id)
            .ifPresent(order -> orderRepository.deleteByExternalId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
