package com.example.again;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoimListFragment extends Fragment implements RecyclerAdapter.OnDataDetailClickListener {

    MoimCategoryResultData.MoimCategoryResultDataResponse moimDataList;
    List<MoimCategoryResultData> moimDataInfo;
    RecyclerAdapter moimRecyclerAdapter;
    RecyclerView moimRecyclerView;
    TextView ifmoimnull;

    public MoimListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.moimlist_fragment, container, false);

        MoimCategoryResultData.serviceApi moimApiInterface = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);
        Bundle bundle = getArguments();
        String categoryName = bundle.getString("category");

        ifmoimnull = (TextView) rootView.findViewById(R.id.ifmoimnull);
        ifmoimnull.setVisibility(View.GONE);

        moimRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragmentRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        moimRecyclerView.setLayoutManager(layoutManager);

        if (categoryName.equals("all")) {
            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getAllMoim();
            moimCall.enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                @Override
                public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {

                    moimDataList = response.body();

                    moimDataInfo = moimDataList.data;

                    moimRecyclerAdapter = new RecyclerAdapter(getContext(), moimDataInfo);
                    recyclerAdapterinit(moimRecyclerAdapter);

                    if(moimRecyclerAdapter.getItemCount() == 0) {
                        ifmoimnull.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

                    Log.d("MoimList 실패", t.toString());
                }
            });
        }
        else {
            Call<MoimCategoryResultData.MoimCategoryResultDataResponse> moimCall = moimApiInterface.getCategoryResultMoim(categoryName);
            moimCall.enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                @Override
                public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {

                    moimDataList = response.body();

                    Log.d("MoimList 성공", moimDataList.toString());

                    moimDataInfo = moimDataList.data;

                    moimRecyclerAdapter = new RecyclerAdapter(getContext(), moimDataInfo);
                    recyclerAdapterinit(moimRecyclerAdapter);

                    if(moimRecyclerAdapter.getItemCount() == 0) {
                        ifmoimnull.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {

                    Log.d("MoimList 실패", t.toString());
                }
            });
        }

        return rootView;
    }

    public void recyclerAdapterinit(RecyclerAdapter moimRecyclerAdapter){
        moimRecyclerAdapter.setOnItemClicklistener(this);
        moimRecyclerView.setAdapter(moimRecyclerAdapter);
    }

    // 모임 리스트 클릭 시
    @Override
    public void onItemClick(View view, MoimCategoryResultData moimCategoryResultData){
//        MoimCategoryResultData data = moimRecyclerAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), MoimDetail.class);
        intent.putExtra("meetingId", moimCategoryResultData.getMeeting_id());
        startActivity(intent);
        Log.e("RecyclerVIew :: ", moimCategoryResultData.toString());
    }

    public void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}
