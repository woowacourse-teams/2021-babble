package gg.babble.babble.service;


import gg.babble.babble.domain.FileName;
import gg.babble.babble.domain.image.ImageResolver;
import gg.babble.babble.domain.repository.S3Repository;
import gg.babble.babble.exception.BabbleIOException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private static final List<Integer> SAVED_SIZE = Arrays.asList(1920, 1280, 640);
    private static final String NEW_FILE_NAME_FORMAT = "%s-x%d.%s";

    private final S3Repository s3Repository;

    public ImageService(final S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    public Set<String> findAllImages() {
        return s3Repository.findAllImages();
    }

    public Set<String> saveImage(final MultipartFile file, final String fullFilename) {
        try {
            ImageResolver imageResolver = new ImageResolver(file.getBytes());
            FileName fileName = FileName.of(fullFilename);
            return resizeAndSaveImage(imageResolver, fileName);
        } catch (IOException ioException) {
            throw new BabbleIOException(String.format("파일 읽기에 실패했습니다. (%s)", fullFilename));
        }
    }

    private Set<String> resizeAndSaveImage(final ImageResolver imageResolver, final FileName fileName) {
        List<byte[]> images = imageResolver.resizedImagesContaining(SAVED_SIZE);
        Set<String> savedImages = new HashSet<>();

        for (int imageIndex = 0; imageIndex < SAVED_SIZE.size(); imageIndex++) {
            final String newFileName = String
                .format(NEW_FILE_NAME_FORMAT, fileName.getSimpleName(), SAVED_SIZE.get(imageIndex), ImageResolver.DEFAULT_EXTENSION);

            s3Repository.save(newFileName, images.get(imageIndex));
            savedImages.add(newFileName);
        }

        return savedImages;
    }
}
