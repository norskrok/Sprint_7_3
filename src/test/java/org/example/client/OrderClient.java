package org.example.client;

import io.restassured.response.Response;
import org.example.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS_PATH = "/api/v1/orders";

    public Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    public Response getOrdersList() {
        return given()
                .when()
                .get(ORDERS_PATH);
    }
}
