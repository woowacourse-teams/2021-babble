package gg.babble.babble.domain.image;

import gg.babble.babble.exception.BabbleIOException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageResolver {

    public static final String DEFAULT_EXTENSION = "jpg";
    private static final float COMPRESSION_QUALITY = 1.0f;
    
    private final byte[] imageData;

    public ImageResolver(final byte[] imageData) {
        this.imageData = imageData;
    }

    public List<byte[]> resizedImagesContaining(final List<Integer> maxPixels) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {

            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            validateBufferedImage(bufferedImage);
            ImageSize originalSize = ImageSize.of(bufferedImage);

            return maxPixels.stream()
                .map(maxPixel -> calculateSizeContaining(originalSize, maxPixel))
                .map(newImageSize -> resizeImage(bufferedImage, newImageSize))
                .map(resizedImage -> compressImage(resizedImage, DEFAULT_EXTENSION))
                .collect(Collectors.toList());

        } catch (IOException ioException) {
            throw new BabbleIOException("이미지 입출력 중에 오류가 발생했습니다.");
        }
    }

    private void validateBufferedImage(final BufferedImage bufferedImage) {
        if (Objects.isNull(bufferedImage)) {
            throw new BabbleIllegalArgumentException("이미지 형식의 파일이 아닙니다.");
        }
    }

    private ImageSize calculateSizeContaining(final ImageSize originalSize, final Integer maxPixel) {
        int originalWidth = originalSize.getWidth();
        int originalHeight = originalSize.getHeight();

        double widthRatio = (double) maxPixel / originalWidth;
        double heightRatio = (double) maxPixel / originalHeight;
        double resultRatio = Math.min(widthRatio, heightRatio);

        return originalSize.multiply(resultRatio);
    }

    private BufferedImage resizeImage(final BufferedImage originalImage, final ImageSize imageSize) {
        Image resultingImage = originalImage.getScaledInstance(imageSize.getWidth(), imageSize.getHeight(), Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(imageSize.getWidth(), imageSize.getHeight(), BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private byte[] compressImage(final BufferedImage image, final String extension) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream)) {

            ImageWriter writer = getImageWriter(extension, imageOutputStream);
            ImageWriteParam param = getCompressionParam(writer);

            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException ioException) {
            throw new BabbleIOException("이미지 입출력 중에 오류가 발생했습니다.");
        }
    }

    private ImageWriter getImageWriter(final String extension, final ImageOutputStream imageOutputStream) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
        if (!writers.hasNext()) {
            throw new BabbleIllegalArgumentException(String.format("No writers found for (%s)", extension));
        }
        ImageWriter writer = writers.next();
        writer.setOutput(imageOutputStream);
        return writer;
    }

    private ImageWriteParam getCompressionParam(final ImageWriter writer) {
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(COMPRESSION_QUALITY);
        }
        return param;
    }
}
