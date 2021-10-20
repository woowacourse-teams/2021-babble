package gg.babble.babble.domain.image;

import gg.babble.babble.domain.FileName;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResolver {

    private static final String NEW_FILE_NAME_FORMAT = "%s-x%d.%s";

    private final ImageFile imageFile;

    public ImageResolver(final ImageFile imageFile) {
        this.imageFile = imageFile;
    }

    public List<ImageFile> resizedImagesContaining(final List<Integer> maxPixels) {
        JpegImage jpegImage = JpegImage.of(imageFile.getData());

        return maxPixels.stream()
            .map(maxPixel -> new ImageFile(resizedFileName(maxPixel), resizedAndCompressedImage(jpegImage, maxPixel)))
            .collect(Collectors.toList());
    }

    private byte[] resizedAndCompressedImage(final JpegImage originalImage, final Integer pixel) {
        ImageSize originalSize = ImageSize.of(originalImage);
        ImageSize newSize = originalSize.calculateSizeContaining(pixel);

        return originalImage.resize(newSize)
            .compress();
    }

    private FileName resizedFileName(final Integer maxPixels) {
        String fullName = String.format(NEW_FILE_NAME_FORMAT, imageFile.getName().getSimpleName(), maxPixels, JpegImage.EXTENSION);
        return FileName.of(fullName);
    }
}
