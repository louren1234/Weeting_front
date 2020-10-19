package com.example.again;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMyMoim extends AppCompatActivity {
    private String myimg = null;
    private EditText m_name, m_description, m_num, m_agemin, m_agemax, selectFirstLocation, selectSecondLocation, selectThirdLocation, selectLastLocation;
    private TextView m_time;
    private ImageButton selectDateButton;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener timecallbackMethod;

    //카메라 관련 변수들
    private ImageView m_img;
    private static final String TAG = "camera";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private static final int DELETE_PIC = 3;
    private File tempFile;
    private File imageFile;
    //카메라 각도 관련 변수
    private Boolean isCamera = false;

    private Spinner interestSpinner;
    private MoimEditData.serviceApi serviceApi;
    private MoimEditSameImageData.serviceApi SameImageserviceApi;
    MoimCategoryData.MoimCategoryResponse dataList;
    //    ArrayList<HashMap<String, Integer>> categoryArrayListData;
    HashMap<String, Integer> categoryHashMap;

    //카메라 or 앨범
    private String[] selectCamorAlbum = {"카메라", "앨범", "썸네일 초기화"};
    AlertDialog.Builder mSelectCamOrAlbum;

    Calendar calendar;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_moim);

        Intent intent = getIntent();
        final int meeting_id = intent.getExtras().getInt("meetingId");

        tedPermission();

        interestSpinner = findViewById(R.id.interest);
        m_img = findViewById(R.id.meetingImg);

        m_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mSelectCamOrAlbum.show();
            }
        });

        this.InitializeView();
        this.InitializeListener();
        selectDateButton = findViewById(R.id.selectDate);

        ImageView main = findViewById(R.id.mainpage);
        ImageButton search = findViewById(R.id.search);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton toList = findViewById(R.id.toList);
        ImageButton toMypage = findViewById(R.id.toMypage);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchList.class);
                startActivity(intent);
            }
        });

        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                startActivity(intent);
            }
        });

        toList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MoimList.class);
                intent.putExtra("category", "all");
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });

        toMypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

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
                            .into(m_img);

                }
            }
        });

        MoimCategoryData.serviceApi apiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
        Call<MoimCategoryData.MoimCategoryResponse> call = apiInterface.getSpinnerList();

        call.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {

                dataList = response.body();

                String[] names = new String[dataList.data.size()];
                categoryHashMap = new HashMap<String, Integer>();
                int i = 0;

                for(MoimCategoryData moimCategoryData : dataList.data) {
                    categoryHashMap.put(moimCategoryData.getInterests_name(), moimCategoryData.getInterests_id());
//                    categoryArrayListData.add(categoryHashMap);
                    names[i] = moimCategoryData.getInterests_name();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
//                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), categoryArrayListData,  R.layout.support_simple_spinner_dropdown_item, new String[]{"interest_name", "interest_id"}, new int[]{android.R.id.text1, android.R.id.text2});
                interestSpinner.setAdapter(adapter);

                Log.d("스피너 성공?", dataList.toString());
            }

            @Override
            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {

                Log.d("스피너 실패", t.toString());
            }
        });

        m_name = findViewById(R.id.meetingName);
        m_description = findViewById(R.id.meetingIntro);
        m_time = findViewById(R.id.meetingDate);
        m_num = findViewById(R.id.meetingNum);
        m_agemin = findViewById(R.id.minAge);
        m_agemax = findViewById(R.id.maxAge);
        selectFirstLocation = findViewById(R.id.textFirstLocation);
        selectSecondLocation = findViewById(R.id.textSecondLocation);
        selectThirdLocation = findViewById(R.id.textThirdLocation);
        selectLastLocation = findViewById(R.id.textLastLocation);
        selectDateButton = findViewById(R.id.selectDate);

        serviceApi = RetrofitClient.getClient().create(MoimEditData.serviceApi.class);

