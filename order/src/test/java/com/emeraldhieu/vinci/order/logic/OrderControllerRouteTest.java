package com.emeraldhieu.vinci.order.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A simple unit test that tests controller routing.
 * It runs fast because no application context is loaded.
 * See https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/#strategy-1-spring-mockmvc-example-in-standalone-modemode
 */
@ExtendWith(MockitoExtension.class)
@Import({CustomStringToListConverter.class})
class OrderControllerRouteTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String id;
    private String productName;
    private OrderResponse orderResponse;

    @BeforeEach
    public void setUp() {
        FormattingConversionService conversionService = new FormattingConversionService();
        conversionService.addConverter(new CustomStringToListConverter());

        /**
         * I manually create mockMvc because I want to set only necessary components.
         * If @WebMvcTest was used, it would load even @ControllerAdvice that is redundant.
         * See https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/#strategy-1-spring-mockmvc-example-in-standalone-modemode
         * Keep it simple and fast.
         */
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
            .setConversionService(conversionService)
            .build();

        id = "pizza";
        productName = "Pizza";
        orderResponse = OrderResponse.builder()
            .id(id)
            .products(List.of(productName))
            .build();
    }

    @Test
    void givenOrderService_whenListOrders_thenReturnOrders() throws Exception {
        // GIVEN
        int offset = 0;
        int limit = 10;
        Page<OrderResponse> orderResponses = new PageImpl<>(List.of(
            orderResponse
        ));
        when(orderService.list(offset, limit, List.of("updatedAt,desc"))) // "updatedAt,desc" is specified default in Open API file
            .thenReturn(orderResponses);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", equalTo(id)))
            .andExpect(jsonPath("$[0].products[0]", equalTo(productName)));
    }

    @Test
    void givenOrderService_whenCreateOrder_thenReturnAnOrder() throws Exception {
        // GIVEN
        OrderRequest orderRequest = OrderRequest.builder()
            .products(List.of(productName))
            .build();
        when(orderService.create(orderRequest)).thenReturn(orderResponse);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", equalTo(id)))
            .andExpect(jsonPath("$.products[0]", equalTo(productName)));
    }

    @Test
    void givenOrderService_whenGetOrder_thenReturnAnOrder() throws Exception {
        // GIVEN
        when(orderService.get(id)).thenReturn(orderResponse);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(id)))
            .andExpect(jsonPath("$.products[0]", equalTo(productName)));
    }

    @Test
    void givenOrderService_whenUpdateOrder_thenReturnAnOrder() throws Exception {
        // GIVEN
        String nameToUpdate = productName + "updated";
        OrderRequest orderRequest = OrderRequest.builder()
            .products(List.of(nameToUpdate))
            .build();
        OrderResponse updatedOrderResponse = orderResponse.toBuilder()
            .products(List.of(nameToUpdate))
            .build();
        when(orderService.update(id, orderRequest)).thenReturn(updatedOrderResponse);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/{id}", id)
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(id)))
            .andExpect(jsonPath("$.products[0]", equalTo(nameToUpdate)));
    }

    @Test
    void givenOrderService_whenDeleteOrder_thenReturnNoContent() throws Exception {
        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", id))
            .andExpect(status().isNoContent());
        verify(orderService, times(1)).delete(id);
    }
}