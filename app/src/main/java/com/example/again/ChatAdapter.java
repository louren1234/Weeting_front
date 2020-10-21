package com.example.again;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.Hold;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatData> chatDataArrayList = null;
    Context context;
    RecyclerView.ViewHolder viewHolder1;
    RecyclerView.ViewHolder viewHolder2;
//    TextView chat_text;
//    TextView yourId;
    SharedPreferences sp;
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
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType ==1 ){
            view = inflater.from(context).inflate(R.layout.my_chat_item, parent,false);
            viewHolder1 = new Holder1(view);
            return viewHolder1;
        }
        else{
            view = inflater.from(context).inflate(R.layout.your_chat_item, parent,false);
            viewHolder2 = new Holder2(view);
            return viewHolder2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final ChatData chatData = chatDataArrayList.get(i);
        if(holder instanceof Holder1){
            ((Holder1)holder).chat_text.setText(chatDataArrayList.get(i).getChatScripts());
        }

        else if(holder instanceof Holder2){
            ((Holder2) holder).chat_text.setText(chatDataArrayList.get(i).getChatScripts());
            ((Holder2) holder).yourId.setText(chatDataArrayList.get(i).getChatId());
        }
    }


    @Override
    public int getItemCount() {
        int result;
        if (chatDataArrayList == null) {
            result = 0;
        }
        else{
            result = chatDataArrayList.size();
        }
        return result;
    }
    public class Holder1 extends RecyclerView.ViewHolder{
        TextView chat_text;
        public Holder1(@NonNull View itemView) {
            super(itemView);
            chat_text = itemView.findViewById(R.id.chat_Text);
        }
    }
    public class Holder2 extends RecyclerView.ViewHolder{
        TextView chat_text;
        TextView yourId;
        public Holder2(@NonNull View itemView) {
            super(itemView);
            chat_text = itemView.findViewById(R.id.chat_Text);
            yourId = itemView.findViewById(R.id.chatYourId);
        }

    }
    public int getItemViewType(int position){
        sp = context.getSharedPreferences("myFile", Activity.MODE_PRIVATE);
        String temp = sp.getString("name","");
//        System.out.println(temp+"sssssssss");
//        System.out.println(chatDataArrayList.get(position).getChatId()+"dddddddd");
        if(chatDataArrayList.get(position).getChatId()==temp){
            return 1;
        }
        else return 2;
    }


}
