package org.example.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.client.CourierClient;
import org.example.model.Courier;
import org.example.model.CourierCredentials;
import org.junit.After;
import org.junit.Before;

import static org.hamcrest.Matchers.is;

public class BaseTest {

    protected final CourierClient courierClient = new CourierClient();

    protected String createdCourierLogin;
    protected String createdCourierPassword;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected String randomLogin() {
        return "login_" + System.currentTimeMillis();
    }

    protected String randomPassword() {
        return "pass_" + System.currentTimeMillis();
    }

    protected void createCourierBeforeTest() {
        createdCourierLogin = randomLogin();
        createdCourierPassword = randomPassword();

        Courier courier = new Courier(createdCourierLogin, createdCourierPassword, "Friend");

        courierClient.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", is(true));
    }

    @After
    public void deleteCreatedCourier() {
        if (createdCourierLogin == null || createdCourierPassword == null) return;

        Response loginResponse = courierClient.login(
                new CourierCredentials(createdCourierLogin, createdCourierPassword)
        );

        if (loginResponse.getStatusCode() == 200) {
            Integer id = loginResponse.then().extract().path("id");

            if (id != null) {
                courierClient.deleteCourier(id)
                        .then()
                        .statusCode(200);
            }
        }

        createdCourierLogin = null;
        createdCourierPassword = null;
    }
}
