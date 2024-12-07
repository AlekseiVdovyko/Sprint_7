import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.model.CourierData;
import ru.yandex.practicum.model.api.CourierApi;

import static org.hamcrest.CoreMatchers.is;
import static ru.yandex.practicum.model.CourierGenerator.*;

public class CreateCourierTest {

    protected CourierData courierData;
    protected CourierApi courierApi = new CourierApi();

    @Test
    @DisplayName("Check create courier /api/v1/courier/login")
    @Description("Basic test for create courier /api/v1/courier/login endpoint")
    public void courierCanBeCreatedTest() {
        courierData = getRandomCourier("Alex", "1234", "Aleks");
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check create identical courier /api/v1/courier/login")
    @Description("Can not create identical courier")
    public void checkCanNotCreateIdenticalCourierTest() {
        courierData = getRandomCourier("Alex", "1234", "Aleks");
        ValidatableResponse response = courierApi.createCourier(courierData);
        ValidatableResponse responseIdenticalCourier = courierApi.createCourier(courierData);

        responseIdenticalCourier.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check create courier without login argument /api/v1/courier/login")
    @Description("Can not create courier without login argument")
    public void checkCreateWithoutLoginArgumentTest() {
        courierData = getRandomCourierWithoutLoginArgument("1234", "Aleks");
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check create courier without password argument /api/v1/courier/login")
    @Description("Can not create courier without password argument")
    public void checkCreateWithoutPasswordArgumentTest() {
        courierData = getRandomCourierWithoutPasswordArgument("Aleks", "Aleks");
        ValidatableResponse response = courierApi.createCourier(courierData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCreatedCourierAccount() {
        ValidatableResponse responseLoginCourier = courierApi.loginCourier(courierData);

        if(responseLoginCourier.extract().statusCode() == 200) {
            String idCourier = responseLoginCourier.extract().jsonPath().get("id").toString();
            ValidatableResponse responseDeleteCourier = courierApi.deleteCourier(idCourier);

            responseDeleteCourier.log().all().
                    assertThat().
                    statusCode(HttpStatus.SC_OK)
                    .body("ok", is(true));
        }
    }
}
