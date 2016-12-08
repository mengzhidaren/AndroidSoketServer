package com.yyl.server;


/**
 * Created by Administrator on 2016/12/6/006.
 */

public class WebConfiguration {

    /**
     * 端口
     */
    private int port;
    /**
     * 最大监听数
     */
    private int maxParallels;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }

    public int getMaxParallels() {
        return maxParallels;
    }
}
