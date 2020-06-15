package com.chinatsp.dvrwlantransfer.http;

/**
 * @author chenzuohua
 * Created at 2020/5/29 16:20
 */
public class HttpCode {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;
    public static final int PARTIAL_CONTENT = 206;
    public static final int MULTI_STATUS = 207;

    public static final int REDIRECT = 301;
    public static final int FOUND = 302;
    public static final int REDIRECT_SEE_OTHER = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int TEMPORARY_REDIRECT = 307;

    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int CONFLICT = 409;
    public static final int GONE = 410;
    public static final int LENGTH_REQUIRED = 411;
    public static final int PRECONDITION_FAILED = 412;
    public static final int PAYLOAD_TOO_LARGE = 413;
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int RANGE_NOT_SATISFIABLE = 416;
    public static final int EXPECTATION_FAILED = 417;
    public static final int TOO_MANY_REQUESTS = 429;

    public static final int INTERNAL_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int UNSUPPORTED_HTTP_VERSION = 505;
}
