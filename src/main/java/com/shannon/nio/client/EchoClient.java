package com.shannon.nio.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shannon.nio.AbstractMessageHandler;
import com.shannon.nio.EchoReactor;

/**
 * Created by Shannon,chen on 16/2/28.
 * <p/>
 * 客户端
 */
public class EchoClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(EchoClient.class.getName());

    private final static int BUFFER_SIZE = 1024;

    private SocketChannel socketChannel; // 客户端channel
    private EchoReactor reactor; // reactor线程

    public EchoClient(String host, int port, AbstractMessageHandler messageHandler) {
        try {
            reactor = new EchoReactor(messageHandler); // 初始化reactor线程

            socketChannel = SocketChannel.open(); // 打开sockectChannel

            socketChannel.configureBlocking(Boolean.FALSE); // 设置socketChannel为非阻塞模式，同时设置TCP参数
            Socket socket = socketChannel.socket();
            socket.setReuseAddress(Boolean.TRUE);
            socket.setSendBufferSize(BUFFER_SIZE);
            socket.setReceiveBufferSize(BUFFER_SIZE);

            //  http://blog.csdn.net/anders_zhuo/article/details/8546604
            //  http://book.51cto.com/art/200902/109752.htm
            socketChannel.connect(new InetSocketAddress(host, port)); // 连接服务，这一处需要注意


            // 向Reactor线程的多路服用器注册OP_CONNECT事件
            reactor.registerChannel(socketChannel, SelectionKey.OP_CONNECT, messageHandler);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("client start up");

    }

    public void stop() {
        reactor.stop();
    }

    public void start() {
        reactor.run();

    }
}
