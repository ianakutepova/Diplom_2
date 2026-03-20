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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class CreateOrderWithAuthorizationNegativeTest extends BaseTest {

    public static String accessToken;
    public static boolean keepUser = false;

    @Test
    @DisplayName("Test creating an order with missing body")
    @Description("Checks error when create an order without ingredients")
    public void testCreateOrderWithAuthorizationWithoutIngredientsError() {
        setKeepUser(true);
        User user = new User(userData[0], userData[1], userData[2]);

        System.out.println("Registering the user...");
        Response registerResponse = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = registerResponse.jsonPath().getString("accessToken");

        assertNotNull(accessToken, "accessToken is null");

        Order order = new Order(null);

        if (accessToken != null) {
            System.out.println("Sending order with accessToken: " + accessToken);
            Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, accessToken);

            orderResponse.then()
                    .log().all()
                    .statusCode(SC_BAD_REQUEST)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Ingredient ids must be provided"));

        } else {
            System.out.println("Failed to send order: accessToken is null");
        }
    }

    @Test
    @DisplayName("Test creating an order with wrong body")
    @Description("Checks error when create an order with wrong hash of ingredients")
    public void testCreateOrderWithAuthorizationWithWrongIngredientsHashError() {
        setKeepUser(true);

        User user = new User(userData[0], userData[1], userData[2]);

        System.out.println("Registering the user...");
        Response registerResponse = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = registerResponse.jsonPath().getString("accessToken");

        assertNotNull(accessToken, "accessToken is null");

        List<String> wrongIngredients = Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f8200f"
        );

        Order order = new Order(wrongIngredients);

        if (accessToken != null) {
            System.out.println("Sending order with accessToken: " + accessToken);
            Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, accessToken);

            orderResponse.then()
                    .log().all()
                    .statusCode(SC_INTERNAL_SERVER_ERROR);
        } else {
            System.out.println("Failed to send order: accessToken is null");
        }
    }
}
