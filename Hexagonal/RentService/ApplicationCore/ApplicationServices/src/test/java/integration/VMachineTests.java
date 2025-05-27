package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.repo.data.DataInitializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


public class VMachineTests {

    DataInitializer dataInitializer = new DataInitializer();


    @BeforeEach
    public void initCollection() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "";
        dataInitializer.dropAndCreateVMachine();
        dataInitializer.dropAndCreateClient();
        dataInitializer.initClient();
        dataInitializer.initVM();
    }

    @AfterEach
    public void dropCollection() {
        dataInitializer.dropAndCreateVMachine();
        dataInitializer.dropAndCreateClient();
        dataInitializer.initClient();
        dataInitializer.initVM();
    }

    public String loginClient() {
        String payloadLogin = """
        {
            "username": "JDoe",
            "password": "12345678"
        }
        """;

        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/User/api/client/login");

        if (loginResponse.statusCode() == 200) {
            return loginResponse.asString();
        }

        String payloadRegister = """
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
                .body(payloadRegister)
                .when()
                .post("/User/api/client")
                .then()
                .statusCode(201);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadLogin)
                .when()
                .post("/User/api/client/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }


    @Test
    public void testCreateVMachine() throws JsonProcessingException {
        String payloadJson = """
            {
                "ramSize": "4GB",
                "isRented": 0,
                "cpunumber": 4,
                "manufacturer": "AMD",
                "_clazz":"x86"
            }
            """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .post("/Rent/api/vmachine")
                .then()
                .statusCode(201)
                .body("_clazz", equalTo("x86"))
                .body("ramSize", equalTo("4GB"))
                .body("cpunumber", equalTo(4));
    }

    @Test
    public void testGetAllVMachines() {
        String payloadJson = """
                {
                    "_clazz": "x86",
                    "entityId": {
                        "uuid": "11111111-e89b-12d3-a456-426614174000"
                    },
                    "ramSize": "8GB",
                    "cpunumber": 2,
                    "isRented": 0,
                    "manufacturer": "AMD"
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .post("/Rent/api/vmachine")
                .then()
                .statusCode(201);

        RestAssured.given()
                .when()
                .get("/Rent/api/vmachine")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));

    }

//    @Test
//    public void testGetVMachineByUUID() {
//        String payloadJson = """
//                {
//                    "_clazz": "applearch",
//                    "entityId": {
//                        "uuid": "123e4567-e89b-12d3-a456-426614174000"
//                    },
//                    "ramSize": "8GB",
//                    "cpunumber": 2
//                }""";
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(payloadJson)
////                .header("Authorization", "Bearer " + loginClient())
//                .when()
//                .post("/Rent/api/vmachine")
//                .then()
//                .statusCode(201);
//
//        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//
//        RestAssured.given()
//                .header("Authorization", "Bearer " + loginClient())
//                .when()
//                .get("/Rent/api/vmachine/{uuid}", uuid)
//                .then()
//                .statusCode(200)
//                .body("entityId.uuid", equalTo(uuid.toString()));
//    }

    @Test
    public void testUpdateVMachine() {
        String payloadJson = """
                {
                    "_clazz": "applearch",
                    "entityId": {
                        "uuid": "123e4567-e89b-12d3-a456-426614174000"
                    },
                    "ramSize": "8GB",
                    "cpunumber": 2
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .post("/Rent/api/vmachine")
                .then()
                .statusCode(201);

        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("ramSize", "16GB");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(fieldsToUpdate)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .put("/Rent/api/vmachine/{uuid}", uuid)
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteVMachine() {
        String payloadJson = """
                {
                    "_clazz": "applearch",
                    "entityId": {
                        "uuid": "123e4567-e89b-12d3-a456-426614174000"
                    },
                    "ramSize": "8GB",
                    "cpunumber": 2
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .post("/Rent/api/vmachine")
                .then()
                .statusCode(201);

        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        RestAssured.given()
                .when()
                .delete("/Rent/api/vmachine/{uuid}", "123e4567-e89b-12d3-a456-426614174000")
                .then()
                .statusCode(204);
    }

    @Test
    public void testIncorrectCreateVM(){
        String payloadJson = """
                {
                    "entityId": {
                        "uuid": "22222222-434f-4b54-a81f-4b1d190e654d"
                    },
                    "ramSize": "4GB",
                    "isRented": 1,
                    "actualRentalPrice": 400.0,
                    "cpunumber": 0,
                    "_clazz":"applearch"
                }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
//                .header("Authorization", "Bearer " + loginClient())
                .when()
                .post("/Rent/api/vmachine")
                .then()
                .statusCode(400);
    }
}
