package tests.api.baskets.delete;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsDelete extends TestConfig {

    /**
     * Scenario: Check deletion of a basket
     *
     * Test steps:
     *
     * 1. Create a basket
     * 2. Assert that basket is created
     * 3. Delete a given basket
     * 4. Assert that given basket is deleted
     *
     * */
    @Test
    public void testBasketDelete(){
        // Arrange
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

        // 1. Create a basket
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));
        String basketToken = response.body().jsonPath().get("token");

        // 2. Assert that basket is created
        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("forward_url"), is("https://nba.com"));
        assertThat(response.getBody().jsonPath().get("proxy_response"), is(true));
        assertThat(response.getBody().jsonPath().get("insecure_tls"), is(true));
        assertThat(response.getBody().jsonPath().get("expand_path"), is(true));
        assertThat(response.getBody().jsonPath().get("capacity"), is(321));

        // 3. Delete a given basket
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", basketToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);

        response = request.delete(basketByName);
        assertThat(response.statusCode(), is(204));

        // 4. Assert that given basket is deleted
        response = request.get(basketByName);
        assertThat(response.statusCode(), is(404));

    }
}
