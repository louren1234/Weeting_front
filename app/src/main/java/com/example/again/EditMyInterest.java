package com.example.again;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyInterest extends AppCompatActivity {

    private Button editMyInterestsButton;

    String interests;
    static int interestsCount;

    private UserData.serviceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_interests);

        serviceApi = RetrofitClient.getClient().create(UserData.serviceApi.class);

        editMyInterestsButton = findViewById(R.id.editMyInterestsButton);
//        editMyInterestsButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                try {
//                    TempInterest(view);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    public void TempInterest(View view) throws ParseException {
        CustomDialog customDialog = new CustomDialog(EditMyInterest.this);
        customDialog.callFunction();
    }

    class CustomDialog {

        private Context context;
        ChipGroup chipGroup;

        public CustomDialog(Context context) {
            this.context = context;
        }

        // 호출할 다이얼로그 함수를 정의한다.
        public void callFunction() {

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog(context);

            // 액티비티의 타이틀바를 숨긴다.
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dlg.setContentView(R.layout.interest);

            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            // 커스텀 다이얼로그의 각 위젯들을 정의한다.
            final Button okButton = (Button) dlg.findViewById(R.id.interestComplete);
            chipGroup = (ChipGroup) dlg.findViewById(R.id.chipgroup);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                    // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                    Filter();
                    // 커스텀 다이얼로그를 종료한다.
//                    try {
//                        SignUpCheck();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    // 여기 서버랑 연동해서 수정사항 보내기
                    serviceApi.updateMyInterests(interests).enqueue(new Callback<UserData.UserImgorIntroorInterestsResponse>() {
                        @Override
                        public void onResponse(Call<UserData.UserImgorIntroorInterestsResponse> call, Response<UserData.UserImgorIntroorInterestsResponse> response) {
                            UserData.UserImgorIntroorInterestsResponse dataList = response.body();

                            if (dataList.getState() == 200) {
                                dlg.dismiss();
                                Toast.makeText(getApplicationContext(), "관심사가 수정되었습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserData.UserImgorIntroorInterestsResponse> call, Throwable t) {

                        }
                    });


                }
            });

        }

        public void Filter() {
            int ids = chipGroup.getCheckedChipId();
            interests = "";
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    System.out.println(chip.getText().toString() + "sssssssssssssss");
                    interests += chip.getText().toString();
                    interests = interests + "/";
                    interestsCount++;
                }

            }

        }
    }
}
