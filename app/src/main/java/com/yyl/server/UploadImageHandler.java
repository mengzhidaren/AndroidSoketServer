package com.yyl.server;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/12/6/006.
 */

public  class UploadImageHandler implements IResourceUriHandler {
    String acceptPrefix = "/image/";
    Activity activity;

    public UploadImageHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void postHandle(String uri, HttpContext httpContext) throws IOException {
        long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Content-Length").trim());
        File file = new File(Environment.getExternalStorageDirectory(),
                "tmpFile.jpg");
        Log.i(tag, "totalLength=" + totalLength + " file    getPath=" + file.getPath());
        if (file.exists()) {
            file.delete();
        }
        byte[] buffer = new byte[10240];
        int read;
        long nLeftLength = totalLength;
        FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
        InputStream inputStream = httpContext.getUnderlySocket().getInputStream();
        while (nLeftLength > 0&&(read = inputStream.read(buffer)) > 0 ) {
            fileOutputStream.write(buffer, 0, read);
            nLeftLength -= read;
        }
        Log.i(tag, "close");
        fileOutputStream.close();

        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println();
        onImageLoaded(file.getPath());
    }

    String tag = "";

    public void onImageLoaded(String path) {
        Log.i(tag, "path=" + path);
    }
}
