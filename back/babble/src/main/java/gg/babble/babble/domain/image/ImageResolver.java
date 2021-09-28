package gg.babble.babble.domain.image;

import gg.babble.babble.domain.FileName;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResolver {

    private static final String NEW_FILE_NAME_FORMAT = "%s-x%d.%s";
    private static final String DEFAULT_EXTENSION = "jpg";

    private final ImageFile imageFile;

    public ImageResolver(final ImageFile imageFile) {
        this.imageFile = imageFile;
    }

    public List<ImageFile> resizedImagesContaining(final List<Integer> maxPixels) {
        return maxPixels.stream()
            .map(maxPixel -> new ImageFile(resizedFileName(maxPixel), resizedAndCompressedImage(maxPixel)))
            .collect(Collectors.toList());
    }

    private byte[] resizedAndCompressedImage(final Integer pixel) {
        MutableImage mutableImage = MutableImage.of(imageFile.getData());
        ImageSize originalSize = ImageSize.of(mutableImage);
        ImageSize newSize = originalSize.calculateSizeContaining(pixel);

        return mutableImage.resize(newSize)
            .compress(DEFAULT_EXTENSION);
    }

    private FileName resizedFileName(final Integer maxPixels) {
        String fullName = String.format(NEW_FILE_NAME_FORMAT, imageFile.getName().getSimpleName(), maxPixels, imageFile.getName().getExtension());
        return FileName.of(fullName);
    }
}
