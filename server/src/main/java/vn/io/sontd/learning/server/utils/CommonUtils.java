package vn.io.sontd.learning.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
    public static Integer toInt(Integer value, Integer defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    /**
     * Converts an arbitrary {@link Object} to an {@link Integer}: a {@link Number}
     * is narrowed via {@link Number#intValue()}, a {@link String} is parsed, and
     * anything else (including {@code null} or an unparsable string) falls back
     * to {@code defaultValue}.
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String stringValue) {
            try {
                return Integer.parseInt(stringValue.trim());
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Same as {@link #toInt(Object, Integer)}, defaulting to {@code null} when
     * {@code value} can't be converted.
     */
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }
}
