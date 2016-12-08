package com.yyl.server;

import android.util.Log;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/6/006.
 * adb devices
 */

public class HttpContext {
    private Socket underlySocket;
    Map<String, String> heard = new HashMap<>();
    private String type;

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    /**
     * @param key
     * @param value
     */
    //       12-08 12:05:28.611 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Host: 192.168.1.56:8088
    //        12-08 12:05:28.611 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Connection: keep-alive
    //       12-08 12:05:28.621 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Content-Length: 14
    //        12-08 12:05:28.621 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Origin: chrome-extension://aejoelaoggembcahagimdiliamlcdmfm
    //       12-08 12:05:28.631 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36
    //      12-08 12:05:28.631 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Content-Type: application/x-www-form-urlencoded
    //      12-08 12:05:28.641 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Accept: */*
    //      12-08 12:05:28.641 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Accept-Encoding: gzip, deflate
    //      12-08 12:05:28.641 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine = Accept-Language: zh-CN,zh;q=0.8,en;q=0.6
    //      12-08 12:05:28.651 5124-27686/com.yyl.server I/SimpleHttpServer: headerLine =
    public void addRequestHeader(String key, String value) {
        heard.put(key, value);
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public String getRequestHeaderValue(String key) {
        return heard.get(key);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
