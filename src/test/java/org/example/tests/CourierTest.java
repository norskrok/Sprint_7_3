package org.example.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.Courier;
import org.example.model.CourierCredentials;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierTest extends BaseTest {

    @Test
    @DisplayName("Courier can be created")
    @Description("Создаем курьера и проверяем ok=true")
    public void courierCanBeCreated() {
        Courier courier = new Courier(randomLogin(), randomPassword(), "Islam");

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Cannot create two identical couriers")
    public void cannotCreateTwoIdenticalCouriers() {
        String login = randomLogin();
        String password = randomPassword();

        Courier courier = new Courier(login, password, "Friend");

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Courier can login and returns id")
    public void courierCanLoginAndReturnId() {
        String login = randomLogin();
        String password = randomPassword();

        Courier courier = new Courier(login, password, "Friend");

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        Response loginResponse =
                given()
                        .header("Content-Type", "application/json")
                        .body(new CourierCredentials(login, password))
                        .post("/api/v1/courier/login");

        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue());

        Integer id = loginResponse.then().extract().path("id");

        rememberCourierIdForDeletion(id);
    }

    @Test
    @DisplayName("Login with wrong password returns error")
    public void loginWithWrongPasswordReturnsError() {
        String login = randomLogin();
        String password = randomPassword();

        Courier courier = new Courier(login, password, "Friend");

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        given()
                .header("Content-Type", "application/json")
                .body(new CourierCredentials(login, "WRONG_PASS"))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }
}
