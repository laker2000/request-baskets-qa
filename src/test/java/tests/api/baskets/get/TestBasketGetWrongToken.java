package tests.api.baskets.get;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketGetWrongToken extends TestConfig {

    /**
     * Scenario: Check GET /baskets endpoint with invalid token
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a given basket
     * 3. Assert that basket is created
     * 4. Execute get method for a given basket
     * 5. Assert that 401 code is returned
     *
     * */
    @Test
    public void testBasketGetInvalidToken(){
        // 1. Arrange a basket for creation
        String wrongToken = "Wrong one!";
        String basketName = RandomStringUtils.randomAlphanumeric(10);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.com");

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

        // 4. Get desired basket using query parameter
        builder = new RequestSpecBuilder();
        builder.addQueryParam("q", basketName);
        builder.addHeader("Authorization", wrongToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        var getResponse  = request.get(baskets);

        // 5. Assert that 401 code is received
        assertThat(getResponse.statusCode(), is(401));

    }


    /**
     * Scenario: Check GET /baskets endpoint for desired basket
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a given basket
     * 3. Assert that basket is created
     * 4. Execute get method for a given basket
     * 5. Assert that 401 code is returned
     *
     * */
    @Test
    public void testBasketGetMissingToken(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.com");

        // 2. Create a given basket
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        var postResponse = request.post(basketByName);
        assertThat(postResponse.statusCode(), is(201));

        // 3. Assert that basket is created
        postResponse = request.get(basketByName);
        assertThat(postResponse.statusCode(), is(200));
        assertThat(postResponse.getBody().jsonPath().get("forward_url"), is("https://nba.com"));

        // 4. Get desired basket using query parameter
        builder = new RequestSpecBuilder();
        builder.addQueryParam("q", basketName);

        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        var getResponse  = request.get(baskets);

        // 5. Assert that 401 code is received
        assertThat(getResponse.statusCode(), is(401));


    }

}
