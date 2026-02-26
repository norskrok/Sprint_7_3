package org.example.tests;

import org.example.client.OrderClient;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    public void getOrdersListReturnsOrders() {
        orderClient.getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
