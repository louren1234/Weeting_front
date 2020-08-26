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

public class MainMoimRecyclerAdapter extends RecyclerView.Adapter<MainMoimRecyclerAdapter.ViewHolder> {

    private List<MainMoimThumbnailData> dataList;
    private Context mcontext;

    public interface OnDataDetailClickListener {
        public void onMainThumbnailClick(View view, MainMoimThumbnailData mainMoimThumbnailData);
    }

    public OnDataDetailClickListener mOnItemClickListener = null;

    public void setOnItemClicklistener(OnDataDetailClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MainMoimRecyclerAdapter(Context mcontext, List<MainMoimThumbnailData> dataList) {
        this.mcontext = mcontext;
        this.dataList = dataList;
    }

    //    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.main_moim_list, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MainMoimRecyclerAdapter.ViewHolder holder, int position) {
        final MainMoimThumbnailData mainMoimThumbnailData = dataList.get(position);

//        holder.moimName.setText(dataList.get(position).getMeeting_name()); // 나중에 썸네일 위에 쓸 글씨

        String img = dataList.get(position).getMeeting_img();

        Glide.with(mcontext)
                .load(img)
                .error(R.drawable.error)
                .fallback(R.drawable.nullimage)
                .into(holder.moimimg);

        holder.moimList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnItemClickListener.onMainThumbnailClick(v, mainMoimThumbnailData);
            }
        });
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
        private TextView moimName;
        private ImageView moimimg;
        private LinearLayout moimList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            moimName = (TextView)itemView.findViewById(R.id.groupName); // 얜 나중에 thumbnail 위에 쓸 글씨
            moimimg = (ImageView)itemView.findViewById(R.id.mainMoim);
            moimList = (LinearLayout)itemView.findViewById(R.id.mainThumbnailLinearLayout);

        }

    }


}
