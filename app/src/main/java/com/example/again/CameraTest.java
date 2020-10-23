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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraTest extends AppCompatActivity {

    //카메라 관련 변수들
    private ImageView m_img;
    private static final String TAG = "camera";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    //카메라 각도 관련 변수
    private Boolean isCamera = false;

    private String[] selectCamorAlbum = {"카메라", "앨범"};
    AlertDialog.Builder mSelectCamOrAlbum;

    private String[] selectTest = {"메인으로", "리스트로"};
    AlertDialog.Builder mselectTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameratest);

        tedPermission();

        Button camera = findViewById(R.id.camera);
        Button test2 = findViewById(R.id.test2);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectCamOrAlbum.show();
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mselectTest.show();
            }
        });

        m_img = findViewById(R.id.myImageTest);

        mSelectCamOrAlbum = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        mSelectCamOrAlbum.setTitle("모임 썸네일 설정").setItems(selectCamorAlbum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectCamorAlbum[which].equals("앨범")){
                    // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                    if(isPermission){
                        goToAlbum();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    }

                } else if (selectCamorAlbum[which].equals("카메라")) {
                    // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                    if(isPermission) {
                        takePhoto();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mselectTest = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        mselectTest.setTitle("모임 썸네일 설정").setItems(selectTest, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectTest[which].equals("메인으로")){
                    Intent intent = new Intent(getApplicationContext(), After_have_group.class);
                    startActivity(intent);
                } else if (selectTest[which].equals("리스트로")) {
                    Intent intent = new Intent(getApplicationContext(), MoimList.class);
                    intent.putExtra("category", "all");
                    startActivity(intent);
                }
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

//        Intent intent = new Intent(Intent.ACTION_PICK); 이거로하니까 에러뜸 (강제종료됨)
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // 이거로 하니까 해결됨

//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE); // 이거로하면 전체 파일 관리자에서 선택해서 가져올 수 있음.

        intent.setType("image/*"); // 이건 앨범에서 가져올 수 있음.

        Toast.makeText(getApplicationContext(), "여기까지 들어오는지", Toast.LENGTH_LONG).show();

        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     *  카메라에서 이미지 가져오기
     */
    private void takePhoto() {
        Toast.makeText(getApplicationContext(), "여기까지 들어오는지", Toast.LENGTH_LONG).show(); // 안 들어옴.

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//
//
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//
//        try {
//            tempFile = createImageFile();
//        } catch (IOException e) {
//            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            finish();
//            e.printStackTrace();
//        }
//            // Continue only if the File was successfully created
//            if (tempFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.again.provider",
//                        tempFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "여기까지 들어오는지" + tempFile, Toast.LENGTH_LONG).show();
//        }


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
                        "com.example.again.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_FROM_CAMERA); // 여기서 startActivity 할 때 에러가 남. 죽겠네
                }

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(intent, PICK_FROM_CAMERA);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_FROM_CAMERA); // 여기서 startActivity 할 때 에러가 남. 죽겠네
                }

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
}
