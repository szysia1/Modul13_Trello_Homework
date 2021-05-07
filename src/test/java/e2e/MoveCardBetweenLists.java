package e2e;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class MoveCardBetweenLists extends BaseTest {

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String cardId;

    @Order(1)
    @Test
    public void createNewBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "New board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        boardId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("New board");
    }

    @Order(2)
    @Test
    public void createFirstList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "First list")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        firstListId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("First list");

    }

    @Order(3)
    @Test
    public void createSecondList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Second list")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        secondListId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("Second list");
    }

    @Order(4)
    @Test
    public void addCardToFirstList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My card")
                .queryParam("idList", firstListId)
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        cardId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("My card");
    }

    @Order(5)
    @Test
    public void moveCardToSecondList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .pathParam("id", cardId)
                .when()
                .put(BASE_URL + CARDS + "/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        assertThat(json.getString("idList")).isEqualTo(secondListId);
    }

    @Order(6)
    @Test
    public void deleteBoard() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}

