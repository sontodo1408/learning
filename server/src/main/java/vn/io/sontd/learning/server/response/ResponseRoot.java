package vn.io.sontd.learning.server.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRoot {
    private Integer code;
    private ResponseBody payload;
    private String msg;
}
