package com.shannon.nio.main;

import com.shannon.nio.AbstractMessageHandler;
import com.shannon.nio.server.EchoServer;
import com.shannon.nio.server.ServerMessageHandler;

/**
 * Created by Shannon,chen on 16/2/28.
 */
public class ServerMain {
    public static void main(String[] args) {
        AbstractMessageHandler handler = new ServerMessageHandler();
        EchoServer echoServer = new EchoServer("127.0.0.1", 8002, handler);
        echoServer.start();
        // echoServer.stop();

    }
}
