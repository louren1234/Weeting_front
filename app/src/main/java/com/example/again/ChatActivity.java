package com.example.again;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_body);
//        preferences = getSharedPreferences()
        recyclerView = findViewById(R.id.chat);
        chatAdapter = new ChatAdapter(context, arrayList);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatSendBtn = findViewById(R.id.chat_send_btn);
        tempItem = PreferenceManager.getString(context,"name");
        sendText = findViewById(R.id.chatScripts);
        chatData = new ChatData(tempItem, sendText.getText().toString());
        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

    }
    public void SendMessage(){

        chatAdapter.addItem(chatData);
        chatAdapter.notifyDataSetChanged();

        sendText.setText("");
    }
}
