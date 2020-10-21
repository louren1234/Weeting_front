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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create extends AppCompatActivity {
    private EditText m_name, m_description, m_num, m_agemin, m_agemax, selectFirstLocation, selectSecondLocation, selectThirdLocation, selectLastLocation;
    private TextView m_time,selectLocationButton;
//    private ImageButton selectLocationButton;
    private ImageButton selectDateButton;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener timecallbackMethod;

    //주소 관련 변수
    private RadioGroup selectAddressOrNot;
    private RadioButton selectAddress, selectNoAddress;
    private LinearLayout showAddress;

    private Spinner spinnerCity, spinnerSigungu, spinnerDong;
    private ArrayAdapter<String> arrayAdapter;
    String address;
    String location;

    //카메라 관련 변수들
    private ImageView m_img;
    private static final String TAG = "camera";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    //카메라 각도 관련 변수
    private Boolean isCamera = false;

    private Spinner interestSpinner;
    private MoimData.ServiceApi serviceApi;
    MoimCategoryData.MoimCategoryResponse dataList;
//    ArrayList<HashMap<String, Integer>> categoryArrayListData;
    HashMap<String, Integer> categoryHashMap;

    //카메라 or 앨범
    private String[] selectCamorAlbum = {"카메라", "앨범"};
    AlertDialog.Builder mSelectCamOrAlbum;

    Calendar calendar;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        spinnerCity = (Spinner)findViewById(R.id.city);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.spinner_region));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(arrayAdapter);

        spinnerSigungu = (Spinner)findViewById(R.id.region);
        spinnerDong = (Spinner)findViewById(R.id.neighborhood);

        initAddressSpinner();

        tedPermission();

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
                }
            }
        });

//        m_img.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
//                if(isPermission) goToAlbum();
//                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        m_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
//                if(isPermission)  takePhoto();
//                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
//            }
//        });

        MoimCategoryData.serviceApi apiInterface = RetrofitClient.getClient().create(MoimCategoryData.serviceApi.class);
        Call<MoimCategoryData.MoimCategoryResponse> call = apiInterface.getSpinnerList();

        call.enqueue(new Callback<MoimCategoryData.MoimCategoryResponse>() {
            @Override
            public void onResponse(Call<MoimCategoryData.MoimCategoryResponse> call, Response<MoimCategoryData.MoimCategoryResponse> response) {

                dataList = response.body();

                // 이거로 하면 하나의 값을 spinner에 저장 가능
//                String[] names = new String[dataList.data.size()];
//
//                int i = 0;
//                for(MoimCategoryData moimCategoryData : dataList.data) {
//                    names[i] = moimCategoryData.getInterests_name();
//                    i++;
//                }

//                categoryArrayListData = new ArrayList<HashMap<String, Integer>>();

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

                Log.d("MoimList 성공?", dataList.toString());
            }

            @Override
            public void onFailure(Call<MoimCategoryData.MoimCategoryResponse> call, Throwable t) {

                Log.d("MoimList 실패", t.toString());
            }
        });

        m_name = findViewById(R.id.meetingName);
        m_description = findViewById(R.id.meetingIntro);
        m_time = findViewById(R.id.meetingDate);
        m_num = findViewById(R.id.meetingNum);
        m_agemin = findViewById(R.id.minAge);
        m_agemax = findViewById(R.id.maxAge);
        selectLastLocation = findViewById(R.id.textLastLocation);
        selectDateButton = findViewById(R.id.selectDate);
        selectAddressOrNot = findViewById(R.id.selectAddressOrNot);
        selectAddress = findViewById(R.id.selectAddress);
        selectNoAddress = findViewById(R.id.selectNoAddress);
        showAddress = findViewById(R.id.showAddress);

        serviceApi = RetrofitClient.getClient().create(MoimData.ServiceApi.class);

        selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SearchMoimAddress.class);
                startActivity(intent);
            }
        });

