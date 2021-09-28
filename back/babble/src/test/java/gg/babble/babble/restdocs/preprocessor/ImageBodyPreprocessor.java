package gg.babble.babble.restdocs.preprocessor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.OperationRequestPartFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public class ImageBodyPreprocessor implements OperationPreprocessor {

    private static final String FILE = "file";
    private static final OperationRequestPartFactory PART_FACTORY = new OperationRequestPartFactory();
    private static final OperationRequestFactory REQUEST_FACTORY = new OperationRequestFactory();

    @Override
    public OperationRequest preprocess(final OperationRequest request) {
        List<OperationRequestPart> parts = new ArrayList<>();

        for (OperationRequestPart part : request.getParts()) {
            if (FILE.equals(part.getName())) {
                parts.add(PART_FACTORY.create(
                    part.getName(),
                    part.getSubmittedFileName(),
                    "<<binary data>>".getBytes(),
                    part.getHeaders()
                ));
                continue;
            }
            parts.add(part);
        }

        return REQUEST_FACTORY.create(
            request.getUri(),
            request.getMethod(),
            request.getContent(),
            request.getHeaders(),
            request.getParameters(),
            parts
        );
    }

    @Override
    public OperationResponse preprocess(final OperationResponse response) {
        return response;
    }
}
