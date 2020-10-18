package com.example.again;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.Hold;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<ChatData> chatDataArrayList;
    Context context;
    RecyclerView.ViewHolder viewHolder1;
    RecyclerView.ViewHolder viewHolder2;
    TextView chat_text;
    TextView yourId;
    public ChatAdapter(Context context, ArrayList<ChatData> arrayList){
        this.context = context;
        this.chatDataArrayList = arrayList;
    }
    public void addItem(ChatData item){
        if(chatDataArrayList != null){
            chatDataArrayList.add(item);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType ==1 ){
            view = LayoutInflater.from(context).inflate(R.layout.my_chat_item, parent,false);
            viewHolder1 = new Holder1(view);
            return viewHolder1;
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.your_chat_item, parent,false);
            viewHolder2 = new Holder2(view);
            return viewHolder2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(holder == viewHolder1){
            chat_text.setText(chatDataArrayList.get(i).getChatScripts());
        }
        else if(holder == viewHolder2){
            chat_text.setText(chatDataArrayList.get(i).getChatScripts());
            yourId.setText(chatDataArrayList.get(i).getChatId());
        }
    }


    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }
    public class Holder1 extends RecyclerView.ViewHolder{
        public Holder1(@NonNull View itemView) {
            super(itemView);
            chat_text = itemView.findViewById(R.id.chat_Text);
        }
    }
    public class Holder2 extends RecyclerView.ViewHolder{
        public Holder2(@NonNull View itemView) {
            super(itemView);
            chat_text = itemView.findViewById(R.id.chat_Text);
            yourId = itemView.findViewById(R.id.chatYourId);
        }

    }
    public int getItemViewType(int position){
        String temp = PreferenceManager.getString(context,"name");
        if(chatDataArrayList.get(position).getChatId()==temp){
            return 1;
        }
        else return 2;
    }
}
