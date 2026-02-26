package org.example.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.example.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Create order with colors: {order.color}")
    public Response createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Get orders list")
    public Response getOrdersList() {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .get(ORDERS_PATH);
    }
}
