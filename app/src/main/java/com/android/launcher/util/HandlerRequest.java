package com.android.launcher.util;

import java.util.LinkedList;

public class HandlerRequest {

    private LinkedList<String> requestQueue = new LinkedList<>();
    private int count;    // 任务队列请求数

    public HandlerRequest() {
        this.count = 0;
    }

    public synchronized void buildEvent( String request) throws InterruptedException {

        while (!requestQueue.isEmpty() && count >= requestQueue.size()) {
            wait();
        }
        requestQueue.addLast(request);
        count++;
        notifyAll();
    }

    public synchronized  String sendEvent() throws InterruptedException {
        while (count <= 0) {
            wait();
        }
        String request = requestQueue.pop();
        count--;
        notifyAll();
        return request;
    }
}