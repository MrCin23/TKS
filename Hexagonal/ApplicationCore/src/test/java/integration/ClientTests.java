package integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.data.DataInitializer;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClientTests {
    DataInitializer dataInitializer = new DataInitializer();
    String payloadJson = """
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
                }"""; //Trzeba było dodać _clazz i password

    @BeforeEach
//    public static void init() {
    public void initCollection() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/REST/api/client";
        dataInitializer.dropAndCreateClient();
        dataInitializer.initClient();
    }

//    @AfterEach
//    public void dropCollection() {
//        dataInitializer.dropAndCreateClient();
//        dataInitializer.initClient();

//    }

    public String loginClient()  {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201);
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
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }

    @Test
    public void testCreateClient()  {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("firstName", equalTo("John"))
                .body("surname", equalTo("Doe"))
                .body("username", equalTo("JDoe"))
                .body("emailAddress", equalTo("john.doe@example.com"));
    }


    @Test
    public void testGetAllClients() {
        String payloadLogin = """
                {
                    "username": "jp2gmd",
                    "password": "a"
                }
                """;
        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get()
                .then()
                .statusCode(200);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetClientByUUID() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // Przykładowy UUID

        RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .when()
                .get("/{uuid}", uuid)
                .then()
                .statusCode(200)
                .body("entityId.uuid", equalTo(uuid.toString()));
    }

//    @Test
//    public void testUpdateClient() {
//        Map<String, Object> fieldsToUpdate = new HashMap<>();
//        fieldsToUpdate.put("emailAddress", "new.email@example.com");
//        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//        RestAssured.given()
//                .header("Authorization", "Bearer " + loginClient())
//                .contentType(ContentType.JSON)
//                .body(fieldsToUpdate)
//                .when()
//                .put("/{uuid}", uuid)
//                .then()
//                .statusCode(204);
//        //TODO pamiętam, że coś trzeba tu jeszcze dodać, ale musisz mnie oświecić co
//    }

    @Test
    public void testDeactivateClient() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // Przykładowy UUID
        RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .when()
                .put("/deactivate/{uuid}", uuid)
                .then()
                .statusCode(204);
    }

    @Test
    public void testActivateClient() {

        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // Przykładowy UUID
        RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .when()
                .put("/activate/{uuid}", uuid)
                .then()
                .statusCode(204);
    }

    @Test
    public void testFindClientByUsername() {
        String username = "JDoe";
        RestAssured.given()
                .header("Authorization", "Bearer " + loginClient())
                .when()
                .get("/findClient/{username}", username)
                .then()
                .statusCode(200)
                .body("username", equalTo(username));
    }

    @Test
    public void testFindClientsByUsername() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201);
        String username = "JDoe";

        String payloadLogin = """
                {
                    "username": "JDoe",
                    "password": "12345678"
                }
                """;
        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/findClients/{username}", username)
                .then()
                .statusCode(200) //TODO zmienić, żeby przechodziło tylko dla admina
                .body("size()", greaterThan(0))
                .body("username", everyItem(equalTo(username)));
    }

    @Test
    public void testIncorrectCreateClient(){
        String payloadJson = """
                {
                  "firstName": "John",
                  "surname": "Doe",
                  "username": "johndoe",
                  "emailAddress": "john.doe@example.com",
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testDuplicateUUIDRejection() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("firstName", equalTo("John"))
                .body("surname", equalTo("Doe"))
                .body("username", equalTo("JDoe"))
                .body("emailAddress", equalTo("john.doe@example.com"));
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post();
        response.then().statusCode(409);

        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("User with id 123e4567-e89b-12d3-a456-426614174000 already exists"));
    }

    @Order(1)
    @Test
    public void testDuplicateUsernameRejection() {
        String payloadJson = """
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
                .body(payloadJson)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("firstName", equalTo("John"))
                .body("surname", equalTo("Doe"))
                .body("username", equalTo("JDoe"))
                .body("emailAddress", equalTo("john.doe@example.com"));


        payloadJson = """
                {
                    "entityId": {
                        "uuid": "123e4567-e89b-12d3-a456-426614174001"
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


        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post();
        response.then().statusCode(409);

        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("Write error: WriteError{code=11000, message='E11000 duplicate key error collection: vmrental.users index: username_1 dup key: { username: \"JDoe\" }', details={}}."
        ));
    }
}