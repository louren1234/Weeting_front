package com.example.again;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinnerTest extends AppCompatActivity {

    MoimCategoryData.MoimCategoryResponse dataList;
    List<MoimCategoryData> dataInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_test);

        final Spinner interestSpinner = findViewById(R.id.spinnerTest);

        MoimCategoryData.serviceApi apiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
        Call<MoimCategoryData.MoimCategoryResponse> call = apiInterface.getSpinnerList();

        call.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {

                dataList = response.body();

                String[] names = new String[dataList.data.size()];
                int i = 0;
                for(MoimCategoryData moimCategoryData : response.body().data) {
                    names[i] = moimCategoryData.getInterests_name();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
                interestSpinner.setAdapter(adapter);

                Log.d("MoimList 성공?", dataList.toString());
            }

            @Override
            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {

                Log.d("MoimList 실패", t.toString());
            }
        });

    }

}
