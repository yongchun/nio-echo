package com.shannon.nio.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.shannon.nio.AbstractMessageHandler;
import com.shannon.nio.bean.EchoRequest;

/**
 * Created by Shannon,chen on 16/2/29.
 * <p/>
 * 服务端处理
 */
public class ServerMessageHandler extends AbstractMessageHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class.getName());
    private volatile boolean isWrite = Boolean.FALSE;
    private String response;

    @Override
    public void accept(SelectionKey selectionKey) throws Exception {
        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
        Selector selector = selectionKey.selector();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(Boolean.FALSE);
        sc.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void read(SelectionKey selectionKey) throws Exception {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int readBytes = sc.read(readBuffer);
        if (readBytes > 0) {
            readBuffer.flip();
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            String body = new String(bytes, "UTF-8");
            LOGGER.info("The echo server receive request :" + body);
            this.response = body;
            this.isWrite = Boolean.FALSE;
            sc.register(selectionKey.selector(), SelectionKey.OP_WRITE);

        } else if (readBytes < 0) {
            selectionKey.cancel();
            sc.close();
        } else {
            // do nothing
        }
    }

    @Override
    public void write(SelectionKey selectionKey) throws Exception {

        if (!isWrite) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();
            EchoRequest echoRequest = JSON.parseObject(this.response, EchoRequest.class);

            int responseValue = echoRequest.getValue() * 100;
            echoRequest.setDesc("echo server response");
            echoRequest.setValue(responseValue);

            byte[] responseBytes = JSON.toJSONString(echoRequest).getBytes();
            ByteBuffer writerBuffer = ByteBuffer.allocate(responseBytes.length);
            writerBuffer.put(responseBytes);
            writerBuffer.flip();
            this.isWrite = Boolean.TRUE;
            LOGGER.info("The echo server send response :" + JSON.toJSONString(echoRequest));
            sc.write(writerBuffer); // iswrite=true
        } else {
            // do nothing 跳出服务端循环
        }
    }

    @Override
    public void connect(SelectionKey selectionKey) throws Exception {
        // do nothing

    }

}
