package com.example.again;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class InterestPopUp extends Dialog implements View.OnClickListener{
    Context context;
    Button btnOk;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest);
        btnOk = findViewById(R.id.interestComplete);
        btnOk.setOnClickListener(this);
    }
    public InterestPopUp(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.interestComplete:
                this.dismiss();
        }
    }
}
