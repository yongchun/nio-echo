package com.shannon.nio.bean;

import java.io.Serializable;

/**
 * Created by Shannon,chen on 16/2/29.
 * <p/>
 * 模拟客户端&服务端通信时传递的信息
 */
public class EchoRequest implements Serializable {
    private String desc;
    private int value;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
