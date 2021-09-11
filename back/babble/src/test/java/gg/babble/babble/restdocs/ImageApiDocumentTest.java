package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.restdocs.preprocessor.ImageBodyPreprocessor;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class ImageApiDocumentTest extends ApiDocumentTest {

    private static final String IMAGE_FILE_NAME = "test.jpg";

    private File file;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(webApplicationContext, restDocumentation);

        deleteAllImageFile();

        final ClassLoader classLoader = getClass().getClassLoader();
        file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());
        s3Repository.save(IMAGE_FILE_NAME, Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        s3Repository.save("textFile.txt", "abc".getBytes(StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        deleteAllImageFile();
    }

    private void deleteAllImageFile() {
        final Set<String> allImages = s3Repository.findAllImages();

        for (String image : allImages) {
            s3Repository.delete(image);
        }
    }

    @DisplayName("이미지 파일 조회 테스트")
    @Test
    void findAllImages() throws Exception {
        mockMvc.perform(get("/api/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value("test.jpg"))
            .andDo(document("read-images",
                responseFields(
                    fieldWithPath("[]").description("파일 경로"))));
    }

    @DisplayName("이미지 파일 저장 테스트")
    @Test
    void saveFile() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test-image.jpg", "image/jpg", Files.readAllBytes(file.toPath()));
        String filePath = "img/new-file.jpg";

        mockMvc.perform(multipart("/api/images")
            .file(multipartFile)
            .param("fileName", filePath))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(hasSize(3)))
            .andExpect(jsonPath("$").value(Matchers.containsInAnyOrder("img/new-file-x640.jpg", "img/new-file-x1280.jpg", "img/new-file-x1920.jpg")))
            .andDo(document("save-image",
                preprocessRequest(new ImageBodyPreprocessor()),
                responseFields(
                    fieldWithPath("[]").description("파일 경로"))));
    }

    @DisplayName("관리자 IP가 아닌 경우 이미지 저장 불가")
    @Test
    void saveFileUnauthorized() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", Files.readAllBytes(file.toPath()));
        String filePath = "img/new-file.jpg";

        mockMvc.perform(multipart("/api/images")
            .file(mockMultipartFile)
            .param("fileName", filePath))
            .andExpect(status().isUnauthorized());
    }
}
