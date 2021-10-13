package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.UserResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserApiDocumentTest extends ApiDocumentTest {

    @DisplayName("유저를 생성한다.")
    @Test
    void createUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("nickname", "fortune");

        UserResponse response = given().body(body)
            .filter(document("create-user",
                requestFields(fieldWithPath("nickname").description("닉네임")),
                responseFields(fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("id").description("유저 Id"),
                    fieldWithPath("avatar").description("아바타 URL"))))
            .when().post("/api/users")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(UserResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getNickname()).isEqualTo("fortune");
        assertThat(response.getAvatar()).isEqualTo("https://d2bidcnq0n74fu.cloudfront.net/img/users/profiles/profile57.png");
    }

    public static UserResponse 유저가_생성_됨(final String nickname) {
        Map<String, Object> body = new HashMap<>();
        body.put("nickname", nickname);

        return given().body(body)
            .when().post("/api/users")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(UserResponse.class);
    }
}
