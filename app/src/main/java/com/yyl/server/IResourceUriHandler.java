package com.yyl.server;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/6/006.
 */

public interface IResourceUriHandler {
    boolean accept(String uri);
    void postHandle(String uri, HttpContext httpContext) throws IOException;

}
