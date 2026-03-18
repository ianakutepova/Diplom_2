
import base.BaseTest;
import base.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.Test;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;


public class CreateOrderWithAuthorizationPositiveTest extends BaseTest {

    @Test
    @DisplayName("Test creating an order successfully")
    @Description("Checks success when create an order with ingredients")
    public void testCreateOrderWithAuthorizationWithIngredientsSuccess() {
        setKeepUser(true); // Устанавливаем keepUser в true для сохранения пользователя после теста

        String[] userData = createUniqueUser();
        User user = new User(userData[0], userData[1], userData[2]);

        System.out.println("Registering the user...");
        Response registerResponse = userPostRequest(Endpoints.REGISTER, user, null);
        accessToken = registerResponse.jsonPath().getString("accessToken");

        assertNotNull(accessToken, "accessToken is null");

        System.out.println("Received accessToken: " + accessToken);

        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));

        System.out.println("Sending order with accessToken: " + accessToken);
        Response orderResponse = orderPostRequest(Endpoints.ORDERS, order, accessToken);

        orderResponse.then()
                .statusCode(SC_OK)
                .log().body()
                .body("success", equalTo(true))
                .body("owner", notNullValue());
    }
    }

