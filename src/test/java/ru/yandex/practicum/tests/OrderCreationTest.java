package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.requests.OrderCreationRequestBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public OrderCreationTest(String firstName, String lastName, String address, int metroStation, String phone,
                             int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        String[] black = {"BLACK"};
        String[] grey = {"GREY"};
        String[] blackAndGrey = {"BLACK", "GREY"};
        String[] empty = {};

        return new Object[][] {
                {"Иван", "Иванов", "Майская, д.1, кв.1", 1, "+79091234567", 2, "2022-11-21", "Быстрая доставка", black},
                {"Петр", "Петров", "Советская, д.2, кв.2", 2, "+79097654321", 4, "2022-11-22", "Серого цвета", grey},
                {"Денис", "Денисов", "Фруктовая, д.3, кв.3", 3, "+79097654321", 6, "2022-11-23", "Бюджетный", blackAndGrey},
                {"Егор", "Егоров", "Школьная, д.4, кв.4", 4, "+79097654321", 8, "2022-11-24", "Полный заряд", empty}
        };
    }

    public Response getOrderCreationMethodResponse(String firstName, String lastName, String address, int metroStation,
                                                   String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        OrderCreationRequestBody requestBody = new OrderCreationRequestBody(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/api/v1/orders");
    }

    @Test
    @Description("Basic test for /api/v1/orders endpoint")
    public void creatingNewOrderReturnsSuccessResponse() {
        getOrderCreationMethodResponse(firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color)
                .then().statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }
}
