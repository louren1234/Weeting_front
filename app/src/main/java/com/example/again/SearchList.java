package com.example.again;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchList extends AppCompatActivity implements RecyclerAdapter.OnDataDetailClickListener {
    RecyclerView searchRecycler;
    RecyclerAdapter recyclerAdapter;
    Context context;
    EditText searchEdit;
    MoimCategoryResultData.serviceApi serviceApi;
    List<MoimCategoryResultData> moimDataInfo;

    private Spinner spinnerCity, spinnerSigungu, spinnerDong;
    private ArrayAdapter<String> arrayAdapter;
    String address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        searchRecycler = findViewById(R.id.searchMoimList);
        context = this;
        searchEdit = findViewById(R.id.edit_search);
        serviceApi = RetrofitClient.getClient().create(MoimCategoryResultData.serviceApi.class);

        spinnerCity = (Spinner)findViewById(R.id.city);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.spinner_region));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(arrayAdapter);

        spinnerSigungu = (Spinner)findViewById(R.id.region);
        spinnerDong = (Spinner)findViewById(R.id.neighborhood);

        initAddressSpinner();



        searchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    public void SearchMoim(View view){
        address = "";
        if (spinnerCity.getSelectedItemPosition() != 0 && spinnerSigungu.getSelectedItemPosition() !=0 && spinnerDong.getSelectedItemPosition() != 0) {
            address = spinnerCity.getSelectedItem().toString()+" " + spinnerSigungu.getSelectedItem().toString()+" " + spinnerDong.getSelectedItem().toString();
        }
        else if (spinnerCity.getSelectedItemPosition() != 0 && spinnerSigungu.getSelectedItemPosition() !=0) {
            address = spinnerCity.getSelectedItem().toString() +" "+ spinnerSigungu.getSelectedItem().toString();
        }
        else if(spinnerCity.getSelectedItemPosition()!=0){
            address = spinnerCity.getSelectedItem().toString();
        }
        if (spinnerCity.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "시를 선택해주세요! ", Toast.LENGTH_LONG).show();
        }
        if(address.length()>0){
            LocationSearch();
        }
        else {
            serviceApi.getSearchEntire(searchEdit.getText().toString()).enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
                @Override
                public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {
                    MoimCategoryResultData.MoimCategoryResultDataResponse response1 = response.body();
                    if (response1.getStatus() == 200) {
                        moimDataInfo = response1.data;

                        recyclerAdapter = new RecyclerAdapter(context, moimDataInfo);
                        recyclerAdapterinit(recyclerAdapter);
                        recyclerAdapter.notifyDataSetChanged();
                        Toast.makeText(SearchList.this, "조회 성공", Toast.LENGTH_LONG).show();

                    } else if (response1.getStatus() == 404) {
                        Toast.makeText(SearchList.this, "검색 결과 없음", Toast.LENGTH_LONG).show();
                    } else if (response1.getStatus() == 500) {
                        Toast.makeText(SearchList.this, "서버 오류", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {
                    Toast.makeText(SearchList.this, "검색 에러", Toast.LENGTH_LONG).show();
                    Log.e("검색 에러", t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }
    public void LocationSearch(){

        serviceApi.getSearchbyLocation(searchEdit.getText().toString(), address).enqueue(new Callback<MoimCategoryResultData.MoimCategoryResultDataResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Response<MoimCategoryResultData.MoimCategoryResultDataResponse> response) {
                MoimCategoryResultData.MoimCategoryResultDataResponse response1 = response.body();
                if(response1.getStatus()==200){
                    moimDataInfo = response1.data;

                    recyclerAdapter = new RecyclerAdapter(context,moimDataInfo);
                    recyclerAdapterinit(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(SearchList.this,"조회 성공",Toast.LENGTH_LONG).show();

                }
                else if(response1.getStatus()==404){
                    Toast.makeText(SearchList.this,"검색 결과 없음",Toast.LENGTH_LONG).show();
                }
                else if(response1.getStatus()==500){
                    Toast.makeText(SearchList.this,"서버 오류",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MoimCategoryResultData.MoimCategoryResultDataResponse> call, Throwable t) {
                Toast.makeText(SearchList.this,"검색 에러",Toast.LENGTH_LONG).show();
                Log.e("검색 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }
    @Override
    public void onItemClick(View view, MoimCategoryResultData moimCategoryResultData) {

    }
    public void recyclerAdapterinit(RecyclerAdapter moimRecyclerAdapter){
        moimRecyclerAdapter.setOnItemClicklistener(this);
        searchRecycler.setAdapter(moimRecyclerAdapter);
    }

    private void initAddressSpinner() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 시군구, 동의 스피너를 초기화한다.
                switch (position) {
                    case 0:
                        spinnerSigungu.setAdapter(null);
                        break;
                    case 1:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 2:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 3:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 4:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 5:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 6:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 7:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 8:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 9:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 10:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 11:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 12:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_nam);

                        break;
                    case 13:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 14:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 15:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 16:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 17:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSigungu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 서울특별시 선택시
                if(spinnerCity.getSelectedItemPosition() == 1 && spinnerSigungu.getSelectedItemPosition() > -1) {
                    switch(position) {
                        //25
                        case 0:
                            break;
                        case 1:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangnam);
                            break;
                        case 2:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangdong);
                            break;
                        case 3:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangbuk);
                            break;
                        case 4:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangseo);
                            break;
                        case 5:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwanak);
                            break;
                        case 6:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwangjin);
                            break;
                        case 7:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_guro);
                            break;
                        case 8:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_geumcheon);
                            break;
                        case 9:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_nowon);
                            break;
                        case 10:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dobong);
                            break;
                        case 11:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongdaemun);
                            break;
                        case 12:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongjag);
                            break;
                        case 13:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_mapo);
                            break;
                        case 14:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seodaemun);
                            break;
                        case 15:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seocho);
                            break;
                        case 16:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongdong);
                            break;
                        case 17:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongbuk);
                            break;
                        case 18:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_songpa);
                            break;
                        case 19:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yangcheon);
                            break;
                        case 20:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yeongdeungpo);
                            break;
                        case 21:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yongsan);
                            break;
                        case 22:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_eunpyeong);
                            break;
                        case 23:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jongno);
                            break;
                        case 24:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jung);
                            break;
                        case 25:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jungnanggu);
                            break;
                    }
                } else {
                    setDongSpinnerAdapterItem(R.array.spinner_region_other_dong);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSigunguSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerSigungu.setAdapter(null);
            arrayAdapter = null;
        }

        if (spinnerCity.getSelectedItemPosition() > 1) {
            spinnerDong.setAdapter(null);
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSigungu.setAdapter(arrayAdapter);
    }

    private void setDongSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerDong.setAdapter(null);
            arrayAdapter = null;
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDong.setAdapter(arrayAdapter);
    }
}

