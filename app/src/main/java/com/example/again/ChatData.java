package com.example.again;

public class ChatData {
  private String chatId, chatScripts;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatScripts() {
        return chatScripts;
    }

    public void setChatScripts(String chatScripts) {
        this.chatScripts = chatScripts;
    }

    public ChatData(String chatId, String chatScripts) {
        this.chatId = chatId;
        this.chatScripts = chatScripts;
    }
}
