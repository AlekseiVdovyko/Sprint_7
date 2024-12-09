import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.model.OrderData;
import ru.yandex.practicum.model.api.OrderApi;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    protected OrderApi orderApi = new OrderApi();
    protected ValidatableResponse response;

    protected String firstName;
    protected String lastName;
    protected String address;
    protected String metroStation;
    protected String phone;
    protected String rentTime;
    protected String deliveryDate;
    protected String comment;
    protected List<String> color;

    public CreateOrderTest(String firstName, String lastName, String address,
                           String metroStation, String phone, String rentTime,
                           String deliveryDate, String comment, List<String> color) {
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

    @Parameterized.Parameters(name = "Тестовые данные: {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}")
    public static Object[] getOrder() {
        return new Object[][] {
                {"Julio", "Iglesias", "Madrid, 142 apt. SPa",
                        "6", "+7 800 355 35 35", "1", "2024-12-12",
                        "Give me my scooter", List.of(OrderApi.BLACK_SCOOTER)},
                {"Iglesias", "Julio", "142 apt. SPa, Madrid",
                        "6", "+7 800 35 355 35", "2", "2024-12-13",
                        "It is my scooter", List.of(OrderApi.GREY_SCOOTER)},
                {"Julio", "Iglesias", "Madrid, 142 apt. SPa",
                        "6", "+7 800 35 35 355", "3", "2024-12-14",
                        "Give me my scooter", List.of(OrderApi.GREY_SCOOTER, OrderApi.BLACK_SCOOTER)},
                {"Iglesias", "Julio", "142 apt. SPa, Madrid",
                        "6", "+7 800 355 35 35", "4", "2024-12-15",
                        "It is my scooter", List.of()}
        };
    }

    @Test
    @DisplayName("Check create order /api/v1/orders")
    @Description("Basic test for create order /api/v1/orders endpoint")
    public void orderCanBeCreatedTest() {
        OrderData orderData = new OrderData(firstName, lastName, address, metroStation, phone,
                                            rentTime, deliveryDate, comment, color);
        response = orderApi.createOrder(orderData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(notNullValue());
    }

    @After
    public void cancelOrder() {
        int trackNumber = response.extract().jsonPath().get("track");
        ValidatableResponse responseDeleteOrder = orderApi.cancelOrder(trackNumber);

        responseDeleteOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("ok", is(true));
    }
}
