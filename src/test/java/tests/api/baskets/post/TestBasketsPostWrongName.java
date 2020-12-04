package tests.api.baskets.post;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.Test;
import tests.TestConfig;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBasketsPostWrongName extends TestConfig {

    /**
     * Scenario: Check creation of a basket with wrong name values in order to provoke Bad Request 400 response
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. For each provided name try to create a basket with wrong given name parameter
     * 3. Assert that 400 code is received, Assert response message
     *
     * */
    @Test
    public void testBasketsPostWrongNameBadRequest(){
        // 1. Arrange a basket for creation

        List<String> wrongNames = Arrays.asList("marko vukobrt", "javascript:alert(‘Executed!’);",
                "SELECT * FROM baskets;", "$SHELL", "+0.3","cd ..", "^monsoon");
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", serviceToken);
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        String expectedMessage;

        for (String name : wrongNames) {

            builder.addPathParam("name", name);
            requestSpec = builder.build();
            request = RestAssured.given().spec(requestSpec);

            // 2. For each provided name try to create a basket with wrong given name parameter
            var postResponse = request.post(basketByName);

            // 3. Assert that 400 code is received, Assert response message
            assertThat(postResponse.statusCode(), is(400));
            expectedMessage = String.format("Invalid basket name; [%s] does not match pattern: ^[\\w\\d\\-_\\.]{1,250}$\n", name);
            assertThat(postResponse.body().asString(), is(expectedMessage));
        }

    }


    /**
     * Scenario: Check creation of a basket with wrong name values in order to provoke NOT FOUND 404 response
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. For each provided name try to create a basket with wrong given name parameter
     * 3. Assert that 404 code is received, Assert response message
     *
     * */


    @Test
    public void testBasketsPostWrongNameNotFound(){
        // 1. Arrange a basket for creation

        List<String> wrongNames = Arrays.asList("", "<script>alert('Injected!');</script>", "api/stats");
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", serviceToken);
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);

        for (String name : wrongNames) {

            builder.addPathParam("name", name);
            requestSpec = builder.build();
            request = RestAssured.given().spec(requestSpec);

            // 2. For each provided name try to create a basket with wrong given name parameter
            var postResponse = request.post(basketByName);

            // 3. Assert that 404 code is received, Assert response message
            assertThat(postResponse.statusCode(), is(404));

        }

    }

    /**
     * Scenario: Check creation of a basket with reserved name values in order to provoke Forbidden 403 response
     *
     * Test steps:
     *
     * 1. Arrange a basket for creation
     * 2. For each provided name try to create a basket with reserved name ("api", "baskets", "web")
     * 3. Assert that 403 code is received, Assert response message
     *
     * */
    @Test
    public void testBasketsPostWrongNameReservedWords(){
        // 1. Arrange a basket for creation
        List<String> wrongNames = Arrays.asList("api", "baskets", "web");
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", serviceToken);
        var requestSpec = builder.build();
        var request = RestAssured.given().spec(requestSpec);
        String expectedMessage;

        for (String name : wrongNames) {

            builder.addPathParam("name", name);
            requestSpec = builder.build();
            request = RestAssured.given().spec(requestSpec);

            // 2. For each provided name try to create a basket with wrong given name parameter
            var postResponse = request.post(basketByName);

            // 3. Assert that 400 code is received, Assert response message
            assertThat(postResponse.statusCode(), is(403));
            expectedMessage = String.format("This basket name conflicts with reserved system path: %s\n", name);
            assertThat(postResponse.body().asString(), is(expectedMessage));
        }

    }

}
