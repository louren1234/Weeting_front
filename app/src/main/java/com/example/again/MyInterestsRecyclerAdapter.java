package com.example.again;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyInterestsRecyclerAdapter extends RecyclerView.Adapter<MyInterestsRecyclerAdapter.ViewHolder> {

//    private List<UserData> dataList;
    private String[] dataList;
    private Context mcontext;

    public MyInterestsRecyclerAdapter(Context mcontext, String[] dataList) {
        this.mcontext = mcontext;
        this.dataList = dataList;
    }

    //    @NonNull
    @Override
    public MyInterestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.my_interests_element, parent, false);

        return new MyInterestsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInterestsRecyclerAdapter.ViewHolder holder, int position) {

//        final UserData userData = dataList.get(position);
//        final String[] interests = dataList.

        holder.userInterests.setText(dataList[position]);
    }

    @Override
    public int getItemCount() {
        int result;
        if (dataList == null) {
            result = 0;
        }
        else{
            result = dataList.length;
        }
        return result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userInterests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userInterests = (TextView) itemView.findViewById(R.id.userInterests);

        }

    }
}
