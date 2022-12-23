package pl.lodz.p.it.opinioncollector.opinion;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OpinionControllerTest {

    private static final MessageFormat BASE_OPINIONS_URL = new MessageFormat("/products/{0}/opinions");
    private static final MessageFormat SPECIFIC_OPINION_URL = new MessageFormat("/products/{0}/opinions/{1}");
    private static final MessageFormat OPINION_REPORT_URL = new MessageFormat("/products/{0}/opinions/{1}/report");

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
              .body("[0].id.opinionId", equalTo("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "[0].id.productId", equalTo(PRODUCT_ID),
                    "[0].description", equalTo("Test desc 1"),
                    "[0].likesCounter", equalTo(0),
                    "[0].rate", equalTo(2),
                    "[0].pros", hasSize(2),
                    "[0].cons", hasSize(1),
                    "[0].author.id", equalTo("12208864-7b61-4e6e-8573-53863bd93b35"))
              .body("[1].id.opinionId", equalTo("dc0dac8a-797b-11ed-a1eb-0242ac120002"),
                    "[1].id.productId", equalTo(PRODUCT_ID),
                    "[1].description", equalTo("desc 2"),
                    "[1].likesCounter", equalTo(1),
                    "[1].rate", equalTo(3),
                    "[1].pros", empty(),
                    "[1].cons", empty(),
                    "[1].author.id", equalTo("66208864-7b61-4e6e-8573-53863bd93b35"));
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
              .body("id.opinionId", is("6c3a61be-955c-411b-9942-e746cfd0e75b"),
                    "id.productId", is(PRODUCT_ID),
                    "description", equalTo("Test desc 1"),
                    "likesCounter", equalTo(0),
                    "rate", equalTo(2),
                    "pros", hasSize(2),
                    "cons", hasSize(1),
                    "author.id", equalTo("12208864-7b61-4e6e-8573-53863bd93b35"));
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
               .body("id.productId", is(productId),
                     "rate", is(5),
                     "description", is("test desc 123"),
                     "pros.size()", is(2),
                     "pros.value", hasItems("p111", "p112"),
                     "cons.size()", is(3),
                     "cons.value", hasItems("c111", "c112", "c113"),
                     "author.email", is("user3"));
    }

    @Test
    @WithAnonymousUser
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
                     "author.email", is("user4"));
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
                     "author.email", is("user5"));
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
                     "author.email", is("user6"));
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
               .body("id.opinionId", is(opinionId),
                     "id.productId", is(PRODUCT_ID),
                     "rate", is(5),
                     "description", is("updated description 1"),
                     "pros.size()", is(2),
                     "cons.size()", is(3),
                     "pros.value", hasItems("updated p1", "updated p2"),
                     "cons.value", hasItems("updated c1", "updated c2", "updated c3"));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId),
                    "id.productId", is(PRODUCT_ID),
                    "rate", is(5),
                    "description", is("updated description 1"),
                    "pros.size()", is(2),
                    "cons.size()", is(3),
                    "pros.value", hasItems("updated p1", "updated p2"),
                    "cons.value", hasItems("updated c1", "updated c2", "updated c3"));
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
    @WithAnonymousUser
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
               .body("id.opinionId", is(opinionId),
                     "id.productId", is(PRODUCT_ID),
                     "rate", is(0),
                     "description", is("Updated description"),
                     "pros.size()", is(0),
                     "cons.size()", is(0));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId),
                    "id.productId", is(PRODUCT_ID),
                    "rate", is(0),
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
               .body("id.opinionId", is(opinionId),
                     "id.productId", is(PRODUCT_ID),
                     "rate", is(1),
                     "description", is("Update"),
                     "pros.size()", is(0),
                     "cons.size()", is(5));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId),
                    "id.productId", is(PRODUCT_ID),
                    "rate", is(1),
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
               .body("id.opinionId", is(opinionId),
                     "id.productId", is(PRODUCT_ID),
                     "rate", is(2),
                     "description", is("Updated description"),
                     "pros.size()", is(2),
                     "cons.size()", is(0));

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId),
                    "id.productId", is(PRODUCT_ID),
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
              .body("id.opinionId", is(opinionId));

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
              .body("id.opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId));
    }

    @Test
    void shouldDeleteOpinionFailWithStatusCode401WhenUserIsUnauthenticatedTest() {
        String opinionId = "dc0dac8a-797b-11ed-a1eb-0242ac120002";
        String url = SPECIFIC_OPINION_URL.format(new Object[] { PRODUCT_ID, opinionId });

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId));

        when().delete(url)
              .then()
              .status(HttpStatus.FORBIDDEN);

        when().get(url)
              .then()
              .status(HttpStatus.OK)
              .contentType(ContentType.JSON)
              .body("id.opinionId", is(opinionId));
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
              .body("id.opinionId", is(opinionId));

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
}
