package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.hamcrest.Matchers;
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

        final ClassLoader classLoader = getClass().getClassLoader();
        file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());
        s3Repository.save(IMAGE_FILE_NAME, Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        s3Repository.save("textFile.txt", "abc".getBytes(StandardCharsets.UTF_8));
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
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", Files.readAllBytes(file.toPath()));
        String filePath = "/img/new-file.jpg";
        final String encodeFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());

        mockMvc.perform(multipart("/api/images/" + encodeFilePath)
            .file(mockMultipartFile))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(hasSize(3)))
            .andExpect(jsonPath("$").value(Matchers.containsInAnyOrder("/img/new-file-x640.jpg", "/img/new-file-x1280.jpg", "/img/new-file-x1920.jpg")))
            .andDo(document("save-image",
                responseFields(
                    fieldWithPath("[]").description("파일 경로"))));

        mockMvc.perform(get("/api/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(hasSize(4)))
            .andExpect(jsonPath("$").value(Matchers.containsInAnyOrder("test.jpg", "/img/new-file-x640.jpg", "/img/new-file-x1280.jpg", "/img/new-file-x1920.jpg")));
    }
}
