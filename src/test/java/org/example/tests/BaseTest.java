package org.example.tests;

import io.restassured.RestAssured;
import org.example.client.CourierClient;
import org.junit.After;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class BaseTest {

    private final List<Integer> couriersToDelete = new ArrayList<>();
    protected final CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected String randomLogin() {
        return "login_" + (int) (Math.random() * 100000);
    }

    protected String randomPassword() {
        return "pass_" + (int) (Math.random() * 100000);
    }

    protected void rememberCourierIdForDeletion(Integer id) {
        if (id != null) {
            couriersToDelete.add(id);
        }
    }

    @After
    public void deleteAllCreatedCouriers() {
        for (Integer id : couriersToDelete) {
            courierClient.deleteCourier(id)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
        couriersToDelete.clear();
    }
}
