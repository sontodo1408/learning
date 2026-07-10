package vn.io.sontd.learning.server.controller;

import vn.io.sontd.learning.server.constant.ResponseCode;
import vn.io.sontd.learning.server.response.ResponseBody;
import vn.io.sontd.learning.server.response.ResponseRoot;

/**
 * Base class for all REST controllers. Every controller should extend this
 * and use these helpers so every endpoint consistently returns a
 * {@link ResponseRoot} envelope instead of raw payloads.
 */
public abstract class BaseController {

    /**
     * Builds a success response carrying a data payload.
     *
     * @param data the payload to return; must extend {@link ResponseBody}
     * @return a {@link ResponseRoot} with {@link ResponseCode#SUCCESS} and the given payload
     */
    protected <T extends ResponseBody> ResponseRoot success(T data) {
        return ResponseRoot.builder().code(ResponseCode.SUCCESS).payload(data).build();
    }

    /**
     * Builds a success response carrying only a message (no data payload).
     *
     * @param msg the message to return
     * @return a {@link ResponseRoot} with {@link ResponseCode#SUCCESS} and the given message
     */
    protected ResponseRoot success(String msg) {
        return ResponseRoot.builder().code(ResponseCode.SUCCESS).msg(msg).build();
    }

    /**
     * Builds a business-error response carrying only a message.
     *
     * @param msg the error message to return
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and the given message
     */
    protected ResponseRoot fail(String msg) {
        return ResponseRoot.builder().code(ResponseCode.BUSINESS_ERROR).msg(msg).build();
    }

    /**
     * Builds a business-error response carrying a data payload (e.g. field-level validation errors).
     *
     * @param data the payload to return; must extend {@link ResponseBody}
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and the given payload
     */
    protected <T extends ResponseBody> ResponseRoot fail(T data) {
        return ResponseRoot.builder().code(ResponseCode.BUSINESS_ERROR).payload(data).build();
    }

    /**
     * Builds a business-error response carrying both a data payload and a message.
     *
     * @param data the payload to return; must extend {@link ResponseBody}
     * @param msg  the error message to return
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR}, the given payload and message
     */
    protected <T extends ResponseBody> ResponseRoot fail(T data, String msg) {
        return ResponseRoot.builder().code(ResponseCode.BUSINESS_ERROR).payload(data).msg(msg).build();
    }
}
