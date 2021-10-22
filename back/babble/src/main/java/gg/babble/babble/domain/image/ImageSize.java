package gg.babble.babble.domain.image;

import java.util.Objects;

public class ImageSize {

    private final int width;
    private final int height;

    public ImageSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public static ImageSize of(final JpegImage image) {
        return new ImageSize(image.getWidth(), image.getHeight());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageSize multiply(final double ratio) {
        return new ImageSize((int) ((double) width * ratio), (int) ((double) height * ratio));
    }

    public ImageSize calculateSizeContaining(final Integer maxPixel) {
        double widthRatio = (double) maxPixel / width;
        double heightRatio = (double) maxPixel / height;
        double resultRatio = Math.min(widthRatio, heightRatio);

        return multiply(resultRatio);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageSize imageSize = (ImageSize) o;
        return width == imageSize.width && height == imageSize.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }
}
