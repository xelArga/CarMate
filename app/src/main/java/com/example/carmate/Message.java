package com.example.carmate;

import java.io.Serializable;

public class Message implements Serializable {
    private final String content;
    private final boolean isSent;

    public Message(String content, boolean isSent) {
        this.content = content;
        this.isSent = isSent;
    }

    public String getContent() {
        return content;
    }

    public boolean isSent() {
        return isSent;
    }
}

