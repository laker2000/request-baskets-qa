package tests.api.baskets.post;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPostWrongConfiguration extends TestConfig {

    /**
     * Scenario: Check creation of a basket with wrong basket configuration
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Try to create a basket with edge capacity value
     * 3. Assert that basket can not be created
     *
     * */
    @Test
    public void testBasketsPostWrongConfiguration(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(10);
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);
        HashMap<String, Object> basketContent = new HashMap<>();
        basketContent.put("capacity", -1);

        // 2. Try to create a basket with edge capacity value
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        // 3. Assert that basket can not be created
        var postResponse = request.post(basketByName);
        int responseStatus = postResponse.statusCode();
        assertThat(responseStatus, is(422));
        String expectedMessage = "Capacity should be a positive number, but was -1\n";
        assertThat(postResponse.body().asString(), is(expectedMessage));

        // 1. Arrange a basket for creation
        basketContent.replace("capacity", 0);

        // 2. Try to create a basket with edge capacity value
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        // 3. Assert that basket can not be created
        postResponse = request.post(basketByName);
        responseStatus = postResponse.statusCode();
        assertThat(responseStatus, is(422));
        expectedMessage = "Capacity should be a positive number, but was 0\n";
        assertThat(postResponse.body().asString(), is(expectedMessage));

        // 1. Arrange a basket for creation
        basketContent.replace("capacity", 2001);

        // 2. Try to create a basket with edge capacity value
        requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
        request.body(basketContent);

        // 3. Assert that basket can not be created
        postResponse = request.post(basketByName);
        responseStatus = postResponse.statusCode();
        assertThat(responseStatus, is(422));
        expectedMessage = "Capacity may not be greater than 2000\n";
        assertThat(postResponse.body().asString(), is(expectedMessage));

    }

}
