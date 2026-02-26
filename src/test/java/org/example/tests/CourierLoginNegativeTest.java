package org.example.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.CourierCredentials;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierLoginNegativeTest extends BaseTest {

    private static final String NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE =
            "Недостаточно данных для входа";

    private void assertBadRequestWithMessage(Response response, String expectedMessage) {
        response.then()
                .statusCode(400)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("Login without login returns error")
    public void loginWithoutLoginReturnsError() {
        Response response = courierClient.login(new CourierCredentials("", randomPassword()));
        assertBadRequestWithMessage(response, NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE);
    }

    @Test
    @DisplayName("Login without password returns error")
    public void loginWithoutPasswordReturnsError() {
        Response response = courierClient.login(new CourierCredentials(randomLogin(), ""));
        assertBadRequestWithMessage(response, NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE);
    }
}