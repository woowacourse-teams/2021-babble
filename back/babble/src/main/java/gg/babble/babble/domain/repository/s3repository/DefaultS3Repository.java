package gg.babble.babble.domain.repository.s3repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import gg.babble.babble.exception.BabbleIOException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("!local")
@Repository
public class DefaultS3Repository extends AbstractS3Repository {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    public DefaultS3Repository(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public List<String> findAllImages() {
        return amazonS3.listObjectsV2(bucketName)
            .getObjectSummaries()
            .stream()
            .map(S3ObjectSummary::getKey)
            .filter(this::isImageFile)
            .collect(Collectors.toList());
    }

    public void save(final String fileName, final byte[] content) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(content.length);
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(fileName));

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content)) {
            amazonS3.putObject(bucketName, fileName, byteArrayInputStream, objectMetadata);
        } catch (IOException ioException) {
            throw new BabbleIOException(String.format("파일을 S3에 저장에 실패했습니다. (%s)", fileName));
        }
    }

    public void delete(final String... fileNames) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(fileNames);
        amazonS3.deleteObjects(deleteObjectsRequest);
    }
}
