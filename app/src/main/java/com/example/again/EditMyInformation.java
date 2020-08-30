package com.example.again;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyInformation extends AppCompatActivity {

    private static final String TAG = "edit myInfo";

    private ImageView myImage;
    private TextView myEmail, myBirth, myNickname;
    private EditText myIntroduce;
    private Button editMyInfoButton;

    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    //카메라 각도 관련 변수
    private Boolean isCamera = false;
    private String myimg, myintro;

    //카메라 or 앨범
    private String[] selectCamorAlbum = {"카메라", "앨범", "썸네일 초기화"};
    AlertDialog.Builder mSelectCamOrAlbum;

    UserData.serviceApi serviceApi;
    UserData.UserDataResponse dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_profile);

        myImage = findViewById(R.id.myImage);
        myEmail = findViewById(R.id.myEmail);
        myBirth = findViewById(R.id.myBirth);
        myNickname = findViewById(R.id.myNickname);
        myIntroduce = findViewById(R.id.myIntroduce);
        editMyInfoButton = findViewById(R.id.editMyInfoButton);

        setInfo();

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectCamOrAlbum.show();
            }
        });

        editMyInfoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editMyInfo();
            }
        });

        tedPermission();

        mSelectCamOrAlbum = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        mSelectCamOrAlbum.setTitle("모임 썸네일 설정").setItems(selectCamorAlbum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectCamorAlbum[which].equals("앨범")){
                    // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                    if(isPermission) goToAlbum();
                    else Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

                } else if (selectCamorAlbum[which].equals("카메라")) {
                    // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                    if(isPermission)  takePhoto();
                    else Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                } else if (selectCamorAlbum[which].equals("썸네일 초기화")){
                    tempFile = null;
                    myimg = null;
//                    m_img.setImageBitmap(null); // 이거로해도 되긴 되는데 그라이드 사용할거야
                    Glide.with(getApplicationContext())
                            .load(R.drawable.nullimage)
                            .error(R.drawable.error)
                            .into(myImage);
                }
            }
        });

    }

    protected void setInfo(){
        serviceApi = RetrofitClient.getClient().create(UserData.serviceApi.class);
        Call<UserData.UserDataResponse> call = serviceApi.getMyInfo();

        call.enqueue(new Callback<UserData.UserDataResponse>() {
            @Override
            public void onResponse(Call<UserData.UserDataResponse> call, Response<UserData.UserDataResponse> response) {
                dataList = response.body();

                for( UserData userData : dataList.list ){
                    Glide.with(getApplicationContext())
                            .load(userData.getUser_img())
                            .error(R.drawable.error)
                            .fallback(R.drawable.nullimage)
                            .into(myImage);

                    myimg = userData.getUser_img();
                    myintro = userData.getUser_introduce();
                    myEmail.setText(userData.getUser_email());
                    myBirth.setText(userData.getUser_birth());
                    myIntroduce.setText(userData.getUser_introduce());
                    myNickname.setText(userData.getUser_nick_name());
                }
            }

            @Override
            public void onFailure(Call<UserData.UserDataResponse> call, Throwable t) {
                Log.d(TAG, "실패원인 : ", t);
            }
        });
    }

    // 카메라 관련
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {

                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        switch (requestCode) {

            case PICK_FROM_ALBUM: {

                Uri photoUri = data.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case PICK_FROM_CAMERA: {

                Uri photoUri = Uri.fromFile(tempFile);
                Log.d(TAG, "takePhoto photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case Crop.REQUEST_CROP: {
                //File cropFile = new File(Crop.getOutput(data).getPath());
                setImage();

            }
        }
    }

    /**
     *  Crop 기능
     */
    private void cropImage(Uri photoUri) {

        Log.d(TAG, "tempFile : " + tempFile);

        /**
         *  갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해줍니다.
         */
        if(tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        //크롭 후 저장할 Uri
        Uri savingUri = Uri.fromFile(tempFile);

        Crop.of(photoUri, savingUri).asSquare().start(this);
    }

    /**
     *  앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     *  카메라에서 이미지 가져오기
     */
    private void takePhoto() {
        isCamera = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.again.fileprovider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /**
     *  폴더 및 파일 만들기
     */
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( weeting_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "weeting_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( weeting )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/weeting/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {


        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        myImage.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */

//        tempFile = null;
    }

    /**
     *  권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    protected void editMyInfo() {

        myintro = myIntroduce.getText().toString();

        Log.d(TAG, "이미지 값 : " + myimg + " 자기소개 " + myintro);

        if( tempFile == null || tempFile.length() <= 0) { // 새로운 이미지 업데이트가 없고

            if (myimg != null) { // 기존 이미지가 존재한다면

                Call<UserData.UserImgorIntroResponse> calls = serviceApi.updateMyIntro(myintro);
                calls.enqueue(new Callback<UserData.UserImgorIntroResponse>() {
                    @Override
                    public void onResponse(Call<UserData.UserImgorIntroResponse> call, Response<UserData.UserImgorIntroResponse> response) {
                        UserData.UserImgorIntroResponse response2 = response.body();

                        if (response2.getState() == 200) {
                            Intent intent = new Intent(getApplicationContext(), Mypage.class);
                            startActivity(intent);
                        }
                        else if (response2.getState() == 400) {
                            Log.e("내 소개 수정 오류", "오류가 있다.");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserData.UserImgorIntroResponse> call, Throwable t) {
                        Log.e("내 intro 수정 통신 자체 실패", t.getMessage());
                        t.printStackTrace();
                    }
                });

            } else { // 기존 이미지도 null이라면

//                RequestBody myNullImage
//                        = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(null));

                Call<UserData.UserImgorIntroResponse> call = serviceApi.updateMyImg(null);
                call.enqueue(new Callback<UserData.UserImgorIntroResponse>() {
                    @Override
                    public void onResponse(Call<UserData.UserImgorIntroResponse> call, Response<UserData.UserImgorIntroResponse> response) {

                        Call<UserData.UserImgorIntroResponse> calls = serviceApi.updateMyIntro(myintro);
                        calls.enqueue(new Callback<UserData.UserImgorIntroResponse>() {
                            @Override
                            public void onResponse(Call<UserData.UserImgorIntroResponse> call, Response<UserData.UserImgorIntroResponse> response) {
                                UserData.UserImgorIntroResponse response2 = response.body();

                                if(response2.getState() == 200){
                                    Intent intent = new Intent(getApplicationContext(), Mypage.class);
                                    startActivity(intent);
                                }else {
                                    Log.e("이미지 null & introduction보냄", "오류");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserData.UserImgorIntroResponse> call, Throwable t) {
                                Log.e("내 intro 수정 에러", t.getMessage());
                                t.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<UserData.UserImgorIntroResponse> call, Throwable t) {
                        Log.e("내 img 수정 에러", t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        } else { // 새로운 이미지가 있다면

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_img", tempFile.getName(), requestFile);

            Call<UserData.UserImgorIntroResponse> call = serviceApi.updateMyNewImg(body);

            call.enqueue(new Callback<UserData.UserImgorIntroResponse>(){
                //        serviceApi.createMoim(data).enqueue(new Callback<MoimData.MoimResponse>(){
                @Override
                public void onResponse(Call<UserData.UserImgorIntroResponse> call, Response<UserData.UserImgorIntroResponse> response) {
                    UserData.UserImgorIntroResponse result = response.body();

//                    Toast.makeText(EditMyInformation.this, result.getMessage(), Toast.LENGTH_LONG).show();

                    if(result.getState() == 200) {

                        Call<UserData.UserImgorIntroResponse> calls = serviceApi.updateMyIntro(myintro);
                        calls.enqueue(new Callback<UserData.UserImgorIntroResponse>() {
                            @Override
                            public void onResponse(Call<UserData.UserImgorIntroResponse> call, Response<UserData.UserImgorIntroResponse> response) {
                                System.out.println("yeah");
                                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<UserData.UserImgorIntroResponse> call, Throwable t) {
                                Log.e("내 intro 수정 에러", t.getMessage());
                                t.printStackTrace();
                            }
                        });
                    }
                    else if(result.getState() == 400) {
                        System.out.println("nooooo");
                    }
                }

                @Override
                public void onFailure(Call<UserData.UserImgorIntroResponse> call, Throwable t) {
                    Toast.makeText(EditMyInformation.this, "모임수정 에러", Toast.LENGTH_LONG).show();
                    Log.e("모임수정 에러", t.getMessage());
                    t.printStackTrace();
                }
            });

        }
    }
}
