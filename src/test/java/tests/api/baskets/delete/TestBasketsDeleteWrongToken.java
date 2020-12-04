package tests.api.baskets.delete;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsDeleteWrongToken extends TestConfig {

    /**
     * Scenario: Check deletion of basket when using wrong auth token
     *
     * Test steps:
     *
     * 1. Create a basket
     * 2. Assert that basket is created
     * 3. Try to delete a given basket with wrong token
     * 4. Assert that 401 code is received
     *
     * */
    @Test
    public void testBasketDeleteWrongToken(){

        // 1. Create a basket
        String wrongToken = "wrong one!";
        String basketName = RandomStringUtils.randomAlphanumeric(10);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.com");
        basketContent.put("proxy_response", true);
        basketContent.put("insecure_tls", true);
        basketContent.put("expand_path", true);
        basketContent.put("capacity", 321);

        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);

        request.body(basketContent);
        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));

        // 2. Assert that basket is created
        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("capacity"), is(321));

        // 3. Try to delete a given basket with wrong service/basket token
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", wrongToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        response = request.delete(basketByName);

        // 4. Assert that 401 status code is received
        assertThat(response.statusCode(), is(401));


    }
}
