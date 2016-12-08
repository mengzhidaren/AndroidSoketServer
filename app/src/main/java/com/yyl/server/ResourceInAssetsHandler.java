package com.yyl.server;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/12/6/006.
 * <p>
 * http://127.0.0.1:8088/static/jd.html
 */

public class ResourceInAssetsHandler implements IResourceUriHandler {
    private String acceptPrefix = "/static/";
    private Context context;

    public ResourceInAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    /**
     * 测试
     *
     * @param uri
     * @param httpContext
     * @throws IOException
     */
    public void postHandle1(String uri, HttpContext httpContext) throws IOException {
        Log.i("ResourceInAssetsHandler", uri);
        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println();
        printWriter.println("from result handle");
        printWriter.flush();
    }

    @Override
    public void postHandle(String uri, HttpContext httpContext) throws IOException {
        int startIndex = acceptPrefix.length();
        String assetsPath = uri.substring(startIndex);
        Log.i("assetsPath", assetsPath);
        InputStream inputStream = context.getAssets().open(assetsPath);
        byte[] raw = StreamTools.readRawFromStream(inputStream);
        Log.i("assetsPath", "raw=" + raw.length);
        inputStream.close();
        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printWriter = new PrintStream(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Length:" + raw.length);

        if (assetsPath.endsWith(".html")) {
            printWriter.println("Content-Type:text/html");
        } else if (assetsPath.endsWith(".js")) {
            printWriter.println("Content-Type:text/js");
        } else if (assetsPath.endsWith(".css")) {
            printWriter.println("Content-Type:text/css");
        } else if (assetsPath.endsWith(".jpg")) {
            printWriter.println("Content-Type:text/jpg");
        } else if (assetsPath.endsWith(".png")) {
            printWriter.println("Content-Type:text/png");
        }
        printWriter.println();

        printWriter.write(raw);

        Log.i(tag, "postHandle  over");
    }


    String tag = "handler";
}
