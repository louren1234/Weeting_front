package com.example.again;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InterestListRecyclerAdapter extends RecyclerView.Adapter<InterestListRecyclerAdapter.ViewHolder> {

    private List<MoimCategoryData> dataList;
    private Context context;

    public interface OnCategoryListClickListener {
        //        public void onItemClick(RecyclerAdapter.ViewHolder holder, View view, int position);
        public void onCategoryClick(View view, MoimCategoryData moimCategoryData);
    }

    public InterestListRecyclerAdapter.OnCategoryListClickListener mOnItemClickListener = null;

    public void setOnItemClicklistener(InterestListRecyclerAdapter.OnCategoryListClickListener listener) {
        mOnItemClickListener = listener;
    }

    public InterestListRecyclerAdapter(Context context, List<MoimCategoryData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }



    //    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interest_lists, parent, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.moimlist_element, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull InterestListRecyclerAdapter.ViewHolder holder, int position) {

        final MoimCategoryData moimCategoryData = dataList.get(position);

        holder.listName.setText(dataList.get(position).getInterests_name());

        holder.listName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnItemClickListener.onCategoryClick(v, moimCategoryData);
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
        TextView listName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = (TextView)itemView.findViewById(R.id.listName);
        }

    }

}
