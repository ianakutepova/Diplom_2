import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.Test;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class CreateOrderWithoutAuthorizationPositiveTest extends BaseTest {

    @Test
    @DisplayName("Test creating an order successfully")
    @Description("Checks success when create an order with ingredients")
    public void testCreateOrderWithoutAuthorizationWithIngredientsSuccess() {
        User user = new User(userData[0], userData[1], userData[2]);
        Order order = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));

        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, null);

        orderResponse.then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
}

