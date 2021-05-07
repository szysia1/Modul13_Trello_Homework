package organizations;

import base.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class Organization extends BaseTest {

    private static Faker faker;
    private String organizationId;
    private String fakeDesc;
    private String fakeName;
    private String fakeWebsiteHttp;
    private String fakeWebsiteHttps;
    private String fakeDisplayName;
    private String fakeRandomWord;

    public void deleteOrganization() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @BeforeEach
    public void beforeEach() {
        faker = new Faker();
        fakeDesc = faker.lorem().sentence();
        fakeName = faker.internet().password(3, 15, false, false);
        fakeWebsiteHttp = "http://" + faker.animal().name() + ".com";
        fakeWebsiteHttps = "https://" + faker.animal().name() + ".com";
        fakeDisplayName = faker.company().name();
        fakeRandomWord = faker.lorem().word();
    }

    @Test
    public void createNewOrganizationWithRequiredParams() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", fakeDisplayName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(fakeDisplayName);

        organizationId = json.getString("id");
        deleteOrganization();
    }

    @Test
    public void createNewOrganizationWithAllParams() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", fakeDisplayName)
                .queryParam("desc", fakeDesc)
                .queryParam("name", fakeName)
                .queryParam("website", fakeWebsiteHttps)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(fakeDisplayName);
        assertThat(json.getString("desc")).isEqualTo(fakeDesc);
        assertThat(json.getString("name")).isEqualTo(fakeName);
        assertThat(json.getString("website")).isEqualTo(fakeWebsiteHttps);

        organizationId = json.getString("id");
        deleteOrganization();
    }

    @Test
    public void createNewOrganizationWithoutDisplayName() {
        given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void createOrganizationWithIncorrectName() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", fakeDisplayName)
                .queryParam("name", "N!")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(fakeDisplayName);
        assertThat(json.getString("name")).isNotEqualTo("N!");

        String name = json.getString("name");
        System.out.println(name);

        organizationId = json.getString("id");
        deleteOrganization();
    }

    @Test
    public void createOrganizationHttpWebsiteAddress() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", fakeDisplayName)
                .queryParam("website", fakeWebsiteHttp)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(fakeDisplayName);
        assertThat(json.getString("website")).isEqualTo(fakeWebsiteHttp);

        organizationId = json.getString("id");
        deleteOrganization();
    }

    @Test
    public void createOrganizationWithIncorrectWebsiteAddress() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", fakeDisplayName)
                .queryParam("website", fakeRandomWord)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(fakeDisplayName);
        assertThat(json.getString("website")).isEqualTo("http://" + fakeRandomWord);

        organizationId = json.getString("id");
        deleteOrganization();
    }


}
