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

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.internal.Internal;

import static com.example.again.MoimDetail.meetingId;


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
    Boolean hasConnection;
    int meeting_id;
    Socket mSocket;
    String message;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_body);
        arrayList = new ArrayList<>();

        hasConnection = false;
//        preferences = getSharedPreferences()
        recyclerView = findViewById(R.id.chatRecycler);
        chatAdapter = new ChatAdapter(getApplicationContext(), arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        chatSendBtn = findViewById(R.id.chat_send_btn);
        sendText = findViewById(R.id.chatScripts);
        //        Intent intent = getIntent();
//        meeting_id = intent.getExtras().getInt("meetingId");
        String url = "http://52.35.235.199:3000/chat/66";
        try {
            mSocket = IO.socket("http://52.35.235.199:3000");
//            mSocket.once("/chat/"+meetingId+"connection",newUser);
//            mSocket.on("chat/"+meetingId,newUser);
//            mSocket.once("/chat/65", (Emitter.Listener) mSocket.on("connection",newUser ));
//            mSocket.once("chat/66",newUser);
//            mSocket.on("chat/66",newUser);
//            mSocket.once(mSocket.EVENT_CONNECT,newUser);
//            System.out.println(mSocket.EVENT_MESSAGE+"2222222222222222");
//            mSocket.on("/socket", newUser);
//            mSocket.connect().once("connection",newUser);
            mSocket.connect();
//            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, newUser);
//            mSocket.connect().once(mSocket.EVENT_CONNECT,newUser);

            if(mSocket.connected()){
                System.out.println("oooooooooooooooooooooooooooooooooooo");
            }
            else if(mSocket.connected()==false)
                System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        mSocket.on("/chat/66", newUser);
//        mSocket.connect();
        if(mSocket.connected()){
            System.out.println("kkkkkkkkkkkkkkkkkkkkkk");
        }

//        mSocket.on("receive message",onMessageReceived);
        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
        JSONObject userId = new JSONObject();
        try {
            userId.put("name", sp.getString("name","")+"Connected");

//            mSocket.emit("new user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hasConnection = true;
        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendMessage();
//                    mSocket.on("send message", sendMessage);
//                    mSocket.on("send message",sendMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    Emitter.Listener sendMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
            tempItem = sp.getString("name","");
            System.out.println(tempItem);
            chatData = new ChatData(tempItem, sendText.getText().toString());
            message = sendText.getText().toString().trim();
            if(TextUtils.isEmpty(message)){
                return;
            }
            sendText.setText("");
            System.out.println("pppppppppppppppppp");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
                    tempItem = sp.getString("name","");
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("meeting_id", "/"+meetingId);
                        jsonObject.put("user_nick_name",tempItem);
                        jsonObject.put("text",message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    private Emitter.Listener newUser = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            System.out.println("ssssssssssssssssssssss");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
                    tempItem = sp.getString("name","");
                    JSONObject object = new JSONObject();
                    System.out.println("999999999999999999999999999999");
                    try {
                        mSocket.emit("connect_user",tempItem);
                        object.put("user_nick_name", tempItem);
//                        object.put("meeting_id", meetingId);
//                        object.get("chats");
//                        object.get("new user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            mSocket.emit("receive message", new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String name ="";
                    String scripts = "";
                    Log.e("asdasd",data.toString());
                    try {
                        name = data.getString("name");
                        scripts = data.getString("message");
                        ChatData chatData = new ChatData(name,scripts);
                        chatAdapter.addItem(chatData);
                        chatAdapter.notifyDataSetChanged();
                        Log.e("new me",name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void SendMessage() throws JSONException {
        SharedPreferences sp = getSharedPreferences("myFile", Activity.MODE_PRIVATE);
        tempItem = sp.getString("name","");
        System.out.println(tempItem);
        chatData = new ChatData(tempItem, sendText.getText().toString());
        message = sendText.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            return;
        }
        sendText.setText("");


//        Log.e("c","sendMessage:1"+mSocket.emit("send message", jsonObject));

        arrayList.add(chatData);
        chatAdapter.addItem(chatData);
        chatAdapter.notifyDataSetChanged();
//
//        sendText.setText("");
    }
}
