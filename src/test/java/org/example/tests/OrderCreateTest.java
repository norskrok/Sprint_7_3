package org.example.tests;

import io.restassured.response.Response;
import org.example.client.OrderClient;
import org.example.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final List<String> color;
    private final OrderClient orderClient = new OrderClient();

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    public void createOrderWithDifferentColors() {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color
        );

        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}