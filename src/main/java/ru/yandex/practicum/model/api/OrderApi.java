package ru.yandex.practicum.model.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.OrderData;

import static io.restassured.RestAssured.given;

public class OrderApi extends RestApi {

    public static final String BLACK_SCOOTER = "BLACK";
    public static final String GREY_SCOOTER = "GREY";
    public static final String CREATE_ORDERS_URI = "/api/v1/orders";
    public static final String CANCEL_ORDER_URI = "/api/v1/orders/cancel";
    public static final String LIST_ORDERS_URI = "/api/v1/orders";

    @Step("Create order")
    public ValidatableResponse createOrder(OrderData order){
        return given()
                .spec(requestSpecification())
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDERS_URI)
                .then();
    }

    @Step("Cancel order")
    public ValidatableResponse cancelOrder(int trackNumber) {
        return given()
                .spec(requestSpecification())
                .and()
                .queryParams("track", trackNumber)
                .when()
                .put(CANCEL_ORDER_URI)
                .then();
    }

    @Step("List orders")
    public ValidatableResponse listOrders() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(LIST_ORDERS_URI)
                .then();
    }
}
