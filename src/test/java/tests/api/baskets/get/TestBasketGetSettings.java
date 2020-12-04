package tests.api.baskets.get;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketGetSettings extends TestConfig {

    /**
     * Scenario: Check GET /baskets endpoint for desired basket
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a given basket
     * 3. Assert basket settings response schema
     *
     * */
    @Test
    public void testBasketGetSettings(){
        // 1. Arrange a basket for creation
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

        // 2. Create a given basket
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        var response = request.post(basketByName);
        String basketToken = response.body().jsonPath().get("token");
        // 3. Assert basket settings response schema
        assertThat(response.statusCode(), is(201));

        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", basketToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);

        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("forward_url"), is("https://nba.com"));
        assertThat(response.getBody().jsonPath().get("proxy_response"), is(true));
        assertThat(response.getBody().jsonPath().get("insecure_tls"), is(true));
        assertThat(response.getBody().jsonPath().get("expand_path"), is(true));
        assertThat(response.getBody().jsonPath().get("capacity"), is(321));

    }

}
