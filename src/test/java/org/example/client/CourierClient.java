package org.example.client;

import io.restassured.response.Response;
import org.example.model.Courier;
import org.example.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    public Response login(CourierCredentials creds) {
        return given()
                .header("Content-Type", "application/json")
                .body(creds)
                .when()
                .post(LOGIN_PATH);
    }

    public Response deleteCourier(int id) {
        return given()
                .when()
                .delete(COURIER_PATH + "/" + id);
    }
}