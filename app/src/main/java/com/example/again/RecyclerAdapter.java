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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<MoimCategoryResultData> dataList;
    private Context mcontext;
//    private MoimCategoryResultData.serviceApi serviceApi;
//    OnDataDetailClickListener listener;

//    public void setOnItemClicklistener(Callback<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCategoryResultDataResponseCallback) {
//    }

    public interface OnDataDetailClickListener {
//        public void onItemClick(RecyclerAdapter.ViewHolder holder, View view, int position);
        public void onItemClick(View view, MoimCategoryResultData moimCategoryResultData);
    }

    public OnDataDetailClickListener mOnItemClickListener = null;

    public void setOnItemClicklistener(OnDataDetailClickListener listener) {
        mOnItemClickListener = listener;
    }

    public RecyclerAdapter(Context mcontext, List<MoimCategoryResultData> dataList) {
        this.mcontext = mcontext;
        this.dataList = dataList;
    }

//    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.moimlist_element, parent, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.moimlist_element, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final MoimCategoryResultData moimCategoryResultData = dataList.get(position);

        holder.moimName.setText(dataList.get(position).getMeeting_name());
        holder.moimNum.setText(String.valueOf(dataList.get(position).getMeeting_recruitment()));
        holder.moimLocation.setText(dataList.get(position).getMeeting_location());
        holder.moimDate.setText(String.valueOf(dataList.get(position).getMeeting_time()));

        String img = dataList.get(position).getMeeting_img();
//        Bitmap imgbitmap = BitmapConverter.StringToBitmap(img);
//        holder.moimimg.setImageBitmap(imgbitmap);

        Glide.with(mcontext)
                .load(img)
                .error(R.drawable.error)
                .fallback(R.drawable.nullimage)
                .into(holder.moimimg);
// 참고 https://blog.yena.io/studynote/2020/06/10/Android-Glide.html

        holder.moimList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnItemClickListener.onItemClick(v, moimCategoryResultData);
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

//    public MoimCategoryResultData getItem(int position){
//        return dataList.get(position);
//    }

//    출처: https://everyshare.tistory.com/68 [에브리셰어]

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView moimName;
        private TextView moimNum;
        private TextView moimLocation;
        private TextView moimDate;
        private ImageView moimimg;
        private LinearLayout moimList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moimName = (TextView)itemView.findViewById(R.id.groupName);
            moimNum = (TextView)itemView.findViewById(R.id.groupNum);
            moimLocation = (TextView)itemView.findViewById(R.id.groupArea);
            moimDate = (TextView)itemView.findViewById(R.id.groupDate);
            moimimg = (ImageView)itemView.findViewById(R.id.groupImage);
            moimList = (LinearLayout)itemView.findViewById(R.id.meetingList);

//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(listener != null) {
//                        listener.onItemClick(ViewHolder.this, v, position);
//                    }
//                }
//            });


        }

    }


}
