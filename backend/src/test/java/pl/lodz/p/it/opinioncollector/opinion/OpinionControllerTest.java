package pl.lodz.p.it.opinioncollector.opinion;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.text.MessageFormat;
import java.util.List;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/test-data.sql")
class OpinionControllerTest {

    private static final MessageFormat BASE_OPINIONS_URL = new MessageFormat("/products/{0}/opinions");
    private static final MessageFormat SPECIFIC_OPINION_URL = new MessageFormat("/products/{0}/opinions/{1}");
    private static final MessageFormat OPINION_REPORT_URL = new MessageFormat("/products/{0}/opinions/{1}/report");
    private static final MessageFormat LIKE_OPINION_URL = new MessageFormat("/products/{0}/opinions/{1}/like");
    private static final MessageFormat DISLIKE_OPINION_URL = new MessageFormat("/products/{0}/opinions/{1}/dislike");

    private static final String PRODUCT_ID = "4811913c-b953-4856-979b-838488049d07";

    private final CreateOpinionDto dto = new CreateOpinionDto();

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    // region GET /products/{productId}/opinions
    @Test
    void shouldGetAllOpinionsWithStatusCode200Test() {
        String url = BASE_OPINIONS_URL.format(new Object[] { PRODUCT_ID });
        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("$.size()", equalTo(2))
              .body("[0].opinionId", equalTo("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "[0].productId", equalTo(PRODUCT_ID),
                    "[0].description", equalTo("Test desc 1"),
                    "[0].likesCounter", equalTo(1),
                    "[0].rate", equalTo(2),
                    "[0].pros", hasSize(2),
                    "[0].cons", hasSize(1),
                    "[0].authorName", equalTo("User1"),
                    "[0].liked", is(false),
                    "[0].disliked", is(false),
                    "[0].createdAt", not(empty()),
                    "[0].createdAt", not("null"))
              .body("[1].opinionId", equalTo("dc0dac8a-797b-11ed-a1eb-0242ac120002"),
                    "[1].productId", equalTo(PRODUCT_ID),
                    "[1].description", equalTo("desc 2"),
                    "[1].likesCounter", equalTo(0),
                    "[1].rate", equalTo(3),
                    "[1].pros", empty(),
                    "[1].cons", empty(),
                    "[1].authorName", equalTo("User"),
                    "[1].liked", is(false),
                    "[1].disliked", is(false),
                    "[0].createdAt", not(empty()),
                    "[0].createdAt", not("null"));
    }

    @Test
    @WithUserDetails("user2")
    void shouldGetAllWithStatusCode200WhenUserIsAuthenticatedAndHasReactionsTest() {
        String url = BASE_OPINIONS_URL.format(new Object[] { PRODUCT_ID });
        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("$.size()", equalTo(2))
              .body("[0].opinionId", equalTo("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "[0].productId", equalTo(PRODUCT_ID),
                    "[0].description", equalTo("Test desc 1"),
                    "[0].likesCounter", equalTo(1),
                    "[0].rate", equalTo(2),
                    "[0].pros", hasSize(2),
                    "[0].cons", hasSize(1),
                    "[0].authorName", equalTo("User1"),
                    "[0].liked", is(true),
                    "[0].disliked", is(false))
              .body("[1].opinionId", equalTo("dc0dac8a-797b-11ed-a1eb-0242ac120002"),
                    "[1].productId", equalTo(PRODUCT_ID),
                    "[1].description", equalTo("desc 2"),
                    "[1].likesCounter", equalTo(0),
                    "[1].rate", equalTo(3),
                    "[1].pros", empty(),
                    "[1].cons", empty(),
                    "[1].authorName", equalTo("User"),
                    "[1].liked", is(false),
                    "[1].disliked", is(false),
                    "[1].createdAt", not(empty()),
                    "[1].createdAt", not("null"));
    }

    @Test
    void shouldGetAllSuccessWithEmptyArrayAndStatusCode200WhenProductDoesNotExistTest() {
        String nonExistentProductId = "00000000-0000-4856-979b-838488049d07";
        String url = BASE_OPINIONS_URL.format(new Object[] { nonExistentProductId });
        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .body("", empty());
    }

    @Test
    void shouldGetAllSuccessWithEmptyArrayAndStatusCode200WhenProductHasNoOpinionsTest() {
        String productIdWithNoOpinions = "b4c7b393-2e26-49e2-9783-785583bd4c66";
        String url = BASE_OPINIONS_URL.format(new Object[] { productIdWithNoOpinions });
        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .body("", empty());
    }
    // endregion

    // region GET /products/{productId}/opinions/{opinionId}
    @Test
    void shouldGetOneWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .body("opinionId", is("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "productId", is(PRODUCT_ID),
                    "description", equalTo("Test desc 1"),
                    "likesCounter", equalTo(1),
                    "rate", equalTo(2),
                    "pros", hasSize(2),
                    "cons", hasSize(1),
                    "authorName", equalTo("User1"),
                    "createdAt", not(empty()),
                    "createdAt", not("null"),
                    "liked", is(false),
                    "disliked", is(false));
    }

    @Test
    @WithUserDetails("user15")
    void shouldGetOneWithStatusCode200WhenUserIsAuthenticatedAndHasLikeTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .body("opinionId", is("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "productId", is(PRODUCT_ID),
                    "description", equalTo("Test desc 1"),
                    "likesCounter", equalTo(1),
                    "rate", equalTo(2),
                    "pros", hasSize(2),
                    "cons", hasSize(1),
                    "authorName", equalTo("User1"),
                    "createdAt", not(empty()),
                    "createdAt", not("null"),
                    "liked", is(true),
                    "disliked", is(false));
    }

    @Test
    @WithUserDetails("user10")
    void shouldGetOneWithStatusCode200WhenUserIsAuthenticatedAndHasDislikeTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .body("opinionId", is("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "productId", is(PRODUCT_ID),
                    "description", equalTo("Test desc 1"),
                    "likesCounter", equalTo(1),
                    "rate", equalTo(2),
                    "pros", hasSize(2),
                    "cons", hasSize(1),
                    "authorName", equalTo("User1"),
                    "createdAt", not(empty()),
                    "createdAt", not("null"),
                    "liked", is(false),
                    "disliked", is(true));
    }

    @Test
    void shouldGetOneFailWithStatusCode404WhenOpinionDoesNotExistTest() {
        String opinionId = "00000000-0000-4856-979b-838488049d07";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.NOT_FOUND)
              .body(is(""));
    }
    // endregion

    // region POST /products/{productId}/opinions
    @Test
    @WithUserDetails("user3")
    void shouldCreateOpinionSuccessWithStatusCode201Test() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(5);
        dto.setDescription("test desc 123");
        dto.setPros(List.of("p111", "p112"));
        dto.setCons(List.of("c111", "c112", "c113"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.CREATED)
               .body("productId", is(productId),
                     "opinionId", not(empty()),
                     "rate", is(5),
                     "description", is("test desc 123"),
                     "likesCounter", is(0),
                     "liked", is(false),
                     "disliked", is(false),
                     "pros.size()", is(2),
                     "pros.value", hasItems("p111", "p112"),
                     "cons.size()", is(3),
                     "cons.value", hasItems("c111", "c112", "c113"),
                     "authorName", is("User3"),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));
    }

