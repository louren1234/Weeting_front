package com.example.again;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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
    class ChatResponse{
            int state;
            String message;

            public int getStatus() {
                return state;
            }
            public String getMessage() {
                return message;
            }
    }
    public interface serviceApi{
        @GET("/chat/{meeting_id}")
        Call<ChatResponse> getChat(@Path("meeting_id") int meeting_id);
    }
}
