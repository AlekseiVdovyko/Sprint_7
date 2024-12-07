import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.CourierData;
import ru.yandex.practicum.model.api.CourierApi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static ru.yandex.practicum.model.CourierGenerator.*;

public class LoginCourierTest {

    protected CourierData courierData;
    protected CourierApi courierApi = new CourierApi();

    @Before
    public void setUpAndCreateCourier() {
        courierData = getRandomCourier("Aleks" ,"1234", "Aleks");
        ValidatableResponse response = courierApi.createCourier(courierData);
    }

    @Test
    @DisplayName("Check login courier /api/v1/courier/login")
    @Description("Basic test for /api/v1/courier/login endpoint")
    public void checkAuthorizedCourierTest() {
        ValidatableResponse responseLoginCourier = courierApi.loginCourier(courierData);

        responseLoginCourier.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue());
    }

    @Test
    @DisplayName("Check login courier with login argument /api/v1/courier/login")
    @Description("Can not login courier without login argument")
    public void checkLoginWithoutLoginArgumentTest() {
        CourierData courierDataWrong = getRandomCourierWithoutLoginArgument("1234", "Aleks");
        ValidatableResponse responseWithoutLogin = courierApi.loginCourier(courierDataWrong);

        responseWithoutLogin.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login courier with password argument /api/v1/courier/login")
    @Description("Can not login courier without password argument")
    public void checkLoginWithoutPasswordArgumentTest() {
        CourierData courierDataWrong = getRandomCourierWithoutPasswordArgument("Aleks", "Aleks");
        ValidatableResponse responseWithoutLogin = courierApi.loginCourier(courierDataWrong);

        responseWithoutLogin.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    @Test
    @DisplayName("Check login nonexistent courier /api/v1/courier/login")
    @Description("Can not login nonexistent courier")
    public void checkLoginNonexistentCourierTest() {
        CourierData courierNonexistent = getRandomCourier("Aleks" ,"1234", "Aleks");
        ValidatableResponse response = courierApi.loginCourier(courierNonexistent);

        response.log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
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