//        selectNoAddress.setOnClickListener();
        selectAddressOrNot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.selectAddress) {
                    showAddress.setVisibility(View.VISIBLE);
                    address = "";
                    if (spinnerCity.getSelectedItemPosition() != 0 && spinnerSigungu.getSelectedItemPosition() !=0 && spinnerDong.getSelectedItemPosition() != 0) {
                        address = spinnerCity.getSelectedItem().toString() + " " + spinnerSigungu.getSelectedItem().toString() + " " + spinnerDong.getSelectedItem().toString();
                    }
                    else if (spinnerCity.getSelectedItemPosition() != 0 && spinnerSigungu.getSelectedItemPosition() !=0) {
                        address = spinnerCity.getSelectedItem().toString() +" "+ spinnerSigungu.getSelectedItem().toString() + " " + " ";
                    }
                    else if(spinnerCity.getSelectedItemPosition()!=0){
                        address = spinnerCity.getSelectedItem().toString() + " " + " " + " " + " ";
                    }
                    if (spinnerCity.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "시를 선택해주세요! ", Toast.LENGTH_LONG).show();
                    }

                    String lastLocation = selectLastLocation.getText().toString();
                    location = address + " " + lastLocation;
                }
                else if(checkedId == R.id.selectNoAddress) {
                    showAddress.setVisibility(View.GONE);
                    location = "장소 미정";
                }
            }
        });
    }

    public void createMoimValid(View view) throws ParseException {

        m_name.setError(null);
        m_description.setError(null);
        m_time.setError(null);
        m_num.setError(null);
        m_agemin.setError(null);
        m_agemax.setError(null);
            String m_interest = interestSpinner.getSelectedItem().toString();
//        int interest = categoryHashMap.get(m_interest);
            Log.d(TAG, "interest 안에 뭐가 들어있니 : " + m_interest);

            String name = m_name.getText().toString();
            String description = m_description.getText().toString();
            String num = m_num.getText().toString();
            String agemin = m_agemin.getText().toString();
            String agemax = m_agemax.getText().toString();

//        BitmapDrawable drawable = (BitmapDrawable) m_img.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);

//        BitmapDrawable drawable = (BitmapDrawable) m_img.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        String img = bitmap.toString();

            String time = m_time.getText().toString();
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
                        startCreateMoim(new MoimData(m_interest, name, description, time, location, numInt, ageminInt, agemaxInt));
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

    public void startCreateMoim(MoimData data) {

        Log.d(TAG, "setImage : 여기까진 들어왔음 startCreateMoim");

        //  사진이 없다면 toast와 함께 return.
        if (tempFile == null || tempFile.length() <= 0) {
            Toast.makeText(getApplicationContext(), "사진이 없음", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "setImage : startCreateMoim : temp가 null이야");

//            return;

            serviceApi.createImageNullMoim(data).enqueue(new Callback<MoimData.MoimResponse>() {
                //        serviceApi.createMoim(data).enqueue(new Callback<MoimData.MoimResponse>(){
                @Override
                public void onResponse(Call<MoimData.MoimResponse> call, Response<MoimData.MoimResponse> response) {
                    MoimData.MoimResponse result = response.body();

                    Toast.makeText(Create.this, result.getMessage(), Toast.LENGTH_LONG).show();

                    if (result.getStatus() == 200) {
                        System.out.println("yeah");
                        Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                        startActivity(intent);
                        finish();
                    } else if (result.getStatus() == 400) {
                        System.out.println("nooooo");
                    }
                }

                @Override
                public void onFailure(Call<MoimData.MoimResponse> call, Throwable t) {
                    Toast.makeText(Create.this, "모임생성 에러", Toast.LENGTH_LONG).show();
                    Log.e("모임생성 에러1", t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("meeting_img", tempFile.getName(), requestFile);

            RequestBody meeting_interest
                    = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_interest());
            RequestBody meeting_name
                    = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_name());
            RequestBody meeting_description
                    = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_description());
            RequestBody meeting_time
                    = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getMeeting_time()));
            RequestBody meeting_location
                    = RequestBody.create(MediaType.parse("text/plain"), data.getMeeting_location());
            RequestBody meeting_recruitment
                    = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getMeeting_recruitment()));
            RequestBody age_limit_min
                    = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getAge_limit_min()));
            RequestBody age_limit_max
                    = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(data.getAge_limit_max()));


