package com.shannon.nio;

import java.nio.channels.SelectionKey;

/**
 * Created by Shannon,chen on 16/2/26.
 * <p/>
 * 事件处理类
 */
public interface MessageHandler {

    /**
     * @param selectionKey 处理注册在{@link java.nio.channels.Selector}上的
     *                     {@link java.nio.channels.Channel}的{@linkplain SelectionKey#OP_ACCEPT}的事件
     */
    void accept(SelectionKey selectionKey) throws Exception;

    /**
     * @param selectionKey 处理注册在{@link java.nio.channels.Selector}上的
     *                     {@link java.nio.channels.Channel}的{@linkplain SelectionKey#OP_READ}的事件
     */
    void read(SelectionKey selectionKey) throws Exception;


    /**
     * @param selectionKey 处理注册在{@link java.nio.channels.Selector}上的
     *                     {@link java.nio.channels.Channel}的{@linkplain SelectionKey#OP_WRITE}的事件
     */
    void write(SelectionKey selectionKey) throws Exception;


    /**
     * @param selectionKey 处理注册在{@link java.nio.channels.Selector}上的
     *                     {@link java.nio.channels.Channel}的{@linkplain SelectionKey#OP_CONNECT}的事件
     */
    void connect(SelectionKey selectionKey) throws Exception;

}
