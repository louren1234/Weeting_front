package com.example.again;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Qna extends AppCompatActivity {

    private QnaData.serviceApi serviceApi;
    private EditText m_content;
//    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna);

        ImageButton back = findViewById(R.id.back);
        Button qna_list = findViewById(R.id.myqna);

        m_content = findViewById(R.id.content);

        serviceApi = RetrofitClient.getClient().create(QnaData.serviceApi.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        qna_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Qna_list.class);
                startActivity(intent);
            }
        });

//        sendQna.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = contents.getText().toString();
//
//                contents.setText("");
//            }
//        });
    }

    public void createQnaValid(View view) throws ParseException {
        m_content.setError(null);
        String content = m_content.getText().toString();

        if(content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈 칸 존재", Toast.LENGTH_LONG).show();
        }
        else{
            startCreateQna(new QnaData(content));
            Toast.makeText(getApplicationContext(), "Qna 전송 완료", Toast.LENGTH_LONG).show();
        }
    }

    public void startCreateQna(QnaData data) {
        serviceApi.createQna(data).enqueue(new Callback<QnaData.QnaResponse>(){
            @Override
            public void onResponse(Call<QnaData.QnaResponse> call, Response<QnaData.QnaResponse> response){
                QnaData.QnaResponse result = response.body();
                Toast.makeText(Qna.this, result.getMessage(), Toast.LENGTH_LONG).show();

                if(result.getStatus() == 200) {
                    System.out.println("yeah");
                    finish();
                }
                else if(result.getStatus() == 400) {
                    System.out.println("nooooo");
                }
            }

            @Override
            public void onFailure(Call<QnaData.QnaResponse> call, Throwable t) {
                Toast.makeText(Qna.this, "모임생성 에러", Toast.LENGTH_LONG).show();
                Log.e("모임생성 에러", t.getMessage());
                t.printStackTrace();
            }
        });
    }


}
