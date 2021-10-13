package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.restdocs.preprocessor.ImageBodyPreprocessor;
import io.restassured.RestAssured;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class ImageApiDocumentTest extends ApiDocumentTest {

    private static final String IMAGE_FILE_NAME = "test.jpg";

    private File file;
    private List<String> fileNames;

    @BeforeEach
    protected void setUp(final RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);

        localhost_관리자가_추가_됨();
        deleteAllImageFile();

        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());
        fileNames = 파일이_저장됨(IMAGE_FILE_NAME, file);
        localhost_관리자가_제거_됨();
    }

    private void deleteAllImageFile() {
        final List<String> allImages = 이미지_파일_조회_됨();

        for (String image : allImages) {
            파일이_삭제됨(image);
        }
    }

    private void 파일이_삭제됨(final String path) {
        RestAssured.given(specification)
            .param("fileName", path)
            .when().delete("/api/images")
            .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private List<String> 파일이_저장됨(final String path, final File file) {
        return RestAssured.given(specification)
            .header("content-type", "multipart/form-data")
            .multiPart("fileName", path)
            .multiPart("file", file)
            .when().post("/api/images")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", String.class);
    }

    private List<String> 이미지_파일_조회_됨() {
        return RestAssured.given(specification)
            .when().get("/api/images")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", String.class);
    }

    @DisplayName("이미지 파일 조회 테스트")
    @Test
    void findAllImages() throws Exception {
        List<String> responses = given().filter(document("read-images",
                responseFields(
                    fieldWithPath("[]").description("파일 경로"))))
            .when().get("/api/images")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", String.class);

        assertThat(responses).hasSameSizeAs(fileNames)
            .containsAll(fileNames);
    }

    @DisplayName("이미지 파일 저장 테스트")
    @Test
    void saveFile() {
        localhost_관리자가_추가_됨();
        String filePath = "img/new-file.jpg";

        List<String> responses = RestAssured.given(specification)
            .header("content-type", "multipart/form-data")
            .multiPart("fileName", filePath)
            .multiPart("file", file)
            .filter(document("create-image",
                preprocessRequest(new ImageBodyPreprocessor()),
                responseFields(
                    fieldWithPath("[]").description("파일 경로"))))
            .when().post("/api/images")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", String.class);

        assertThat(responses).hasSize(3).containsAll(Arrays.asList("img/new-file-x640.jpg", "img/new-file-x1280.jpg", "img/new-file-x1920.jpg"));
    }

    @DisplayName("관리자 IP가 아닌 경우 이미지 저장 불가")
    @Test
    void saveFileUnauthorized() throws Exception {
        String filePath = "img/new-file.jpg";

        RestAssured.given(specification)
            .header("content-type", "multipart/form-data")
            .multiPart("fileName", filePath)
            .multiPart("file", file)
            .when().post("/api/images")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
