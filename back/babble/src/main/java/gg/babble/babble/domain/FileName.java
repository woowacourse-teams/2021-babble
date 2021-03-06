package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileName {

    private static final char FILE_EXTENSION_DELIMITER = '.';
    private static final int SIMPLE_NAME_INDEX = 0;
    private static final int EXTENSION_INDEX = 1;
    private static final int SPLIT_SIZE = 2;

    private final String simpleName;
    private final String extension;

    public static FileName of(final String fullName) {
        int extensionDelimiterIndex = fullName.lastIndexOf(FILE_EXTENSION_DELIMITER);
        if (extensionDelimiterIndex == -1) {
            throw new BabbleIllegalArgumentException(String.format("올바른 파일 이름 형식이 아닙니다.(%s)", fullName));
        }

        String simpleName = fullName.substring(0, extensionDelimiterIndex);
        String extension = fullName.substring(extensionDelimiterIndex + 1);

        return new FileName(simpleName, extension);
    }

    @Override
    public String toString() {
        return simpleName + FILE_EXTENSION_DELIMITER + extension;
    }
}
