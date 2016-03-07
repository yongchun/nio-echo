package com.shannon.nio.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shannon.nio.AbstractMessageHandler;

/**
 * Created by Shannon,chen on 16/2/29.
 * <p/>
 * 客户端处理
 */
public class ClientMessageHandler extends AbstractMessageHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class.getName());
    private volatile boolean isWrite = Boolean.FALSE;

    @Override
    public void accept(SelectionKey selectionKey) throws Exception {
        // do nothing
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

            LOGGER.info("the echo cli  receive response from server is :" + body);

        } else if (readBytes < 0) {
            selectionKey.cancel();
            sc.close();
        } else {
            // do nothing
        }
    }

    @Override
    public void connect(SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        while (!socketChannel.finishConnect()) { // 必须等待连接完全建立,才能写数据
            // 否则MessageHandler处理的时候会抛{@link NotYetConnectedException}异常
        }
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_WRITE);

    }

    @Override
    public void write(SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        if (!isWrite) {
            byte[] requestBytes = getContent().getBytes();
            ByteBuffer writerBuffer = ByteBuffer.allocate(requestBytes.length);
            writerBuffer.put(requestBytes);
            writerBuffer.flip();
            isWrite = Boolean.TRUE;
            socketChannel.write(writerBuffer);

            if (!writerBuffer.hasRemaining()) {
                LOGGER.info("the echo cli send reques :" + getContent());
            } else {
                // todo
            }
        } else {
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        }
    }

}
