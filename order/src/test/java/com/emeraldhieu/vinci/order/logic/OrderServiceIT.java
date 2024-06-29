package com.emeraldhieu.vinci.order.logic;

import com.emeraldhieu.vinci.order.OrderApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
    classes = OrderApp.class
)
class OrderServiceIT extends BaseTestContainersTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void givenRequest_whenCreate_thenReturnResponse() {
        // GIVEN
        var products = List.of("pepperoni", "margherita", "marinara");
        var request = OrderRequest.builder()
            .products(products)
            .build();

        // WHEN
        var createdOrder = orderService.create(request);

        // THEN
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getProducts()).isEqualTo(products);
        assertThat(createdOrder.getCreatedBy()).isNotNull();
        assertThat(createdOrder.getCreatedAt()).isNotNull();
        assertThat(createdOrder.getUpdatedBy()).isNotNull();
        assertThat(createdOrder.getUpdatedAt()).isNotNull();
    }
}