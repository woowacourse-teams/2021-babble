package gg.babble.babble.restdocs.preprocessor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public class BigListPreprocessor implements OperationPreprocessor {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final OperationResponseFactory RESPONSE_FACTORY = new OperationResponseFactory();

    @Override
    public OperationRequest preprocess(final OperationRequest request) {
        return request;
    }

    @Override
    public OperationResponse preprocess(final OperationResponse response) {
        try {
            List<Map<String, Object>> content = MAPPER.readValue(response.getContent(), new TypeReference<List<Map<String, Object>>>() {
            });
            return RESPONSE_FACTORY.create(
                response.getStatus().value(),
                response.getHeaders(),
                MAPPER.writeValueAsBytes(Collections.singletonList(content.get(0)))
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("리스트로 변환할 수 없습니다.");
        }
    }
}
