package com.example.again;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.again.SignUpData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Multipart;


public class SignUpActivity extends AppCompatActivity {

    private EditText e_name, e_password, e_nickname, e_mail;
    private SignUpData.ServiceApi serviceApi;
    boolean cancel;
    File file;
    static int genderValid;
    RequestBody reqFile, r_email,r_password,r_name,r_nickname;
    private static final int PICK_ALBUM = 1;
    private static final int PICK_CAMERA = 2;
    private File tempFile;
    int writingLayoutId = 0, writingImgId=0;
    static int writing_imgLayoutCount =0;
    String emailNum;
    MultipartBody.Part body;
    String writing_layout, writing_img;
    static int interestsCount;
    static String interests;
    static PreferenceManager preferenceManager;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        e_name = findViewById(R.id.sign_upName);
        e_password = findViewById(R.id.sign_upPassword);
        e_nickname = findViewById(R.id.sign_upId);
        e_mail= findViewById(R.id.sign_upEmail);
        serviceApi = RetrofitClient.getClient()
                .create(SignUpData.ServiceApi.class);
        tedPermission();
        btn();
        if(interestsCount>0){
            try {
                SignUpCheck();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    public void btn(){
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbum();
            }
        });

    }
    private boolean SignUpValid(){
        e_name.setError(null);
        e_password.setError(null);
        e_nickname.setError(null);
        e_mail.setError(null);
        String name = e_name.getText().toString();
        String password = e_password.getText().toString();
        String id = e_nickname.getText().toString();
        String email = e_mail.getText().toString();
        View focusView = null;
        if (name.isEmpty() || password.isEmpty() || id.isEmpty()||email.isEmpty()) {
//            e_nickname.setError("이름을 입력해주세요");
//            focusView=e_nickname;
//            cancel=true;
            Toast.makeText(getApplicationContext(),"비어있는 칸이 있습니다.",Toast.LENGTH_LONG).show();
        }
        else{
            cancel = true;
        }
        return cancel;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SignUpCheck() throws ParseException {
        e_name.setError(null);
        e_password.setError(null);
        e_nickname.setError(null);
        e_mail.setError(null);
        String birth = "2017-07-30 10:10:10";
        SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date birth_date = trans.parse(birth);
        String name = e_name.getText().toString();
        String password = e_password.getText().toString();
        String id = e_nickname.getText().toString();
        String email = e_mail.getText().toString();

//        Drawable drawable = imageView.getDrawable();
//        Drawable drawable = getResources().getDrawable(R.drawable.img2);
        View focusView = null;
        if (name.isEmpty() || password.isEmpty() || id.isEmpty()||email.isEmpty()) {
//            e_nickname.setError("이름을 입력해주세요");
//            focusView=e_nickname;
//            cancel=true;
            Toast.makeText(getApplicationContext(),"비어있는 칸이 있습니다.",Toast.LENGTH_LONG).show();
            //String user_email, String user_name, String user_passwd, String user_nick_name, Date user_birth
        }
        else{
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            reqFile = RequestBody.create(MediaType.parse("image/jpeg"),tempFile);
            body = MultipartBody.Part.createFormData(
                    "user_img", "/weetingImg/", reqFile
            );
            r_email = RequestBody.create(MediaType.parse("text/plain"),
                    email);
            r_password = RequestBody.create(MediaType.parse("text/plain"), password);
            r_name = RequestBody.create(MediaType.parse("text/plain"),
                    name);
            r_nickname = RequestBody.create(MediaType.parse("text/plain"), e_nickname.getText().toString());

            startSignUp(password,birth,email,name,id);
            preferenceManager = new PreferenceManager();
            preferenceManager.setString(getApplicationContext(), "name",e_nickname.getText().toString());
            Toast.makeText(getApplicationContext(), "로그인을 해보세요!", Toast.LENGTH_LONG).show();

        }
    }
    private void startSignUp(String password, String birth, String email, String name, String id){

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
        MultipartBody.Part user_img = MultipartBody.Part.createFormData("user_img", tempFile.getName(), requestFile);

        RequestBody user_passwd
                = RequestBody.create(MediaType.parse("text/plain"), password );
        RequestBody user_birth
                = RequestBody.create(MediaType.parse("text/plain"), birth);
        RequestBody user_email
                = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody user_name
                = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody user_nick_name
                = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody user_interests
                = RequestBody.create(MediaType.parse("text/plain"), interests);
        serviceApi.userSignUP(user_passwd,user_birth,user_email,user_name,user_nick_name,user_interests,user_img).enqueue(new Callback<SignUpData.Response>() {
            @Override
            public void onResponse(Call<SignUpData.Response> call, Response<SignUpData.Response> response) {
                SignUpData.Response result = response.body();

                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                if(result.getStatus()==200){
                    System.out.println("sdfsdfsdfdfdfsfsdfsd");
                    finish();
                }
                else if(result.getStatus() == 400){
                    System.out.println("gggggggggggggggggggggg");
                }
                if(response.isSuccessful()){

                }
                else{
                    ResponseBody error =response.errorBody();
                    System.out.println(error.toString());
                }
            }

            @Override
            public void onFailure(Call<SignUpData.Response> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"회원가입 에러",Toast.LENGTH_LONG).show();
                Log.e("회원가입 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }


    public void tedPermission(){//권한 요청 함수
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        }
        else if(requestCode==PICK_ALBUM){
            setImage();
        }
        if(requestCode==PICK_CAMERA && resultCode== Activity.RESULT_OK){
            setImage();
        }
    }
    private void setImage(){
        ImageView imageView = findViewById(R.id.profile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(),options);
        imageView.setImageBitmap(originalBm);
    }
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,"{package_name}.fileprovider",tempFile));
            startActivityForResult(intent, PICK_CAMERA);
        }
    }
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 + 시간
//        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "weetingImg01_";
        // 이미지가 저장될 폴더 이름
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/weetingImg/");
        if (!storageDir.exists()) storageDir.mkdirs();
        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
    public void nickNameCheck(View view){
        e_nickname = findViewById(R.id.sign_upId);
        serviceApi.nickNameOverlap(e_nickname.getText().toString()).enqueue(new Callback<SignUpData.Response>() {
            @Override
            public void onResponse(Call<SignUpData.Response> call, Response<SignUpData.Response> response) {
                SignUpData.Response response1 = response.body();
                if(response1.getStatus()==200){
                    Toast.makeText(SignUpActivity.this,"중복된 아이디가 아닙니다.",Toast.LENGTH_LONG).show();
                }
                else if(response1.getStatus()==300){
                    Toast.makeText(SignUpActivity.this,"중복된 아이디입니다. 다른 아이디를 사용해주세요.",Toast.LENGTH_LONG).show();
                }
                else if(response1.getStatus()==400){
                    Toast.makeText(SignUpActivity.this,response1.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpData.Response> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"닉네임 에러",Toast.LENGTH_LONG).show();
                Log.e("닉네임 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }
    public void emailOverlap(View view){
        e_mail = findViewById(R.id.sign_upEmail);
        serviceApi.emailOverlap(e_mail.getText().toString()).enqueue(new Callback<SignUpData.Response>() {
            @Override
            public void onResponse(Call<SignUpData.Response> call, Response<SignUpData.Response> response) {
                SignUpData.Response response1 = response.body();
                if(response1.getStatus()==200){
                    Toast.makeText(SignUpActivity.this,"중복된 이메일이 아닙니다.",Toast.LENGTH_LONG).show();
                }
                else if(response1.getStatus()==300){
                    Toast.makeText(SignUpActivity.this,"중복된 이메일입니다.",Toast.LENGTH_LONG).show();

                }
                else if(response1.getStatus()==400){
                    Toast.makeText(SignUpActivity.this,response1.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpData.Response> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"이메일 에러",Toast.LENGTH_LONG).show();
                Log.e("이메일 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }
    public void emailCheck(View view){
        emailNum = "";

        e_mail = findViewById(R.id.sign_upEmail);
        serviceApi.emailCheck(e_mail.getText().toString()).enqueue(new Callback<SignUpData.Response>() {
            @Override
            public void onResponse(Call<SignUpData.Response> call, Response<SignUpData.Response> response) {
                final SignUpData.Response response1 = response.body();
                if(response1.getStatus()==200){
                    Toast.makeText(SignUpActivity.this,"이메일 발신 성공",Toast.LENGTH_LONG).show();

                     final EditText editText = new EditText(SignUpActivity.this);
                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(SignUpActivity.this);
                    alBuilder.setMessage("인증번호를 입력해주세요.");
                    alBuilder.setView(editText);
                    // "예" 버튼을 누르면 실행되는 리스너
                    alBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            emailNum = editText.getText().toString();
                            if(emailNum.equals(response1.getToken()))
                                Toast.makeText(SignUpActivity.this,"이메일 인증 완료되었습니다.", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(SignUpActivity.this,"인증번호가 다릅니다. 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                    // "아니오" 버튼을 누르면 실행되는 리스너
                    alBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return; // 아무런 작업도 하지 않고 돌아간다
                        }
                    });
                    alBuilder.setTitle("이메일 인증");
                    alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
                }
                else if(response1.getStatus()==400){
                    Toast.makeText(SignUpActivity.this,response1.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpData.Response> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"이메일 발신 에러",Toast.LENGTH_LONG).show();
                Log.e("이메일 발신 에러",t.getMessage());
                t.printStackTrace();
            }
        });
    }
    public void PersonCheck(View view){
        if(PersonCheckTF()){
            Toast.makeText(getApplicationContext(),"주민번호 인증이 완료되었습니다.", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(),"유효한 주민번호가 아닙니다. 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();

        if(PersonCheckTF() && genderValid <= 0)
            Toast.makeText(getApplicationContext(),"Weeting은 여성만 가입 가능한 어플입니다.", Toast.LENGTH_LONG).show();

    }

    public Boolean PersonCheckTF(){
        EditText editFront = findViewById(R.id.sign_upPersonFront);
        EditText editBack = findViewById(R.id.sign_upPersonBack);
        String leftSid = "";
        String rightSid = "";
        leftSid = editFront.getText().toString();
        rightSid = editBack.getText().toString();
        Boolean valid = false;
        genderValid = 0;
        if (leftSid.length()+rightSid.length() != 13) throw new IllegalArgumentException("주민등록번호 자리수 13자리를 확인하기 바랍니다.");

        // 입력받은 주민번호 앞자리 유효성 검증============================


        int yy = Integer.parseInt(leftSid.substring(0, 2));
        int mm = Integer.parseInt(leftSid.substring(2, 4));
        int dd = Integer.parseInt(leftSid.substring(4, 6));
        int gender = Integer.parseInt(rightSid.substring(0,1));
        if(gender%2==0)
            genderValid++;
        else
            genderValid = 0;
        System.out.println(genderValid + "ffffffffffffffffffffffffffffffffffffff");

        if (yy < 1 || yy > 99 || mm > 12 || mm < 1 || dd < 1 || dd > 31)
            valid = false;
        int digit1 = Integer.parseInt(leftSid.substring(0, 1)) * 2;
        int digit2 = Integer.parseInt(leftSid.substring(1, 2)) * 3;
        int digit3 = Integer.parseInt(leftSid.substring(2, 3)) * 4;
        int digit4 = Integer.parseInt(leftSid.substring(3, 4)) * 5;
        int digit5 = Integer.parseInt(leftSid.substring(4, 5)) * 6;
        int digit6 = Integer.parseInt(leftSid.substring(5, 6)) * 7;

        int digit7 = Integer.parseInt(rightSid.substring(0, 1)) * 8;
        int digit8 = Integer.parseInt(rightSid.substring(1, 2)) * 9;
        int digit9 = Integer.parseInt(rightSid.substring(2, 3)) * 2;
        int digit10 = Integer.parseInt(rightSid.substring(3, 4)) * 3;
        int digit11 = Integer.parseInt(rightSid.substring(4, 5)) * 4;
        int digit12 = Integer.parseInt(rightSid.substring(5, 6)) * 5;

        int last_digit = Integer.parseInt(rightSid.substring(6, 7));

        int error_verify = (digit1 + digit2 + digit3 + digit4 + digit5 + digit6 + digit7 + digit8 + digit9 + digit10 + digit11 + digit12) % 11;

        int sum_digit = 0;
        if (error_verify == 0) {
            sum_digit = 1;
        } else if (error_verify == 1) {
            sum_digit = 0;
        } else {
            sum_digit = 11 - error_verify;
        }

        if (last_digit == sum_digit)
            valid = true;
        else
            valid = false;

        return valid;
    }
    public void TempInterest(View view) throws ParseException {
        CustomDialog customDialog = new CustomDialog(SignUpActivity.this);
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
                    dlg.dismiss();
                try {
                    SignUpCheck();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                }
            });

        }
        public void Filter(){
        int ids = chipGroup.getCheckedChipId();
        interests = "";
        for(int i =0 ; i < chipGroup.getChildCount(); i++){
            Chip chip = (Chip)chipGroup.getChildAt(i);
            if(chip.isChecked()){
                System.out.println(chip.getText().toString()+"sssssssssssssss");
                interests += chip.getText().toString();
                interests = interests + "/";
                interestsCount++;
            }

        }

        }
}

}
