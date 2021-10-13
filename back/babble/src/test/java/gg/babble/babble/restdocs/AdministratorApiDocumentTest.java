package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.AdministratorResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class AdministratorApiDocumentTest extends AcceptanceTest {

    @BeforeEach
    protected void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);
    }

    @DisplayName("관리자 전체 조회한다.")
    @Test
    void findAllAdministrator() {
        localhost_관리자가_추가_됨();
        List<AdministratorResponse> response = given()
            .filter(document("read-administrators",
                responseFields(
                    fieldWithPath("[].id").description("관리자 ID"),
                    fieldWithPath("[].ip").description("관리자 IP 주소"),
                    fieldWithPath("[].name").description("관리자 이름"))))
            .when().get("/api/admins")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", AdministratorResponse.class);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isNotNull();
        assertThat(response.get(0).getIp()).isEqualTo("127.0.0.1");
        assertThat(response.get(0).getName()).isEqualTo("localhost");
    }

    @DisplayName("관리자 전체 조회한다.(X-Forwarded-For)")
    @Test
    void findAllAdministratorWithXForwardedForHeader() {
        localhost_관리자가_추가_됨();
        List<AdministratorResponse> response = given()
            .header("X-Forwarded-For", "127.0.0.1")
            .filter(document("read-administrators",
                responseFields(
                    fieldWithPath("[].id").description("관리자 ID"),
                    fieldWithPath("[].ip").description("관리자 IP 주소"),
                    fieldWithPath("[].name").description("관리자 이름"))))
            .when().get("/api/admins")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", AdministratorResponse.class);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isNotNull();
        assertThat(response.get(0).getIp()).isEqualTo("127.0.0.1");
        assertThat(response.get(0).getName()).isEqualTo("localhost");
    }

    @DisplayName("등록되지 않는 유저는 관리자 전체 조회를 할 수 없다.(X-Forwarded-For)")
    @Test
    void findAllAdministratorWithInvalidIp() {
        localhost_관리자가_추가_됨();
        given().header("X-Forwarded-For", "127.0.0.2")
            .when().get("/api/admins")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("관리자 IP를 추가한다.")
    @Test
    void createAdministrator() {
        localhost_관리자가_추가_됨();
        String ip = "111.111.11.11";
        String name = "포츈집";

        Map<String, Object> body = new HashMap<>();
        body.put("ip", ip);
        body.put("name", name);

        AdministratorResponse response = given()
            .body(body)
            .filter(document("create-administrator",
                requestFields(
                    fieldWithPath("ip").description("관리자 IP 주소"),
                    fieldWithPath("name").description("관리자 이름")),
                responseFields(
                    fieldWithPath("id").description("관리자 ID"),
                    fieldWithPath("ip").description("관리자 IP 주소"),
                    fieldWithPath("name").description("관리자 이름"))))
            .when().post("/api/admins")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(AdministratorResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getIp()).isEqualTo(ip);
        assertThat(response.getName()).isEqualTo(name);

        List<AdministratorResponse> administrators = 관리자_전체_조회_됨();
        assertThat(administrators).hasSize(2);
    }

    @DisplayName("관리자 IP를 삭제한다.")
    @Test
    void deleteAdministrator() {
        localhost_관리자가_추가_됨();
        Long idToDelete = 관리자_전체_조회_됨().get(0).getId();

        given()
            .filter(document("delete-administrator"))
            .when().delete("/api/admins/{idToDelete}", idToDelete)
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        관리자_전체_조회_시_인증_실패();
    }

    private List<AdministratorResponse> 관리자_전체_조회_됨() {
        return given()
            .when().get("/api/admins")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", AdministratorResponse.class);
    }

    private void 관리자_전체_조회_시_인증_실패() {
        given()
            .when().get("/api/admins")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
