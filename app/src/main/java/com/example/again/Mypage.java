package com.example.again;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import org.w3c.dom.Text;

public class Mypage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_mypage);

        TextView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);

        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMap = findViewById(R.id.toMap);
//        ImageButton toCalender = (ImageButton) findViewById(R.id.toCalender);
        ImageButton toMypage = findViewById(R.id.toMypage);

        LinearLayout interest = findViewById(R.id.interest);
        LinearLayout alertSetting = findViewById(R.id.alertSetting);
        LinearLayout faq = findViewById(R.id.faq);
        LinearLayout qna = findViewById(R.id.qna);
        LinearLayout terms_of_use = findViewById(R.id.terms_of_use);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        toList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MoimList.class);
                startActivity(intent);
            }
        });

        toMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Group_location_map.class);
                startActivity(intent);
            }
        });

//        toCalender.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ToCalender.class);
//                startActivity(intent);
//            }
//        });

        toMypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        interest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Interest.class);
                startActivity(intent);
            }
        });

        alertSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlertSetting.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Faq.class);
                startActivity(intent);
            }
        });

        qna.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Qna.class);
                startActivity(intent);
            }
        });

        terms_of_use.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Terms_of_use.class);
                startActivity(intent);
            }
        });
    }
}
