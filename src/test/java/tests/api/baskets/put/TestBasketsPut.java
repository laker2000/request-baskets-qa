package tests.api.baskets.put;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPut extends TestConfig {

    /**
     * Scenario: Check creation of a basket
     *
     * Test steps:
     *
     * 1. Create a basket with desired settings
     * 2. Change basket settings
     * 3. Assert that basket settings changes are applied
     *
     * */
    @Test
    public void testBasketsPut(){
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
        assertThat(response.statusCode(), is(201));

        // 3. Assert that basket is created
        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("forward_url"), is("https://nba.com"));
        assertThat(response.getBody().jsonPath().get("proxy_response"), is(true));
        assertThat(response.getBody().jsonPath().get("insecure_tls"), is(true));
        assertThat(response.getBody().jsonPath().get("expand_path"), is(true));
        assertThat(response.getBody().jsonPath().get("capacity"), is(321));

        basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.org");
        basketContent.put("proxy_response", false);
        basketContent.put("insecure_tls", false);
        basketContent.put("expand_path", false);
        basketContent.put("capacity", 123);

        request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        response = request.put(basketByName);
        assertThat(response.statusCode(), is(204));

        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("forward_url"), is("https://nba.org"));
        assertThat(response.getBody().jsonPath().get("proxy_response"), is(false));
        assertThat(response.getBody().jsonPath().get("insecure_tls"), is(false));
        assertThat(response.getBody().jsonPath().get("expand_path"), is(false));
        assertThat(response.getBody().jsonPath().get("capacity"), is(123));

    }

}
