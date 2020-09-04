package com.example.billinavi;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class MyRoom extends Fragment {
    private Button info_correct;
    static public ImageView user_profile,background;
    static public TextView userName, userTeam, userRate, userHigh, userWinRate;
    private static final int PICK_FROM_ALBUM_B = 1;
    private static final int PICK_FROM_CAMERA_B = 0;
    private static final int PICK_FROM_ALBUM_U = 3;
    private static final int PICK_FROM_CAMERA_U = 2;
    private Uri imgUri, photoURI, albumURI;
    private String mCurrentPhotoPath;
    private ImageButton backChange, userChange;
    private int select;


    public MyRoom() {
    }

    public static MyRoom newInstance() {
        Bundle args = new Bundle();
        MyRoom fragment = new MyRoom();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myroom, container, false);
        {
            user_profile = view.findViewById(R.id.user_profile);
            background = view.findViewById(R.id.background_myroom);
            backChange = view.findViewById(R.id.backPhotoChange_btn);
            userChange = view.findViewById(R.id.userImage_change_myroom);
            info_correct = view.findViewById(R.id.info_correct);
            user_profile.setBackground(new ShapeDrawable(new OvalShape()));
            user_profile.setClipToOutline(true);
        }
        //url입력
        {
            String url = "";
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();
        }

        //카메라 설정
        {
            //카메라 권한 획득
            info_correct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.userIndex < 0) {
                        Toast.makeText(view.getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), SettingSub2.class);
                        startActivity(intent);
                    }
                }
            });


            //카메라 다이얼로그 띄우기
            backChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.userIndex < 0) {
                        Toast.makeText(view.getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        PermissionListener permissionListener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            }
                        };
                        TedPermission.with(getContext())
                                .setPermissionListener(permissionListener)
                                .setDeniedMessage("만약 거부하신다면 이 서비스를 사용할 수 없습니다.\n\n[설정]>[사용 권한]에서 사용 권한을 설정해주세요.")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                .check();
                        setImage();
                        makeDialog();
                    }
                }
            });
            userChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.userIndex < 0) {
                        Toast.makeText(view.getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        PermissionListener permissionListener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            }
                        };
                        TedPermission.with(getContext())
                                .setPermissionListener(permissionListener)
                                .setDeniedMessage("만약 거부하신다면 이 서비스를 사용할 수 없습니다.\n\n[설정]>[사용 권한]에서 사용 권한을 설정해주세요.")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                .check();
                        setImage();
                        makeDialog2();
                    }
                }
            });
        }

        return view;
    }

    //다이얼로그 띄우기
    private void makeDialog() {
        final CameraPermissionDialog oDialog = new CameraPermissionDialog(getContext());
        oDialog.show();
        select = 1;
        CameraPermissionDialog.takePhoto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select == 1) {
                    takePhoto();
                } else if (select == 2) {
                    takePhoto2();
                }
            }
        });
        CameraPermissionDialog.album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select == 1) {
                    selectAlbum();
                } else if (select == 2) {
                    selectAlbum2();
                }
            }
        });
        CameraPermissionDialog.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oDialog.dismiss();
            }
        });

    }

    private void makeDialog2() {
        final CameraPermissionDialog oDialog = new CameraPermissionDialog(getContext());
        oDialog.show();
        select = 2;
        CameraPermissionDialog.takePhoto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select == 1) {
                    takePhoto();
                } else if (select == 2) {
                    takePhoto2();
                }
            }
        });
        CameraPermissionDialog.album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select == 1) {
                    selectAlbum();
                } else if (select == 2) {
                    selectAlbum2();
                }
            }
        });
        CameraPermissionDialog.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oDialog.dismiss();
            }
        });

    }
    //다이얼로그 띄우기 끝

    //프로필 동그랗게 하기
    public void setImage() {
        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        user_profile.setClipToOutline(true);
    }

    //갤러리에서 배경사진 선택
    public void selectAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM_B);
    }

    public void selectAlbum2() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM_U);
    }

    //사진촬영한것 갤러리에 저장
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        Toast.makeText(getContext(), "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();

    }

    //사진촬영
    public void takePhoto() {
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                    imgUri = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, PICK_FROM_CAMERA_B);
                }
            }
        } else {
            Log.v("알림", "저장공간에 접근 불가능");

            return;
        }
    }

    public void takePhoto2() {
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                    imgUri = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, PICK_FROM_CAMERA_U);
                }
            }
        } else {
            Log.v("알림", "저장공간에 접근 불가능");

            return;
        }
    }

    //사진 폴더 생성(내장메모리에 pictures/billinavi 생김)
    private File createImageFile() throws IOException {
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "billinavi");
        if (!storageDir.exists()) {
            Log.v("알림", "storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림", "storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_ALBUM_B: {
                //앨범에서 가져오기
                if (data.getData() != null) {
                    try {
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        albumURI = Uri.fromFile(albumFile);
                        galleryAddPic();
                        background.setImageURI(photoURI);
                        //cropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("알림", "앨범에서 가져오기 에러");
                    }
                }
                break;
            }
            case PICK_FROM_CAMERA_B: {
                //카메라 촬영
                try {
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    background.setImageURI(imgUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case PICK_FROM_ALBUM_U: {
                //앨범에서 가져오기
                if (data.getData() != null) {
                    try {
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        albumURI = Uri.fromFile(albumFile);
                        galleryAddPic();
                        user_profile.setImageURI(photoURI);
                        //cropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("알림", "앨범에서 가져오기 에러");
                    }
                }
                break;
            }
            case PICK_FROM_CAMERA_U: {
                //카메라 촬영
                try {
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    user_profile.setImageURI(imgUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //네트워크 연결
    class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        private int count;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //output.setText(s);
        }
    }

    //연결요청
    public class RequestHttpURLConnection {

        public String request(String _url, ContentValues _params) {
            HttpURLConnection urlConn = null;
            StringBuffer sbParams = new StringBuffer();

            if (_params == null)
                sbParams.append("");
            else {
                boolean isAnd = false;
                String key;
                String value;

                for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                    key = parameter.getKey();
                    value = parameter.getValue().toString();

                    if (isAnd)
                        sbParams.append("&");
                    sbParams.append(key).append("=").append(value);

                    if (!isAnd)
                        if (_params.size() >= 2)
                            isAnd = true;
                }
            }
            try {
                String myId, myName;
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setDefaultUseCaches(false);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8");
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                StringBuffer buffer = new StringBuffer();
           /* buffer.append("id").append("=").append(myId).append("&");
            buffer.append("name").append("=").append(myName);*/

                OutputStreamWriter os = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(os);
                writer.write(buffer.toString());
                writer.flush();

                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                String line;
                String page = "";

                while ((line = reader.readLine()) != null) {
                    page += line + "\n";
                }
                return page;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }
}