//        serviceApi.createMoim(data.getMeeting_interest(), data.getMeeting_name(), data.getMeeting_description(), data.getMeeting_time(), data.getMeeting_location(), data.getMeeting_recruitment(), data.getAge_limit_min(), data.getAge_limit_max() , body).enqueue(new Callback<MoimData.MoimResponse>(){
            serviceApi.createMoim(meeting_interest, meeting_name, meeting_description, meeting_time, meeting_location, meeting_recruitment, age_limit_min, age_limit_max, body).enqueue(new Callback<MoimData.MoimResponse>() {
                //        serviceApi.createMoim(data).enqueue(new Callback<MoimData.MoimResponse>(){
                @Override
                public void onResponse(Call<MoimData.MoimResponse> call, Response<MoimData.MoimResponse> response) {
                    MoimData.MoimResponse result = response.body();

                    Toast.makeText(Create.this, result.getMessage(), Toast.LENGTH_LONG).show();

                    if (result.getStatus() == 200) {
                        System.out.println("yeah");
                        Toast.makeText(getApplicationContext(), "모임을 확인해보세요!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                        startActivity(intent);
                        finish();
                    } else if (result.getStatus() == 400) {
                        System.out.println("nooooo");
                    }
                }

                @Override
                public void onFailure(Call<MoimData.MoimResponse> call, Throwable t) {
                    Toast.makeText(Create.this, "모임생성 에러", Toast.LENGTH_LONG).show();
                    Log.e("모임생성 에러2", t.getMessage());
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

    private void initAddressSpinner() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 시군구, 동의 스피너를 초기화한다.
                switch (position) {
                    case 0:
                        spinnerSigungu.setAdapter(null);
                        break;
                    case 1:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 2:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 3:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 4:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 5:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 6:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 7:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 8:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 9:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 10:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 11:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 12:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_nam);

                        break;
                    case 13:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 14:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 15:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 16:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 17:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSigungu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 서울특별시 선택시
                if(spinnerCity.getSelectedItemPosition() == 1 && spinnerSigungu.getSelectedItemPosition() > -1) {
                    switch(position) {
                        //25
                        case 0:
                            break;
                        case 1:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangnam);
                            break;
                        case 2:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangdong);
                            break;
                        case 3:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangbuk);
                            break;
                        case 4:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangseo);
                            break;
                        case 5:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwanak);
                            break;
                        case 6:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwangjin);
                            break;
                        case 7:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_guro);
                            break;
                        case 8:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_geumcheon);
                            break;
                        case 9:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_nowon);
                            break;
                        case 10:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dobong);
                            break;
                        case 11:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongdaemun);
                            break;
                        case 12:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongjag);
                            break;
                        case 13:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_mapo);
                            break;
                        case 14:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seodaemun);
                            break;
                        case 15:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seocho);
                            break;
                        case 16:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongdong);
                            break;
                        case 17:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongbuk);
                            break;
                        case 18:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_songpa);
                            break;
                        case 19:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yangcheon);
                            break;
                        case 20:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yeongdeungpo);
                            break;
                        case 21:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yongsan);
                            break;
                        case 22:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_eunpyeong);
                            break;
                        case 23:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jongno);
                            break;
                        case 24:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jung);
                            break;
                        case 25:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jungnanggu);
                            break;
                    }
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSigunguSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerSigungu.setAdapter(null);
            arrayAdapter = null;
        }

        if (spinnerCity.getSelectedItemPosition() > 1) {
            spinnerDong.setAdapter(null);
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSigungu.setAdapter(arrayAdapter);
    }

    private void setDongSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerDong.setAdapter(null);
            arrayAdapter = null;
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDong.setAdapter(arrayAdapter);
    }

}
