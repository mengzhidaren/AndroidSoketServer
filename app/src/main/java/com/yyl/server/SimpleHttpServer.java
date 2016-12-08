package com.yyl.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/6/006.
 */

public class SimpleHttpServer {
    String tag = "SimpleHttpServer";
    private WebConfiguration webConfig;
    private ExecutorService threadPool;
    private ServerSocket socket;
    private Set<IResourceUriHandler> resourceUriHandlers;
    private boolean isEnable;

    public SimpleHttpServer(WebConfiguration webConfig) {
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
        resourceUriHandlers = new HashSet<>();
    }

    public void registerResourceHandler(IResourceUriHandler iResourceUriHandler) {
        resourceUriHandlers.add(iResourceUriHandler);
    }


    private void doProSync() {
        Log.i(tag, "doProSync");
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isEnable) {
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(tag, "doProSync    getLocalAddress=" + remotePeer.getLocalAddress());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 一个HTTP请求报文由请求行（request line）、请求头部（header）、空行和请求数据4个部分组成
     *
     * @param remotePeer
     */
    private void onAcceptRemotePeer(Socket remotePeer) {
        HttpContext httpContext = new HttpContext();
        try {
            httpContext.setUnderlySocket(remotePeer);
            InputStream nis = remotePeer.getInputStream();
            String headerLine = null;
            String readLine = StreamTools.readLine(nis);//http请求行（request line）1
            Log.i(tag, "http 1 请求行  readLine =" + readLine);
            //测试请求行结果 readLine =POST /get/?key=dddd&sss=123456 HTTP/1.1
            httpContext.setType(readLine.split(" ")[0]);
            String resourceUri = headerLine = readLine.split(" ")[1];
            Log.i(tag, "地址 =" + headerLine);
            while ((headerLine = StreamTools.readLine(nis)) != null) {//http请求头部（header）2

                if (headerLine.equals("\r\n")) {//http请求空行3
                    Log.i(tag, "http 3 请求头部 /r/n ");
                    break;
                }
                Log.i(tag, "http 2 请求头部 headerLine = " + headerLine);
                String[] pair = headerLine.split(": ");
                if (pair.length > 1) {
                    httpContext.addRequestHeader(pair[0], pair[1]);
                }
            }
            for (IResourceUriHandler handler : resourceUriHandlers) {
                if (!handler.accept(resourceUri)) {
                    continue;
                }
                handler.postHandle(resourceUri, httpContext);//http请求数据4
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                remotePeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startAsync() {
        isEnable = true;
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                doProSync();
            }
        });
    }

    public void stopAsync() {
        if (!isEnable) return;
        isEnable = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
    }
}
