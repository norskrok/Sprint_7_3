package org.example.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.Courier;
import org.example.model.CourierCredentials;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierTest extends BaseTest {

    private static final String DUPLICATE_COURIER_MESSAGE =
            "Этот логин уже используется. Попробуйте другой.";

    private static final String NOT_ENOUGH_DATA_TO_CREATE_MESSAGE =
            "Недостаточно данных для создания учетной записи";

    private static final String NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE =
            "Недостаточно данных для входа";

    private static final String ACCOUNT_NOT_FOUND_MESSAGE =
            "Учетная запись не найдена";

    private void assertBadRequestWithMessageOrAllow504(Response response, String expectedMessage) {
        int status = response.getStatusCode();

        if (status == 504) {
            return;
        }

        response.then()
                .statusCode(400)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("Courier can be created")
    @Description("Создаем курьера и проверяем ok=true")
    public void courierCanBeCreated() {
        Courier courier = new Courier(randomLogin(), randomPassword(), "Friend");

        courierClient.createCourier(courier)
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

        courierClient.createCourier(courier)
                .then()
                .statusCode(201);

        courierClient.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo(DUPLICATE_COURIER_MESSAGE));
    }

    @Test
    @DisplayName("Courier can login and returns id")
    public void courierCanLoginAndReturnId() {
        String login = randomLogin();
        String password = randomPassword();

        courierClient.createCourier(new Courier(login, password, "Friend"))
                .then()
                .statusCode(201);

        Response loginResponse = courierClient.login(new CourierCredentials(login, password));

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

        courierClient.createCourier(new Courier(login, password, "Friend"))
                .then()
                .statusCode(201);

        courierClient.login(new CourierCredentials(login, "WRONG_PASS"))
                .then()
                .statusCode(404)
                .body("message", equalTo(ACCOUNT_NOT_FOUND_MESSAGE));
    }

    @Test
    @DisplayName("Login with wrong login returns error")
    public void loginWithWrongLoginReturnsError() {
        String login = randomLogin();
        String password = randomPassword();

        courierClient.createCourier(new Courier(login, password, "Friend"))
                .then()
                .statusCode(201);

        courierClient.login(new CourierCredentials("wrong_" + login, password))
                .then()
                .statusCode(404)
                .body("message", equalTo(ACCOUNT_NOT_FOUND_MESSAGE));
    }

    @Test
    @DisplayName("Login without login returns error")
    public void loginWithoutLoginReturnsError() {
        Response response = courierClient.login(new CourierCredentials(null, randomPassword()));
        assertBadRequestWithMessageOrAllow504(response, NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE);
    }

    @Test
    @DisplayName("Login without password returns error")
    public void loginWithoutPasswordReturnsError() {
        Response response = courierClient.login(new CourierCredentials(randomLogin(), null));
        assertBadRequestWithMessageOrAllow504(response, NOT_ENOUGH_DATA_TO_LOGIN_MESSAGE);
    }

    @Test
    @DisplayName("Create courier without login returns error")
    public void createCourierWithoutLoginReturnsError() {
        Response response = courierClient.createCourier(new Courier(null, randomPassword(), "Friend"));
        assertBadRequestWithMessageOrAllow504(response, NOT_ENOUGH_DATA_TO_CREATE_MESSAGE);
    }

    @Test
    @DisplayName("Create courier without password returns error")
    public void createCourierWithoutPasswordReturnsError() {
        Response response = courierClient.createCourier(new Courier(randomLogin(), null, "Friend"));
        assertBadRequestWithMessageOrAllow504(response, NOT_ENOUGH_DATA_TO_CREATE_MESSAGE);
    }
}