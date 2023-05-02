package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.logic.event.OrderCreatedEvent;
import com.emeraldhieu.vinci.order.logic.mapping.OrderRequestMapper;
import com.emeraldhieu.vinci.order.logic.mapping.OrderResponseMapper;
import com.emeraldhieu.vinci.order.logic.sort.SortOrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultOrderServiceTest {

    private DefaultOrderService defaultOrderService;
    private OrderRepository orderRepository;
    private OrderRequestMapper orderRequestMapper;
    private OrderResponseMapper orderResponseMapper;
    private SortOrderValidator sortOrderValidator;
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderRequestMapper = mock(OrderRequestMapper.class);
        orderResponseMapper = mock(OrderResponseMapper.class);
        sortOrderValidator = mock(SortOrderValidator.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        defaultOrderService = new DefaultOrderService(orderRepository, orderRequestMapper,
            orderResponseMapper, sortOrderValidator, applicationEventPublisher);
    }

    @Test
    void givenOrderRequest_whenCreate_thenPublishEventAndReturnOrderResponse() {
        // GIVEN
        OrderRequest orderRequest = OrderRequest.builder()
            .build();
        Order orderToSave = Order.builder()
            .build();
        String externalId = "amazingExternalId";
        Order savedOrder = Order.builder()
            .externalId(externalId)
            .build();

        when(orderRequestMapper.toEntity(orderRequest)).thenReturn(orderToSave);
        when(orderRepository.save(orderToSave)).thenReturn(savedOrder);

        OrderResponse expectedOrderResponse = OrderResponse.builder()
            .id(externalId)
            .build();
        when(orderResponseMapper.toDto(savedOrder)).thenReturn(expectedOrderResponse);

        // WHEN
        OrderResponse orderResponse = defaultOrderService.create(orderRequest);

        // THEN
        OrderCreatedEvent event = new OrderCreatedEvent(externalId);
        verify(applicationEventPublisher, times(1)).publishEvent(event);
        assertEquals(expectedOrderResponse, orderResponse);
    }

    @Test
    void givenOrderIdAndRequest_whenUpdate_thenReturnOrderResponse() {
        // GIVEN
        String externalId = "amazingExternalId";
        String productName = "pizza";
        String productNameToUpdate = "pizza updated";
        OrderRequest orderRequest = OrderRequest.builder()
            .products(List.of(productNameToUpdate))
            .build();
        Order retrievedOrder = Order.builder()
            .externalId(externalId)
            .products(List.of(productName))
            .build();
        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.of(retrievedOrder));

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Order orderArg = (Order) args[0];
            OrderRequest orderRequestArg = (OrderRequest) args[1];
            orderArg.setProducts(orderRequestArg.getProducts());
            return null;
        }).when(orderRequestMapper).partialUpdate(retrievedOrder, orderRequest);

        /**
         * TODO Find a way to initialize #orderToUpdate based on "orderArg" above
         */
        Order orderToUpdate = Order.builder()
            .externalId(externalId)
            .products(List.of(productNameToUpdate))
            .build();
        Order updatedOrder = Order.builder()
            .externalId(externalId)
            .products(List.of(productNameToUpdate))
            .build();
        when(orderRepository.save(orderToUpdate)).thenReturn(updatedOrder);

        OrderResponse expectedOrderResponse = OrderResponse.builder()
            .id(externalId)
            .products(List.of(productNameToUpdate))
            .build();
        when(orderResponseMapper.toDto(updatedOrder)).thenReturn(expectedOrderResponse);

        // WHEN
        OrderResponse orderResponse = defaultOrderService.update(externalId, orderRequest);

        // THEN
        assertEquals(expectedOrderResponse, orderResponse);
    }

    @Test
    void givenOffsetLimitAndSortOrders_whenList_thenReturnAListOfOrderResponses() {
        // GIVEN
        int offset = 0;
        int limit = 42;
        List<String> sortOrderStrs = List.of(
            "fieldA,asc",
            "fieldB,desc"
        );
        List<Sort.Order> sortOrders = defaultOrderService.getSortOrders(sortOrderStrs);
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(sortOrders));

        String externalId = "amazingExternalId";
        Order order = Order.builder()
            .externalId(externalId)
            .build();
        Page<Order> orders = new PageImpl<>(
            List.of(
                order
            )
        );
        when(orderRepository.findAll(pageable)).thenReturn(orders);

        OrderResponse expectedOrderResponse = OrderResponse.builder()
            .id(externalId)
            .build();
        when(orderResponseMapper.toDto(order)).thenReturn(expectedOrderResponse);

        List<OrderResponse> expectedOrderResponses = List.of(
            expectedOrderResponse
        );

        // WHEN
        Page<OrderResponse> orderResponses = defaultOrderService.list(offset, limit, sortOrderStrs);

        // THEN
        assertEquals(expectedOrderResponses, orderResponses.get().collect(Collectors.toList()));
    }

    @Test
    void givenOrderId_whenGet_thenReturnOrderResponse() {
        // GIVEN
        String externalId = "amazingExternalId";
        Order order = Order.builder()
            .externalId(externalId)
            .build();
        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.of(order));

        OrderResponse expectedOrderResponse = OrderResponse.builder()
            .id(externalId)
            .build();
        when(orderResponseMapper.toDto(order)).thenReturn(expectedOrderResponse);

        // WHEN
        OrderResponse orderResponse = defaultOrderService.get(externalId);

        // THEN
        assertEquals(expectedOrderResponse, orderResponse);
    }

    @Test
    void givenOrderId_whenDelete_thenCallRepositoryMethods() {
        // GIVEN
        String externalId = "amazingExternalId";

        Order order = Order.builder()
            .externalId(externalId)
            .build();
        when(orderRepository.findByExternalId(externalId)).thenReturn(Optional.of(order));

        // WHEN
        defaultOrderService.delete(externalId);

        // THEN
        verify(orderRepository, times(1)).findByExternalId(externalId);
        verify(orderRepository, times(1)).deleteByExternalId(externalId);
    }
}