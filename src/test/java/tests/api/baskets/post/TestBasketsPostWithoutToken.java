package tests.api.baskets.post;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPostWithoutToken extends TestConfig {

    /**
     * Scenario: Check creation of a basket without any authorization
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Create a given basket without auth
     * 3. Assert that basket is created
     *
     * */
    @Test
    public void testBasketsPostWithoutToken(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);

        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("forward_url", "https://nba.com");

        //2. Create a given basket without auth
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        var response = request.post(basketByName);
        assertThat(response.statusCode(), is(201));

        // 3. Assert that basket is created
        builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        response = request.get(basketByName);
        assertThat(response.statusCode(), is(200));
        assertThat(response.getBody().jsonPath().get("forward_url"), is("https://nba.com"));

    }


}
