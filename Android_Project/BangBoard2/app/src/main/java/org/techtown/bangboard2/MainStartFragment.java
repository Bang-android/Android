package org.techtown.bangboard2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainStartFragment extends Fragment {

    boolean isOpen = false;

    MainActivity mainActivity;

    TextView textView;

    String mCurrentPhotoPath;

    static Bitmap bitmap;

    //Fragment는 기본 생성자만 사용해야한다! -> 이런식으로 값을 전달하고 해당 Fragment에서 복원해야한다.
    public static Fragment newInstance (String id) {
        MainStartFragment fragment = new MainStartFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainstart, container, false);

        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        ArrayList<String> targetList = new ArrayList<String>();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mainActivity, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Bang", permissions[i] + " 권한 있음");
            } else {
                Log.i("Bang", permissions[i] + " 권한 없음");
                if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, permissions[i])) {
                    Log.i("Bang", permissions[i] + " 권한 설명 필요");
                } else {
                    targetList.add(permissions[i]);
                }
            }
        }

        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        if (targets.length > 0) {
            ActivityCompat.requestPermissions(mainActivity, targets, 101);
        }

        textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageViewAdd);

        TextView textCamera = view.findViewById(R.id.textCamera);
        TextView textAlbum = view.findViewById(R.id.textAlbum);

        if (getArguments() != null) {
            textView.setText(getArguments().getString("id") + " 님");  // LoginFragment에서 받은 id 값을 복원하여 textView에 설정
        }

        LinearLayout layoutStart = view.findViewById(R.id.layoutStart);
        LinearLayout layoutSub = view.findViewById(R.id.layoutSub);
        layoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    layoutSub.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.baseline_add_black_48);

                    isOpen = false;
                } else {
                    layoutSub.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.baseline_remove_black_48);

                    isOpen = true;
                }
            }
        });

        textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // 기본 카메라 어플 실행
                if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {  // resolveActivity -> 해당 Intent를 받을 수 있는 컴포넌트의 존재 유무를 확인함. null일 경우 해당 Intent를 사용할 수 없다.
                    File photoFile = null;

                    try {
                        photoFile = createImageFile(); // createImageFile()에서 throws IOException 을 하기 때문에 여기서 예외처리를 해줘야 함
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(mainActivity, "org.techtown.bangboard2.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 101);
                    }
                }
            }
        });

        textAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 102);
            }
        });

        LinearLayout layoutBoard = view.findViewById(R.id.layoutBoard);
        layoutBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(new ListBoardFragment());
            }
        });

        LinearLayout layoutLogout = view.findViewById(R.id.layoutLogout);
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new CustomDialog(mainActivity, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        mainActivity.getSupportFragmentManager().popBackStack();
                    }
                }, "로그아웃 하시겠습니까?", "예");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {  // 해당 Fragment에서 뒤로가기 버튼을 누를 시 동작
            @Override
            public void handleOnBackPressed() {
                CustomDialog dialog = new CustomDialog(mainActivity, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        mainActivity.getSupportFragmentManager().popBackStack();
                    }
                }, "로그아웃 하시겠습니까?", "예");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        };
        mainActivity.getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());  // new Date() 하면 현재 시간을 리턴한다
        String imageFileName = "IMG_" + timeStamp +"_" + getArguments().getString("id") + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("Bang", "createImageFile() / mCurrentPhotoPath : " + mCurrentPhotoPath);

        return image;
    }

    // 주석 풀면 299 x 299 사이즈로 리사이즈 후 서버로 업로드함 -> 299 x 299 사이즈가 서버 모델에서 인식률이 제일 좋음
    public File createScaleImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_SCALE_" + timeStamp +"_" + getArguments().getString("id") + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "scale");
        //Log.i("Bang", "storageDir : " + storageDir.toString());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image_scale = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image_scale.getAbsolutePath();
        Log.i("Bang", "createScaleImageFile() / mCurrentPhotoPath : " + mCurrentPhotoPath);

        return image_scale;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Bang", permissions[i] + " 권한 승인됨");
                } else {
                    Log.i("Bang", permissions[i] + " 권한 거부됨");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {  // 카메라 열기
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                if (Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source =ImageDecoder.createSource(mainActivity.getContentResolver(), Uri.fromFile(file));
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source, new ImageDecoder.OnHeaderDecodedListener() {
                            @Override
                            public void onHeaderDecoded(@NonNull ImageDecoder imageDecoder, @NonNull ImageDecoder.ImageInfo imageInfo, @NonNull ImageDecoder.Source source) {
                                imageDecoder.setTargetSampleSize(2); // 이미지의 사이즈를 1/2 만큼 줄여서 decoding 한다.
                            }
                        });
                        //Log.i("Bang", "width : " + bitmap.getWidth() + ", " + "height : " + bitmap.getHeight());
                        // 주석 풀면 299 x 299 사이즈로 리사이즈 후 서버로 업로드함 -> 299 x 299 사이즈가 서버 모델에서 인식률이 제일 좋음
                        //Log.i("Bang", "bitmapWidth : " + bitmap.getWidth() + ", " + "bitmapHeigth : " + bitmap.getHeight());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 299, 299, true);
                        //Log.i("Bang", "bitmapWidth : " + bitmap.getWidth() + ", " + "bitmapHeigth : " + bitmap.getHeight());
                        File photoFile_scale = createScaleImageFile();

                        FileOutputStream fos = new FileOutputStream(photoFile_scale);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();

                        mainActivity.changeFragment(new CameraFragment());
                        MediaScanner scanner = MediaScanner.newInstance(mainActivity);  // 새로 찍은 사진이 갤러리에 바로 뜨도록 mediaScanning을 해줌
                        scanner.mediaScanning(mCurrentPhotoPath);
                        new Thread(new Runnable() {  // 찍은 사진을 서버로 업로드 -> MainThread가 아닌 별도의 Thread에서 실행되야함
                            @Override
                            public void run() {
                                uploadFile(mCurrentPhotoPath);
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (requestCode == 102) {  // 앨범 열기
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String path = null;

                Cursor cursor = mainActivity.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

                while (cursor.moveToNext()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }

                Log.i("Bang", "path : " + path);
                mCurrentPhotoPath = path;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(path, options);  // 이미지의 사이즈를 1/2 만큼 줄여서 decoding 한다.
                //Log.i("Bang", "width : " + bitmap.getWidth() + ", " + "height : " + bitmap.getHeight());

                mainActivity.changeFragment(new CameraFragment());  // CameraFragment로 넘어가면서 동시에 이미지 파일을 서버에 업로드한다.
                uploadFile(mCurrentPhotoPath);
            }
        }
    }
    // 서버로 업로드하는 코드 -> multipart 사용
    public void uploadFile (String filePath) {
        /*String twoHyphens = "--";
        String boundary = "*****";
        String lineEnd = "\r\n";
        int maxBufferSize = 5 * 1024 * 1024;

        File file = new File(filePath);
        if (!file.isFile()) {
            Log.i("Bang", "파일 존재 X");
            return;
        }

        try {
            URL url = new URL("http://10.0.2.2/" + "UploadToServer.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                    + filePath + "\"" + lineEnd);
            dos.writeBytes("Content-Type: " + "image/jpg" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fileInputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            dos.close();
            fileInputStream.close();

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                //Log.i("Bang/uploadfile", "echo : " + sb.toString());
                if (sb.toString().equals("OK")) {
                    Log.i("Bang", "이미지 업로드 성공");
                } else {
                    Log.i("Bang", "이미지 업로드 실패");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", filePath, requestBody);

        Call<String> call = RetrofitClient.getInstance().getApiService().uploadImage(part);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", "에러 : " + t.getMessage());
            }
        });
    }
}
