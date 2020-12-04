package tests.api.baskets.post;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import tests.TestConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPostLengthOfName extends TestConfig {

    /**
     * Scenario: Check creation of a basket with a boundary (length) name values
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. For each provided name try to create a basket with given boundary name parameter
     * 3. Assert that basket is created
     *
     * */
    @Test
    public void testBasketsPostLengthOfName(){
        // 1. Arrange a basket for creation

        List<String> boundaryNames = Arrays.asList(
                RandomStringUtils.randomAlphanumeric(1),
                RandomStringUtils.randomAlphanumeric(2),
                RandomStringUtils.randomAlphanumeric(249),
                RandomStringUtils.randomAlphanumeric(250),
                "-" + RandomStringUtils.randomNumeric(1), // yep, you can call it e.g: -89
                RandomStringUtils.randomNumeric(1),
                RandomStringUtils.randomNumeric(2),
                RandomStringUtils.randomNumeric(249),
                RandomStringUtils.randomNumeric(250)

        );
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", serviceToken);
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);


        for (String name : boundaryNames) {

            builder.addPathParam("name", name);
            requestSpec = builder.build();
            request = RestAssured.given().spec(requestSpec);

            // 2. For each provided name try to create a basket with given boundary name parameter
            var postResponse = request.post(basketByName);

            // 3. Assert that basket is created
            assertThat(postResponse.statusCode(), is(201));

        }

    }

    /**
     * Scenario: Check creation of a basket when basket name length exceeds maximum value
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. Try to create a basket with name longer than supported (250 + 1 chars)
     * 3. Assert that basket is not created, Assert status code, Assert response message
     *
     * */
    @Test
    public void testBasketsPostLengthOfNameOutOfIndex(){
        // 1. Arrange a basket for creation
        String basketName = RandomStringUtils.randomAlphanumeric(251);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addPathParam("name", basketName);
        builder.addHeader("Authorization", serviceToken);

        // 2. Try to create a basket with name longer than supported (250 + 1 chars)
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);

        var postResponse = request.post(basketByName);
        assertThat(postResponse.statusCode(), is(400));
        // 3. Assert that basket is not created, Assert status code, Assert response message
        String expectedMessage = String.format("Invalid basket name; [%s] does not match pattern: ^[\\w\\d\\-_\\.]{1,250}$\n", basketName);
        assertThat(postResponse.body().asString(), is(expectedMessage));

    }

}
