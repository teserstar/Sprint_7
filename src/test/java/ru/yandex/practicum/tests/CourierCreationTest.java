package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.CourierCreationRequestBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response getCourierCreationMethodResponse(String login, String password, String firstName) {
        CourierCreationRequestBody requestBody = new CourierCreationRequestBody(login, password, firstName);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/v1/courier");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingNewCourierReturnsSuccessResponse() {
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "1234_" + randomString, "biker_" + randomString)
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingDuplicateCourierReturnsClientError() {
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "1234_" + randomString, "biker_" + randomString);
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "1234_" + randomString, "biker_" + randomString)
                .then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingNewCourierWithOnlyRequiredFieldsReturnsSuccessResponse() {
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "1234_" + randomString, null)
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingNewCourierWithoutLoginReturnsClientError() {
        getCourierCreationMethodResponse("qa_scooter_" + randomString, null, "biker_" + randomString)
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingNewCourierWithoutPasswordReturnsClientError() {
        getCourierCreationMethodResponse(null, "1234_" + randomString, "biker_" + randomString)
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Basic test for /api/v1/courier endpoint")
    public void creatingDuplicateCourierLoginReturnsClientError() {
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "1234_" + randomString, "biker_" + randomString);
        getCourierCreationMethodResponse("qa_scooter_" + randomString, "4321_" + randomString, "driver_" + randomString)
                .then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
