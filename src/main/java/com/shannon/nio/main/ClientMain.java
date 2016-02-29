package com.shannon.nio.main;

import com.alibaba.fastjson.JSON;
import com.shannon.nio.AbstractMessageHandler;
import com.shannon.nio.bean.EchoRequest;
import com.shannon.nio.client.ClientMessageHandler;
import com.shannon.nio.client.EchoClient;

/**
 * Created by Shannon,chen on 16/2/28.
 */
public class ClientMain {
    public static void main(String[] args) {
        EchoRequest request = new EchoRequest();
        request.setDesc("echo client request");
        request.setValue(12);
        String content = JSON.toJSONString(request);
        AbstractMessageHandler handler = new ClientMessageHandler();
        handler.setContent(content);

        EchoClient echoClient = new EchoClient("127.0.0.1", 8002, handler);
        echoClient.start();
        // echoClient.stop();
    }
}
