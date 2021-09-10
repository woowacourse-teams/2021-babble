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

    private final S3Repository s3Repository;

    public ImageService(final S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    public Set<String> findAllImages() {
        return s3Repository.findAllImages();
    }

    public Set<String> saveImage(final MultipartFile file, final String fileFullName) {
        try {
            ImageResolver imageResolver = new ImageResolver(file.getBytes());
            FileName fileName = FileName.of(fileFullName);
            return resizeAndSaveImage(imageResolver, fileName);
        } catch (IOException ioException) {
            throw new BabbleIOException(String.format("파일 읽기에 실패했습니다. (%s)", fileFullName));
        }
    }

    private Set<String> resizeAndSaveImage(final ImageResolver imageResolver, final FileName fileName) {
        List<byte[]> images = imageResolver.resizedImagesContaining(SAVED_SIZE);
        Set<String> savedImages = new HashSet<>();

        for (byte[] image : images) {
            final String newFileName = fileName.getSimpleName() + "-x" + ImageResolver.DEFAULT_EXTENSION;

            s3Repository.save(newFileName, image);
            savedImages.add(newFileName);
        }

        return savedImages;
    }
}
