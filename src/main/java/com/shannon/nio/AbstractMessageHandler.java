package com.shannon.nio;

import java.nio.channels.SelectionKey;

/**
 * Created by Shannon,chen on 16/2/28.
 */
public abstract class AbstractMessageHandler implements MessageHandler {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void handle(SelectionKey selectionKey) throws Exception {
        if (!selectionKey.isValid()) {
            return;
        }

        if (selectionKey.isAcceptable()) {
            accept(selectionKey);
        }

        if (selectionKey.isConnectable()) {
            connect(selectionKey);
        }

        if (selectionKey.isReadable()) {
            read(selectionKey);
        }

    }
}
