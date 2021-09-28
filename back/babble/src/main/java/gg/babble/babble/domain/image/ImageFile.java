package gg.babble.babble.domain.image;

import gg.babble.babble.domain.FileName;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFile {

    private final FileName name;
    private final byte[] data;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageFile imageFile = (ImageFile) o;
        return Objects.equals(name, imageFile.name) && Arrays.equals(data, imageFile.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