    @Test
    void shouldCreateOpinionFailWithStatusCode403WhenUserIsUnauthenticatedTest() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(5);
        dto.setDescription("test desc 123");
        dto.setPros(List.of("p111", "p112"));
        dto.setCons(List.of("c111", "c112", "c113"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails("user4")
    void shouldCreateOpinionWithoutProsAndConsWithStatusCode201Test() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(6);
        dto.setDescription("test 321");

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.CREATED)
               .body("rate", is(6),
                     "description", is("test 321"),
                     "pros.size()", is(0),
                     "cons.size()", is(0),
                     "liked", is(false),
                     "disliked", is(false),
                     "authorName", is("User4"),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));
    }

    @Test
    @WithUserDetails("user5")
    void shouldCreateOpinionWithoutProsWithStatusCode201Test() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(1);
        dto.setDescription("1234");
        dto.setCons(List.of("c1234", "c12345", "c123456"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.CREATED)
               .body("rate", is(1),
                     "description", is("1234"),
                     "pros.size()", is(0),
                     "cons.size()", is(3),
                     "liked", is(false),
                     "disliked", is(false),
                     "authorName", is("User5"),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));
    }

    @Test
    @WithUserDetails("user6")
    void shouldCreateOpinionWithoutConsWithStatusCode201Test() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(3);
        dto.setDescription("ddd");
        dto.setPros(List.of("p1234", "p12345", "p123456", "p1234567"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.CREATED)
               .body("rate", is(3),
                     "description", is("ddd"),
                     "pros.size()", is(4),
                     "cons.size()", is(0),
                     "authorName", is("User6"),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));
    }

    @Test
    @WithUserDetails("user4")
    void shouldCreateOpinionFailWithStatusCode400WhenRateIsNegativeTest() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(-1);
        dto.setDescription("ddd");
        dto.setPros(List.of("p1234", "p12345", "p123456", "p1234567"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user5")
    void shouldCreateOpinionFailWithStatusCode400WhenRateIsGreaterThan10Test() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(11);
        dto.setDescription("ddd");

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user3")
    void shouldCreateOpinionFailWithStatusCode400WhenDescriptionIsEmptyTest() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(5);
        dto.setDescription("");
        dto.setPros(List.of("p111", "p112"));
        dto.setCons(List.of("c111", "c112", "c113"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user3")
    void shouldCreateOpinionFailWithStatusCode400WhenDescriptionIsBlankTest() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(5);
        dto.setDescription(" ");
        dto.setPros(List.of("p111", "p112"));
        dto.setCons(List.of("c111", "c112", "c113"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("admin")
    void shouldCreateOpinionFailWithStatusCode403WhenUserIsAdmin() {
        String productId = "4950d349-1127-4690-82a0-94fdc81b019b";
        String url = BASE_OPINIONS_URL.format(new Object[] { productId });

        dto.setRate(5);
        dto.setDescription("d");
        dto.setPros(List.of("p111", "p112"));
        dto.setCons(List.of("c111", "c112", "c113"));

        given().body(dto)
               .contentType(MediaType.APPLICATION_JSON)
               .when()
               .post(url)
               .then()
               .status(HttpStatus.FORBIDDEN);
    }
    // endregion

    // region PUT /products/{productId}/opinions/{opinionId}
    @Test
    @WithUserDetails("user")
    void shouldUpdateOpinionWithStatusCode200Test() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(5);
        dto.setDescription("updated description 1");
        dto.setPros(List.of("updated p1", "updated p2"));
        dto.setCons(List.of("updated c1", "updated c2", "updated c3"));

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.OK)
               .contentType(ContentType.JSON)
               .body("opinionId", is(opinionId),
                     "productId", is(PRODUCT_ID),
                     "rate", is(5),
                     "description", is("updated description 1"),
                     "pros.size()", is(2),
                     "cons.size()", is(3),
                     "liked", is(false),
                     "disliked", is(false),
                     "pros.value", hasItems("updated p1", "updated p2"),
                     "cons.value", hasItems("updated c1", "updated c2", "updated c3"),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "rate", is(5),
                    "description", is("updated description 1"),
                    "pros.size()", is(2),
                    "cons.size()", is(3),
                    "liked", is(false),
                    "disliked", is(false),
                    "pros.value", hasItems("updated p1", "updated p2"),
                    "cons.value", hasItems("updated c1", "updated c2", "updated c3"),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));
    }

    @Test
    @WithUserDetails("user2")
    void shouldUpdateOpinionFailWithStatusCode403WhenUserIsNotAuthorTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(5);
        dto.setDescription("updated description 1");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldUpdateOpinionFailWithStatusCode401WhenUserIsNotAuthenticatedTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(5);
        dto.setDescription("updated description 1");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails("user")
    void shouldUpdateOpinionFailWithStatusCode400WhenNewRateIsNegativeTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(-1);
        dto.setDescription("updated description 1");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user")
    void shouldUpdateOpinionFailWithStatusCode400WhenNewRateIsGreaterThan10Test() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(11);
        dto.setDescription("updated description 1");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user")
    void shouldUpdateOpinionFailWithStatusCode400WhenNewDescriptionIsBlankTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(6);
        dto.setDescription("  ");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user")
    void shouldUpdateOpinionFailWithStatusCode400WhenDescriptionIsEmptyTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(6);
        dto.setDescription("");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user1")
    void shouldUpdateOpinionsWithoutProsNorConsWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(0);
        dto.setDescription("Updated description");

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.OK)
               .contentType(ContentType.JSON)
               .body("opinionId", is(opinionId),
                     "productId", is(PRODUCT_ID),
                     "rate", is(0),
                     "liked", is(false),
                     "disliked", is(false),
                     "description", is("Updated description"),
                     "pros.size()", is(0),
                     "cons.size()", is(0),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "rate", is(0),
                    "liked", is(false),
                    "disliked", is(false),
                    "description", is("Updated description"),
                    "pros.size()", is(0),
                    "cons.size()", is(0));
    }

    @Test
    @WithUserDetails("user1")
    void shouldUpdateOpinionsWithoutProsWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(1);
        dto.setDescription("Update");
        dto.setCons(List.of("c1", "c2", "c3", "c4", "c5"));

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.OK)
               .contentType(ContentType.JSON)
               .body("opinionId", is(opinionId),
                     "productId", is(PRODUCT_ID),
                     "rate", is(1),
                     "liked", is(false),
                     "disliked", is(false),
                     "description", is("Update"),
                     "pros.size()", is(0),
                     "cons.size()", is(5),
                     "createdAt", not(empty()),
                     "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "rate", is(1),
                    "liked", is(false),
                    "disliked", is(false),
                    "description", is("Update"),
                    "pros.size()", is(0),
                    "cons.size()", is(5));
    }

    @Test
    @WithUserDetails("user1")
    void shouldUpdateOpinionsWithoutConsWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(2);
        dto.setDescription("Updated description");
        dto.setPros(List.of("p1", "p2"));

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.OK)
               .contentType(ContentType.JSON)
               .body("opinionId", is(opinionId),
                     "productId", is(PRODUCT_ID),
                     "rate", is(2),
                     "description", is("Updated description"),
                     "pros.size()", is(2),
                     "cons.size()", is(0));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "rate", is(2),
                    "description", is("Updated description"),
                    "pros.size()", is(2),
                    "cons.size()", is(0));
    }

    @Test
    @WithUserDetails("user1")
    void shouldUpdateOpinionsFailWithStatusCode400WhenAnyOfProsIsBlankTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(2);
        dto.setDescription("Updated description");
        dto.setPros(List.of("p1", ""));

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user1")
    void shouldUpdateOpinionsFailWithStatusCode400WhenAnyOfConsIsBlankTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        dto.setRate(2);
        dto.setDescription("Updated description");
        dto.setCons(List.of(" ", "c1", "c2"));

        given().contentType(MediaType.APPLICATION_JSON)
               .body(dto)
               .when()
               .put(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }
    // endregion

    // region DELETE /products/{productId}/opinions/{opinionId}
    @Test
    @WithUserDetails("user")
    void shouldDeleteOpinionWithStatusCode204WhenUserIsAuthorTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.NO_CONTENT);

        when().get(url)
              .then()
              .status(HttpStatus.NOT_FOUND)
              .body(is(""));
    }

    @Test
    @WithUserDetails("user10")
    void shouldDeleteOpinionFailWithStatusCode403WhenUserIsNotAuthorTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));
    }

    @Test
    void shouldDeleteOpinionFailWithStatusCode401WhenUserIsUnauthenticatedTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));
    }

    @Test
    @WithUserDetails("admin")
    void shouldDeleteOpinionWithStatusCode204WhenUserIsAdminTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.NO_CONTENT);

        when().get(url)
              .then()
              .status(HttpStatus.NOT_FOUND)
              .body(is(""));
    }
    // endregion

    // region POST /products/{productId}/opinions/{opinionId}/report
    @Test
    @WithUserDetails("user8")
    void shouldReportOpinionWithStatusCode202AsUserTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = OPINION_REPORT_URL.format(new Object[] { PRODUCT_ID, opinionId });

        given().contentType(MediaType.TEXT_PLAIN)
               .body("reason 54321")
               .when()
               .post(url)
               .then()
               .status(HttpStatus.ACCEPTED)
               .body(is(""));
    }

    @Test
    @WithUserDetails("user9")
    void shouldReportOpinionFailWithStatus400WhenReasonIsBlankTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = OPINION_REPORT_URL.format(new Object[] { PRODUCT_ID, opinionId });

        given().contentType(MediaType.TEXT_PLAIN)
               .body("")
               .when()
               .post(url)
               .then()
               .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    @WithUserDetails("user10")
    void shouldReportOpinionFailWithStatus404WhenOpinionDoesNotExistTest() {
        String opinionId = "00000000-0000-4856-979b-838488049d07";
        String url = OPINION_REPORT_URL.format(new Object[] { PRODUCT_ID, opinionId });

        given().contentType(MediaType.TEXT_PLAIN)
               .body("rea5on")
               .when()
               .post(url)
               .then()
               .status(HttpStatus.NOT_FOUND);
    }

    @Test
    @WithUserDetails("admin")
    void shouldReportOpinionFailWithStatusCode403AsAdminTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        String url = OPINION_REPORT_URL.format(new Object[] { PRODUCT_ID, opinionId });

        given().contentType(MediaType.TEXT_PLAIN)
               .body("abc")
               .when()
               .post(url)
               .then()
               .status(HttpStatus.FORBIDDEN);
    }
    // endregion

    // region PUT /products/{productId}/opinions/{opinionId}/like
    @Test
    @WithUserDetails("user1")
    void shouldLikeOpinionWithStatusCode200WhenUserIsAuthenticatedTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(1));

        when().put(likeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(2),
                    "liked", is(true),
                    "disliked", is(false),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(2),
                    "liked", is(true),
                    "disliked", is(false));

        // liking again should not change like counter
        when().put(likeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(2),
                    "liked", is(true),
                    "disliked", is(false),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(2),
                    "liked", is(true),
                    "disliked", is(false));
    }

    @Test
    void shouldLikeOpinionFailWithStatusCode403WhenUserIsUnauthenticatedTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().put(likeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails("admin")
    void shouldLikeOpinionFailWithStatusCode403WhenUserIsAdminTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().put(likeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithUserDetails("user10")
    void shouldChangeReactionToLikeFromDislikeWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(1));

        when().put(likeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(true),
                    "disliked", is(false),
                    "likesCounter", is(3),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(true),
                    "disliked", is(false),
                    "likesCounter", is(3));
    }
    // endregion

    // region PUT /products/{productId}/opinions/{opinionId}/dislike
    @WithUserDetails("user1")
    @Test
    void shouldDislikeOpinionWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(1));

        when().put(dislikeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(0),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(0));

        // disliking again should not change like counter
        when().put(dislikeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(0),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(0));
    }

    @Test
    void shouldDislikeOpinionFailWithStatusCode403WhenUserIsUnauthenticatedTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(1));

        when().put(dislikeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(1));
    }

    @Test
    @WithUserDetails("admin")
    void shouldDislikeOpinionFailWithStatusCode403WhenUserIsAdminTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));

        when().put(dislikeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));
    }

    @WithUserDetails("user15")
    @Test
    void shouldChangeReactionToDislikeWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(true),
                    "disliked", is(false),
                    "likesCounter", is(1));

        when().put(dislikeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(-1),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(-1));
    }
    // endregion

    // region DELETE /products/{productId}/opinions/{opinionId}/like
    @Test
    @WithUserDetails("user2")
    void shouldRemoveLikeWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(true),
                    "disliked", is(false),
                    "likesCounter", is(1));

        when().delete(likeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(0),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(0));
    }

    @Test
    void shouldRemoveLikeFailWithStatusCode403WhenUserIsUnauthenticatedTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));

        when().delete(likeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));
    }

    @Test
    @WithUserDetails("admin")
    void shouldRemoveLikeFailWithStatusCode403WhenUserIsAdminTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String likeUrl = LIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));

        when().delete(likeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));
    }
    // endregion

    // region DELETE /products/{productId}/opinions/{opinionId}/dislike
    @Test
    @WithUserDetails("user10")
    void shouldRemoveDislikeWithStatusCode200Test() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(true),
                    "likesCounter", is(1));

        when().delete(dislikeUrl)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(2),
                    "createdAt", not(empty()),
                    "createdAt", not("null"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "liked", is(false),
                    "disliked", is(false),
                    "likesCounter", is(2));
    }

    @Test
    void shouldRemoveDislikeFailWithStatusCode403WhenUserIsUnauthenticatedTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));

        when().delete(dislikeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));
    }

    @Test
    @WithUserDetails("admin")
    void shouldRemoveDislikeFailWithStatusCode403WhenUserIsAdminTest() {
        String opinionId = "6c3a61be-955c-411b-9942-e746cfd0e75b";
        var formatObject = new Object[] { PRODUCT_ID, opinionId };
        String url = SPECIFIC_OPINION_URL.format(formatObject);
        String dislikeUrl = DISLIKE_OPINION_URL.format(formatObject);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));

        when().delete(dislikeUrl)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("opinionId", is(opinionId),
                    "productId", is(PRODUCT_ID),
                    "likesCounter", is(1));
    }
    // endregion
}
