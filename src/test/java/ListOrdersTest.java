import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.practicum.model.api.OrderApi;

import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOrdersTest {

    @Test
    @DisplayName("Get list orders /api/v1/orders")
    @Description("Basic test for list orders /api/v1/orders endpoint")
    public void getListOrdersTest() {
        OrderApi orderApi = new OrderApi();
        ValidatableResponse response = orderApi.listOrders();

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue());
    }
}
