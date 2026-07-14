package vn.io.sontd.learning.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.exception.BusinessException;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
    /** Shared mapper for {@link #parseJson}; a Jackson {@link ObjectMapper} is thread-safe for read operations. */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Integer toInt(Integer value, Integer defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    /**
     * Parses a JSON string into an instance of the given type. Handy for reading a JSON
     * payload that arrives as a raw string rather than a request body (e.g. a multipart part).
     *
     * @param json the JSON text to parse
     * @param type the target class to deserialize into
     * @param <T>  the target type
     * @return the deserialized object
     * @throws BusinessException if {@code json} can't be parsed into {@code type}
     */
    public static <T> T parseJson(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JacksonException ex) {
            throw new BusinessException(Message.INVALID_JSON, ex);
        }
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
