package com.example.again;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimMemberRecyclerAdapter extends RecyclerView.Adapter<MoimMemberRecyclerAdapter.ViewHolder> {

    private List<MoimDetailData> dataList;
    private Context mcontext;

    public MoimMemberRecyclerAdapter(Context mcontext, List<MoimDetailData> dataList) {
        this.mcontext = mcontext;
        this.dataList = dataList;
    }

    //    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.moim_member_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoimMemberRecyclerAdapter.ViewHolder holder, int position) {

        final MoimDetailData moimDetailData = dataList.get(position);

        String memberImg = moimDetailData.getUser_img();

        holder.memberNickname.setText(moimDetailData.getUser_nick_name());
        holder.memberIntro.setText(moimDetailData.getUser_introduce());

        Glide.with(mcontext)
                .load(memberImg)
                .error(R.drawable.error)
                .fallback(R.drawable.nullimage)
                .into(holder.memberImg);
    }

    @Override
    public int getItemCount() {
        int result;
        if (dataList == null) {
            result = 0;
        }
        else{
            result = dataList.size();
        }
        return result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memberNickname;
        private TextView memberIntro;
        private ImageView memberImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberNickname = (TextView) itemView.findViewById(R.id.memberNickname);
            memberIntro = (TextView) itemView.findViewById(R.id.memberIntroduce);
            memberImg = (ImageView)itemView.findViewById(R.id.memberImg);

        }

    }


}
