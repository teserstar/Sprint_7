package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.requests.CourierLoginRequestBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response getCourierLoginMethodResponse(String login, String password) {
        CourierLoginRequestBody requestBody = new CourierLoginRequestBody(login, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/v1/courier/login");
    }

    private String randomString = RandomStringUtils.randomAlphanumeric(6);

    public void createNewCourier(String login, String password, String firstName) {
        CourierCreationTest courierCreationTest = new CourierCreationTest();
        courierCreationTest.getCourierCreationMethodResponse(login, password, firstName);
    }

    @Test
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void courierAuthorizationReturnsSuccessResponse() {
        createNewCourier("qa_scooter_" + randomString, "1234_" + randomString, null);
        getCourierLoginMethodResponse("qa_scooter_" + randomString, "1234_" + randomString)
                .then().statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void courierAuthorizationWithIncorrectLoginReturnsClientError() {
        createNewCourier("qa_scooter_" + randomString, "1234_" + randomString, null);
        getCourierLoginMethodResponse("dev_scooter_" + randomString, "1234_" + randomString)
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void courierAuthorizationWithIncorrectPasswordReturnsClientError() {
        createNewCourier("qa_scooter_" + randomString, "1234_" + randomString, null);
        getCourierLoginMethodResponse("qa_scooter_" + randomString, "4321_" + randomString)
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void courierAuthorizationWithoutLoginReturnsClientError() {
        getCourierLoginMethodResponse(null, "1234_" + randomString)
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test //На данный момент тест не проходит, так как эндпойнт возвращает ответ с ошибкой сервера "504 Gateway time out"
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void courierAuthorizationWithoutPasswordReturnsClientError() {
        getCourierLoginMethodResponse("qa_scooter_" + randomString, null)
                .then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void authorizationOfNonExistentCourierReturnsClientError() {
        getCourierLoginMethodResponse("qa_scooter_" + randomString, "4321_" + randomString)
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}
