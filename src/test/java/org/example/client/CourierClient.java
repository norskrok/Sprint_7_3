package org.example.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.example.model.Courier;
import org.example.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Create courier with login: {courier.login}")
    public Response createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Login courier with login: {credentials.login}")
    public Response login(CourierCredentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Delete courier with id: {id}")
    public Response deleteCourier(int id) {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .delete(COURIER_PATH + "/" + id);
    }
}