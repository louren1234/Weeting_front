//package com.example.again;//package com.example.again;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//
//import java.lang.reflect.Field;
//import java.text.ParseException;
//import java.util.ArrayList;
//
//
//public class InterestPopUp extends Dialog implements View.OnClickListener{
//    Context context;
//    Button btnOk;
//    ChipGroup chipGroup;
//    static int completeCount;
//    String[] chipCategory = {"reading","music","study","trip", "interior","photo","sports","job","shopping","stock","riding","horticulture","food","camping","exhibition"};
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.interest);
//        chipGroup = findViewById(R.id.chipgroup);
//        try{
//        Filter();
//        }catch (NullPointerException e){
//            System.out.println();
//        }
//        btnOk = findViewById(R.id.interestComplete);
//        btnOk.setOnClickListener(this);
//    }
//    public void Filter(){
//        int ids = chipGroup.getCheckedChipId();
//        interests = "";
//        for(int i =0 ; i < chipGroup.getChildCount(); i++){
//            Chip chip = (Chip)chipGroup.getChildAt(i);
//            if(chip.isChecked()){
//                System.out.println(chip.getText().toString()+"sssssssssssssss");
//                interests += chip.getText().toString();
//                interests = interests + "/";
//                interestsCount++;
//            }
//
//        }
//
//    }
//    public InterestPopUp(@NonNull Context context) {
//        super(context);
//        this.context = context;
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.interestComplete:
//                Filter();
//
//
//        }
//    }
//}
