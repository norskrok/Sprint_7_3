package org.example.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.Courier;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {

    private static final String DUPLICATE_COURIER_MESSAGE =
            "Этот логин уже используется. Попробуйте другой.";

    private static final String NOT_ENOUGH_DATA_TO_CREATE_MESSAGE =
            "Недостаточно данных для создания учетной записи";

    @Before
    public void prepareCourierForDuplicateTest() {
        createCourierBeforeTest();
    }

    private void assertBadRequestWithMessage(Response response, String expectedMessage) {
        response.then()
                .statusCode(400)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("Cannot create two identical couriers")
    public void cannotCreateTwoIdenticalCouriers() {

        Courier duplicateCourier =
                new Courier(createdCourierLogin, createdCourierPassword, "Friend");

        courierClient.createCourier(duplicateCourier)
                .then()
                .statusCode(409)
                .body("message", equalTo(DUPLICATE_COURIER_MESSAGE));
    }

    @Test
    @DisplayName("Create courier without login returns error")
    public void createCourierWithoutLoginReturnsError() {
        Response response = courierClient.createCourier(
                new Courier(null, randomPassword(), "Friend")
        );
        assertBadRequestWithMessage(response, NOT_ENOUGH_DATA_TO_CREATE_MESSAGE);
    }

    @Test
    @DisplayName("Create courier without password returns error")
    public void createCourierWithoutPasswordReturnsError() {
        Response response = courierClient.createCourier(
                new Courier(randomLogin(), null, "Friend")
        );
        assertBadRequestWithMessage(response, NOT_ENOUGH_DATA_TO_CREATE_MESSAGE);
    }
}
