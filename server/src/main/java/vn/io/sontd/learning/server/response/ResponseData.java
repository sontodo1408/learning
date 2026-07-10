package vn.io.sontd.learning.server.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Generic {@link ResponseBody} wrapper for payloads that don't have (or don't
 * need) their own dedicated response DTO, e.g. lists of entities.
 *
 * @param <T> the type of the wrapped data
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> extends ResponseBody {
    private T data;
}
