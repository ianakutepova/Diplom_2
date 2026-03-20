import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderWithoutAuthorizationNegativeTest extends BaseTest {


    @Test
    @DisplayName("Test creating an order with missing body")
    @Description("Checks error when create an order without ingredients")
    public void testCreateOrderWithoutAuthorizationWithoutIngredientsError() {
        User user = new User(userData[0], userData[1], userData[2]);

        Order order = new Order(null);

        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, null);

        orderResponse.then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Test creating an order with wrong body")
    @Description("Checks error when create an order with wrong hash of ingredients")
    public void testCreateOrderWithWrongIngredientsHashError() {

        User user = new User(userData[0], userData[1], userData[2]);
        List<String> wrongIngredients = Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f8200f"
        );

        Order order = new Order(wrongIngredients);

        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, null);

        orderResponse.then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}



