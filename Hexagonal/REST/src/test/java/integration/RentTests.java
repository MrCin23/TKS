package integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.*;
import pl.lodz.p.data.DataInitializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentTests {

    DataInitializer dataInitializer = new DataInitializer();

    @BeforeEach
    public void initCollection() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/REST/api";
        dataInitializer.dropAndCreateRent();
        dataInitializer.dropAndCreateClient();
//        dataInitializer.initClient();
        dataInitializer.init();
//        dataInitializer.init();
    }

//    @AfterEach
//    public void dropCollection() {
//        dataInitializer.dropAndCreateRent();
//        dataInitializer.init();
//    }

    String rentJson = """
                {
                    "username": "JDoe",
                    "vmId": "7ab44a0b-8347-41cb-a64a-452666d0494a",
                    "startTime": "2024-11-11T11:11"
                }
                """;

    public void createClient() {
        String clientJson = """
                {
                    "entityId": {
                        "uuid": "123e4567-e89b-12d3-a456-426614174000"
                    },
                    "firstName": "John",
                    "surname": "Doe",
                    "username": "JDoe",
                    "emailAddress": "john.doe@example.com",
                    "_clazz": "Client",
                    "role": "CLIENT",
                    "clientType": {
                        "_clazz": "standard",
                        "entityId": {
                            "uuid": "5bd23f3d-0be9-41d7-9cd8-0ae77e6f463d"
                        },
                        "maxRentedMachines": 5,
                        "name": "Standard"
                    },
                    "currentRents": 0,
                    "active": true,
                    "password": "12345678"
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/client")
                .then()
                .statusCode(201);
    }

    public String loginClient()  {
        String payloadLogin = """
                    {
                        "username": "JDoe",
                        "password": "12345678"
                    }
                    """;
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/client/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }

    public String loginManager()  {
        String payloadLogin = """
                    {
                        "username": "pepper",
                        "password": "a"
                    }
                    """;
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/client/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }

    public void createVM() {
        String vMJson = """
                {
                    "_clazz": "applearch",
                    "entityId": {
                        "uuid": "7ab44a0b-8347-41cb-a64a-452666d0494a"
                    },
                    "ramSize": "4GB",
                    "cpunumber": 4
                }""";

        RestAssured.given()
                .header("Authorization", "Bearer " + loginManager())
                .contentType(ContentType.JSON)
                .body(vMJson)
                .when()
                .post("/vmachine")
                .then()
                .statusCode(201);
    }


    @Test
    public void testCreateRent() {
        //create client
        createClient();
        //create vm
        createVM();
        //create rent


        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rent");
        response.then().statusCode(201);

        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("JDoe"));
        assertThat(responseBody, containsString("7ab44a0b-8347-41cb-a64a-452666d0494a"));
        assertThat(responseBody, containsString("[2024,11,11,11,11]"));
    }

    @Test
    public void testRentRented(){
        //create client
        createClient();
        //create vm
        createVM();
        //create rent
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rent");
        response.then().statusCode(201);

        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("JDoe"));
        assertThat(responseBody, containsString("7ab44a0b-8347-41cb-a64a-452666d0494a"));
        assertThat(responseBody, containsString("[2024,11,11,11,11]"));
        //create rent of rented machine
        Response response2 = RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rent");
        response2.then().statusCode(409);

//        String responseBody2 = response2.getBody().asString();
//        assertThat(responseBody2, containsString("Request processing failed: java. lang. RuntimeException: VMachine already rented"));
    }
}
