package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest extends Credentials {
    protected static final String KEY = Credentials.yourKey;
    protected static final String TOKEN = Credentials.yourToken;
    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String ORGANIZATIONS = "organizations";
    protected static final String BOARDS = "boards";
    protected static final String CARDS = "cards";
    protected static final String LISTS = "lists";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
    }
}
