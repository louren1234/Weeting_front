package com.example.again;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;

public class Search extends AppCompatActivity {
    EditText m_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

//        m_search = findViewById(R.id.search);
    }

//    public void searchValid(View v) throws ParseException {
//        m_search.setError(null);
//        String search = m_search.getText().toString();
//
//        if(search.isEmpty()){
//            Toast.makeText(getApplicationContext(), "search 빈 칸 존재", Toast.LENGTH_LONG).show();
//        }
//        else{
//            startSearch(new )
//        }
//    }
}