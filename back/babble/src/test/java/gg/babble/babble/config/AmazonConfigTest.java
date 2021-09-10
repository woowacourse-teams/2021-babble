package gg.babble.babble.config;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class AmazonConfigTest {

    @Autowired
    private AmazonS3 s3;

    @Test
    void accessBucketTest() {
        s3.listObjectsV2("bucket-babble-front").getObjectSummaries()
            .forEach(s3ObjectSummary -> System.out.println(s3ObjectSummary.getKey()));
    }
}
