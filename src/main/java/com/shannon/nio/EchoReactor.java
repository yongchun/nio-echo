package com.shannon.nio;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/2/29.
 * <p/>
 * echo Reactor线程 待优化
 */
public class EchoReactor implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(EchoReactor.class.getName());

    private Selector selector;
    private AbstractMessageHandler messageHandler;
    private volatile boolean isStop;

    public EchoReactor(AbstractMessageHandler messageHandler) {
        try {
            this.isStop = Boolean.FALSE;
            this.messageHandler = messageHandler;
            selector = Selector.open(); // 创建Selector
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            while (!isStop) {
                int keys = selector.select();
                if (keys < 1) {
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) { // 轮询就绪的key并进行处理
                    try {
                        selectionKey = iterator.next();
                        iterator.remove(); // 需要手动清除
                        messageHandler.handle(selectionKey);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                            if (serverSocketChannel != null) {
                                serverSocketChannel.close();
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    public void registerChannel(SelectableChannel selectableChannel, int selectionKey, Object handle)
            throws ClosedChannelException {
        if (isStop) {
            throw new RuntimeException("not start");
        }
        if (checkSelectionKey(selectionKey)) {
            selectableChannel.register(selector, selectionKey, handle);
        }
    }

    public void stop() {
        this.isStop = Boolean.TRUE;
    }

    private boolean checkSelectionKey(int selectionKey) {
        if (SelectionKey.OP_ACCEPT == selectionKey) {
            return Boolean.TRUE;
        }

        if (SelectionKey.OP_CONNECT == selectionKey) {
            return Boolean.TRUE;
        }

        if (SelectionKey.OP_READ == selectionKey) {
            return Boolean.TRUE;
        }

        if (SelectionKey.OP_WRITE == selectionKey) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
