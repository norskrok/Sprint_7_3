package org.example.tests;

import io.qameta.allure.junit4.DisplayName;
import org.example.model.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginPositiveTest extends BaseTest {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE =
            "Учетная запись не найдена";

    @Before
    public void prepareCourier() {
        createCourierBeforeTest();
    }

    @Test
    @DisplayName("Courier can login and returns id")
    public void courierCanLoginAndReturnId() {
        courierClient.login(new CourierCredentials(createdCourierLogin, createdCourierPassword))
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Login with wrong password returns error")
    public void loginWithWrongPasswordReturnsError() {
        courierClient.login(new CourierCredentials(createdCourierLogin, "WRONG_PASS"))
                .then()
                .statusCode(404)
                .body("message", equalTo(ACCOUNT_NOT_FOUND_MESSAGE));
    }

    @Test
    @DisplayName("Login with wrong login returns error")
    public void loginWithWrongLoginReturnsError() {
        courierClient.login(new CourierCredentials("wrong_" + createdCourierLogin, createdCourierPassword))
                .then()
                .statusCode(404)
                .body("message", equalTo(ACCOUNT_NOT_FOUND_MESSAGE));
    }
}