//        selectLocationButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(getApplicationContext(), SearchMoimAddress.class);
//                startActivity(intent);
//            }
//        });

        final MoimDetailData.serviceApi MoimInfogetApiInterface = RetrofitClient.getClient().create(MoimDetailData.serviceApi.class);
        Call<MoimDetailData.MoimDetailDataResponse> getMoimInfocall = MoimInfogetApiInterface.getMoimDetail(meeting_id);

        getMoimInfocall.enqueue(new Callback<MoimDetailData.MoimDetailDataResponse>() {
            @Override
            public void onResponse(Call<MoimDetailData.MoimDetailDataResponse> call, Response<MoimDetailData.MoimDetailDataResponse> response) {
                MoimDetailData.MoimDetailDataResponse detailList = response.body();

                for(MoimDetailData moimDetailData : detailList.data){


                    Glide.with(getApplicationContext())
                            .load(moimDetailData.getMeeting_img())
                            .error(R.drawable.error)
                            .fallback(R.drawable.nullimage)
                            .into(m_img);

                    myimg = moimDetailData.getMeeting_img();
                    m_name.setText(moimDetailData.getMeeting_name());
                    m_description.setText(moimDetailData.getMeeting_description());
                    m_time.setText(String.valueOf(moimDetailData.getMeeting_time()));

                    String firstlocation;
                    String secondlocation;
                    String thridlocation;
                    String lastlocation = "";

                    String location = moimDetailData.getMeeting_location();
                    String locationList[] = location.split(" ");

                    try {
                        firstlocation = locationList[0];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        firstlocation = " ";
                    }

                    try {
                        secondlocation = locationList[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        secondlocation = " ";
                    }

                    try {
                        thridlocation = locationList[2];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        thridlocation = " ";
                    }

                    try {
                        for (int i = 3; i < locationList.length; i++) {
                            lastlocation = lastlocation + " " + locationList[i];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        lastlocation = " ";
                    }

                        selectFirstLocation.setText(firstlocation);
                        selectSecondLocation.setText(secondlocation);
                        selectThirdLocation.setText(thridlocation);
                        selectLastLocation.setText(lastlocation);

//                    try {
//                        selectFirstLocation.setText(firstlocation);
//                        selectSecondLocation.setText(secondlocation);
//                        selectThirdLocation.setText(thridlocation);
//                        selectLastLocation.setText(lastlocation);
//                    } catch (NullPointerException e) {
//                        firstlocation = " ";
//                        secondlocation = " ";
//                        thridlocation = " ";
//                        lastlocation =" ";
//                        selectFirstLocation.setText(firstlocation);
//                        selectSecondLocation.setText(secondlocation);
//                        selectThirdLocation.setText(thridlocation);
//                        selectLastLocation.setText(lastlocation);
//                    }

                    m_num.setText(String.valueOf(moimDetailData.getMeeting_recruitment()));
                    m_agemin.setText(String.valueOf(moimDetailData.getAge_limit_min()));
                    m_agemax.setText(String.valueOf(moimDetailData.getAge_limit_max()));
                    interestSpinner.setSelection(moimDetailData.getFk_meeting_interest() - 1);

                }

            }
            @Override
            public void onFailure(Call<MoimDetailData.MoimDetailDataResponse> call, Throwable t) {
                Toast.makeText(UpdateMyMoim.this, "모임 디테일 에러", Toast.LENGTH_LONG).show();
                Log.e("모임 디테일 에러", t.getMessage());
                t.printStackTrace();
            }
        });



    }

    public void editMoimValid(View view) throws ParseException {

        Intent intent = getIntent();
        final int meeting_id = intent.getExtras().getInt("meetingId");


        m_name.setError(null);
        m_description.setError(null);
        m_time.setError(null);
        m_num.setError(null);
        m_agemin.setError(null);
        m_agemax.setError(null);
        selectFirstLocation.setError(null);
        selectSecondLocation.setError(null);
        selectThirdLocation.setError(null);
        selectLastLocation.setError(null);

        final String m_interest = interestSpinner.getSelectedItem().toString();
//        int interest = categoryHashMap.get(m_interest);
        Log.d(TAG, "interest 안에 뭐가 들어있니 : " + m_interest);

        final String name = m_name.getText().toString();
        final String description = m_description.getText().toString();
        final String num = m_num.getText().toString();
        final String agemin = m_agemin.getText().toString();
        final String agemax = m_agemax.getText().toString();
        final String firstLocation = selectFirstLocation.getText().toString();
        final String secondLocation = selectSecondLocation.getText().toString();
        final String thirdLocation = selectThirdLocation.getText().toString();
        final String lastLocation = selectLastLocation.getText().toString();
        final String location = firstLocation + " " + secondLocation + " " + thirdLocation + " " + lastLocation;
        final String time = m_time.getText().toString();
//        SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
//        Date timeDate = trans.parse(time);

        if (name.isEmpty() || description.isEmpty() || time.isEmpty() || location.isEmpty() ||
                num.isEmpty() || agemin.isEmpty() || agemax.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빈 칸 존재", Toast.LENGTH_LONG).show();
        }
        else {
            int numInt = Integer.parseInt(num);
            int ageminInt = Integer.parseInt(agemin);
            int agemaxInt = Integer.parseInt(agemax);
//            int interest = Integer.parseInt(m_interest);
            if ( ageminInt > agemaxInt ) {
                Toast.makeText(getApplicationContext(), "최소 인원이 최대 인원보다 크게 설정되어 있습니다.", Toast.LENGTH_LONG).show();
            } else {
                if( numInt < 5 ){
                    Toast.makeText(getApplicationContext(), "모임원은 5명 이상으로 설정해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    startEditMoim(new MoimEditData(m_interest, name, description, location, time, numInt, ageminInt, agemaxInt, meeting_id));
                }
            }
        }
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
            m_img.setImageBitmap(originalBm);

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

    public void startEditMoim(MoimEditData data) {

        Log.d(TAG, "updateImage : 여기까진 들어왔음 startUpdateMoim");
        Log.d(TAG, "데이터가 들어있니?" + data.getMeeting_location()); // 있는데ㅡㅡ

        RequestBody meeting_interest
                = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_interest() );
        RequestBody meeting_name
                = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_name() );
        RequestBody meeting_description
                = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_description() );
        RequestBody meeting_time
                = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getMeeting_time()) );
        RequestBody meeting_location
                = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_location() );
        RequestBody meeting_recruitment
                = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getMeeting_recruitment()) );
        RequestBody age_limit_min
                = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getAge_limit_min()) );
        RequestBody age_limit_max
                = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getAge_limit_max()) );
        RequestBody meeting_id
                = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getMeeting_id()) );

        final int meetingid = data.getMeeting_id();

        MultipartBody.Part body;

        if( tempFile == null || tempFile.length() <= 0) {

            if ( myimg != null ) { // 이미지가 기존 이미지라면

//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile );
//                body = MultipartBody.Part.createFormData("meeting_img", tempFile.getName(), requestFile);

//                SameImageserviceApi = RetrofitClient.getClient().create(MoimEditSameImageData.serviceApi.class);
////                SameImageserviceApi.editSameImgMoim(data.getMeeting_interest(), data.getMeeting_name(), data.getMeeting_description(), data.getMeeting_time(), data.getMeeting_location(), data.getMeeting_recruitment(), data.getAge_limit_min(), data.getAge_limit_max(), data.getMeeting_id()).enqueue(new Callback<MoimEditSameImageData.MoimEditSameImageDataResponse>(){
//                SameImageserviceApi.editSameImgMoim(meeting_interest, meeting_name, meeting_description, meeting_time, meeting_location, meeting_recruitment, age_limit_min, age_limit_max, meeting_id).enqueue(new Callback<MoimEditSameImageData.MoimEditSameImageDataResponse>(){
//                    @Override
//                    public void onResponse(Call<MoimEditSameImageData.MoimEditSameImageDataResponse> call, Response<MoimEditSameImageData.MoimEditSameImageDataResponse> response) {
//                        MoimEditSameImageData.MoimEditSameImageDataResponse result = response.body();
//
//                        Toast.makeText(UpdateMyMoim.this, result.getMessage(), Toast.LENGTH_LONG).show();
//
//                        if(result.getState() == 200) {
//                            System.out.println("yeah");
//                            Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
//                            intent.putExtra("meetingId", meetingid);
//                            startActivity(intent);
//                        }
//                        else if(result.getState() == 400) {
//                            System.out.println("nooooo");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoimEditSameImageData.MoimEditSameImageDataResponse> call, Throwable t) {
//                        Toast.makeText(UpdateMyMoim.this, "모임수정 에러", Toast.LENGTH_LONG).show();
//                        Log.e("모임수정 에러", t.getMessage());
//                        t.printStackTrace();
//                    }
//                });

                serviceApi.editSameImgMoim(data).enqueue(new Callback<MoimEditData.MoimEditDataResponse>(){
                    @Override
                    public void onResponse(Call<MoimEditData.MoimEditDataResponse> call, Response<MoimEditData.MoimEditDataResponse> response) {
                        MoimEditData.MoimEditDataResponse result = response.body();

                        Toast.makeText(UpdateMyMoim.this, result.getMessage(), Toast.LENGTH_LONG).show();

                        if(result.getState() == 200) {
                            System.out.println("yeah");
                            Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
                            intent.putExtra("meetingId", meetingid);
                            startActivity(intent);
                            finish();
                        }
                        else if(result.getState() == 400) {
                            System.out.println("nooooo");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoimEditData.MoimEditDataResponse> call, Throwable t) {
                        Toast.makeText(UpdateMyMoim.this, "모임수정 에러", Toast.LENGTH_LONG).show();
                        Log.e("모임수정 에러", t.getMessage());
                        t.printStackTrace();
                    }
                });

            }else{

//                RequestBody meeting_img
//                        = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(null) );

                serviceApi.editNullImgMoim(meeting_interest, meeting_name, meeting_description, meeting_time, meeting_location, meeting_recruitment, age_limit_min, age_limit_max, meeting_id, null).enqueue(new Callback<MoimEditData.MoimEditDataResponse>(){
                    @Override
                    public void onResponse(Call<MoimEditData.MoimEditDataResponse> call, Response<MoimEditData.MoimEditDataResponse> response) {
                        MoimEditData.MoimEditDataResponse result = response.body();

                        Toast.makeText(UpdateMyMoim.this, result.getMessage(), Toast.LENGTH_LONG).show();

                        if(result.getState() == 200) {
                            System.out.println("yeah");
                            Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
                            intent.putExtra("meetingId", meetingid);
                            startActivity(intent);
                            finish();
                        }
                        else if(result.getState() == 400) {
                            System.out.println("nooooo");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoimEditData.MoimEditDataResponse> call, Throwable t) {
                        Toast.makeText(UpdateMyMoim.this, "모임수정 에러", Toast.LENGTH_LONG).show();
                        Log.e("모임수정 에러", t.getMessage());
                        t.printStackTrace();
                    }
                });

            }

        }else{
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile );
            body = MultipartBody.Part.createFormData("meeting_img", tempFile.getName(), requestFile);

            serviceApi.editUpdateImgMoim(meeting_interest, meeting_name, meeting_description, meeting_time, meeting_location, meeting_recruitment, age_limit_min, age_limit_max, meeting_id, body).enqueue(new Callback<MoimEditData.MoimEditDataResponse>(){
                //        serviceApi.createMoim(data).enqueue(new Callback<MoimData.MoimResponse>(){
                @Override
                public void onResponse(Call<MoimEditData.MoimEditDataResponse> call, Response<MoimEditData.MoimEditDataResponse> response) {
                    MoimEditData.MoimEditDataResponse result = response.body();

                    Toast.makeText(UpdateMyMoim.this, result.getMessage(), Toast.LENGTH_LONG).show();

                    if(result.getState() == 200) {
                        System.out.println("yeah");
                        Intent intent = new Intent(getApplicationContext(), MoimDetail.class);
                        intent.putExtra("meetingId", meetingid);
                        startActivity(intent);
                        finish();
                    }
                    else if(result.getState() == 400) {
                        System.out.println("nooooo");
                    }
                }

                @Override
                public void onFailure(Call<MoimEditData.MoimEditDataResponse> call, Throwable t) {
                    Toast.makeText(UpdateMyMoim.this, "모임수정 에러", Toast.LENGTH_LONG).show();
                    Log.e("모임수정 에러", t.getMessage());
                    t.printStackTrace();
                }
            });
        }

    }

    //     달력 함수

    public void InitializeView()
    {
        m_time = findViewById(R.id.meetingDate);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int newyear, int monthOfYear, int dayOfMonth)
            {
                year = newyear;
                month = monthOfYear + 1;
                day = dayOfMonth;

                timeSet(); // 시간 정하면 좋을 것 같아서 넣음


// 날짜만 정하는거면 밑 두 줄만 해도 됨. timeSet() 함수랑 timecallbackMethod 지우고.
//                int setmonthOfYear = monthOfYear + 1;
//                m_time.setText(year + "-" + setmonthOfYear + "-" + dayOfMonth);
            }
        };

        timecallbackMethod = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int newminute)
            {
//                textView_Date.setText(hourOfDay + "시" + minute + "분");
                hour = hourOfDay;
                minute = newminute;
                m_time.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
            }
        };

    }

    public void OnClickHandler(View view)
    {
        calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, nowYear, nowMonth, nowDay);
        dialog.show();
    }

    public void timeSet(){
        TimePickerDialog timedialog = new TimePickerDialog(this, timecallbackMethod, 8, 10, true);
        timedialog.show();
    }


}
