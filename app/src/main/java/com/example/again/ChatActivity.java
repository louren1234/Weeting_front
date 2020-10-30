package com.example.again;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.internal.Internal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity{
    SharedPreferences preferences;
    EditText sendText;
    ArrayList<ChatData> arrayList;
    ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    Context context;
    Button chatSendBtn;
    String item;
    String tempItem;
    ChatData chatData;
    Internal internal;
    private Socket mSocket;
    Boolean hasConnection;
    ChatData.serviceApi serviceApi;
    ChatData.ChatResponse dataList;

    public ChatActivity() throws URISyntaxException {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_body);
        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        final int meeting_id = intent.getExtras().getInt("meetingId");

        serviceApi = RetrofitClient.getClient().create(ChatData.serviceApi.class);
        Call<ChatData.ChatResponse> call = serviceApi.getChat(meeting_id);
        call.enqueue(new Callback<ChatData.ChatResponse>() {
            @Override
            public void onResponse(Call<ChatData.ChatResponse> call, Response<ChatData.ChatResponse> response) {
                Log.d("ㅇㅇ", "테스트 채팅");
                dataList = response.body();
                try {
                    mSocket = IO.socket("http://52.35.235.199:3000/chat/"+meeting_id);
                    System.out.println("http://52.35.235.199:3000/chat/"+meeting_id);
                    System.out.println(mSocket+"ffffffffffffffffffffffffffffffffffffff");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                mSocket.connect();

                if(mSocket.connected()){
                    System.out.println("kkkkkkkkkkkkkkkkkkkkkk");

                }else {
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjj");
                }
            }

            @Override
            public void onFailure(Call<ChatData.ChatResponse> call, Throwable t) {

            }
        });

//        try {
//            mSocket = IO.socket("http://52.35.235.199:3000/chat/"+meeting_id);
//            System.out.println("http://52.35.235.199:3000/chat/"+meeting_id);
//            System.out.println(mSocket+"ffffffffffffffffffffffffffffffffffffff");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        hasConnection = false;
//        preferences = getSharedPreferences()
        recyclerView = findViewById(R.id.chatRecycler);
        chatAdapter = new ChatAdapter(getApplicationContext(), arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        chatSendBtn = findViewById(R.id.chat_send_btn);
        sendText = findViewById(R.id.chatScripts);
//        mSocket.on(Socket.EVENT_CONNECT, mSocket);
//        mSocket.on("new user", onNewUser);
//
//        mSocket.on("receive message",onMessageReceived);
//        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
//        JSONObject userId = new JSONObject();
//        try {
//            userId.put("name", sp.getString("name","")+"Connected");
//            mSocket.emit("new user", userId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        hasConnection = true;

//        mSocket.connect();
//
//        if(mSocket.connected()){
//            System.out.println("kkkkkkkkkkkkkkkkkkkkkk");
//
//        }else {
//            System.out.println("jjjjjjjjjjjjjjjjjjjjjjj");
//        }

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

//    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
//
//        @Override
//        public void call(final Object... args) {
//            mSocket.emit("receive message", new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String name ="";
//                    String scripts = "";
//                    Log.e("asdasd",data.toString());
//                    try {
//                        name = data.getString("name");
//                        scripts = data.getString("message");
//                        ChatData chatData = new ChatData(name,scripts);
//                        chatAdapter.addItem(chatData);
//                        chatAdapter.notifyDataSetChanged();
//                        Log.e("new me",name);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };
//    private Emitter.Listener onNewUser = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            mSocket.emit("new user", new Object[]{new Runnable() {
//                @Override
//                public void run() {
//                    int length = args.length;
//                    if (length == 0) {
//                        return;
//                    }
//                    String userName = args[0].toString();
//                    try {
//                        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
//                        tempItem = sp.getString("name","");
//                        JSONObject object = new JSONObject(userName);
//                        userName = object.getString("name");
//                        object.put("name", tempItem);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }});
//        }
//    };
    public void SendMessage() throws JSONException {
        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
        tempItem = sp.getString("name","");
        System.out.println(tempItem);
        chatData = new ChatData(tempItem, sendText.getText().toString());
        String message = sendText.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            return;
        }
        sendText.setText("");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",tempItem);
        jsonObject.put("message",message);
//        Log.e("c","sendMessage:1"+mSocket.emit("send message", jsonObject));

        arrayList.add(chatData);
        chatAdapter.addItem(chatData);
        chatAdapter.notifyDataSetChanged();
//
//        sendText.setText("");
    }
}
