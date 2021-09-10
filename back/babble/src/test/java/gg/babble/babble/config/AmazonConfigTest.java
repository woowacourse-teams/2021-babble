package gg.babble.babble.config;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AmazonConfigTest {

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void accessBucketTest() {
        amazonS3.listObjectsV2("bucket-babble-front")
            .getObjectSummaries()
            .forEach(s3ObjectSummary -> System.out.println(s3ObjectSummary.getKey()));
    }
}
