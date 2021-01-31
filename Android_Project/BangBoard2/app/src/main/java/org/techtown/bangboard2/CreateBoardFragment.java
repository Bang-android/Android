package org.techtown.bangboard2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBoardFragment extends Fragment {

    MainActivity mainActivity;

    EditText editText;
    EditText editText2;

    ProgressDialog progressDialog;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");

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

        View view = inflater.inflate(R.layout.createboard, container, false);

        editText = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);

        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString();
                String content = editText2.getText().toString();
                long now = System.currentTimeMillis();
                Date nowDate = new Date(now);
                String date = sdf.format(nowDate);

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    Toast.makeText(mainActivity, "제목 또는 내용을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    insertDB(title, content, date, LoginFragment.id);
                }
            }
        });

        Button btnList = view.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(editText.getText().toString()) || !TextUtils.isEmpty(editText2.getText().toString())) {
                    CustomDialog customDialog = new CustomDialog(mainActivity, new CustomDialogClickListener() {
                        @Override
                        public void onPositiveClicked() {
                            mainActivity.changeFragment(new ListBoardFragment());
                        }
                    }, "나가시겠습니까?", "예");

                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                } else {
                    mainActivity.changeFragment(new ListBoardFragment());
                }
            }
        });

        // CreateBoardFragment 에서 뒤로가기 버튼을 눌렀을 때 이벤트 정의 -> 제목이나 내용에 글이 쓰여져있는 상황에서 뒤로가기 버튼을 눌렀을 때, 나가겠냐고 물어보는 다이얼로그를 띄워줌
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!TextUtils.isEmpty(editText.getText().toString()) || !TextUtils.isEmpty(editText2.getText().toString())) {  // title 이나 content 에 무언가 쓰여있다면
                    CustomDialog customDialog = new CustomDialog(mainActivity, new CustomDialogClickListener() {
                        @Override
                        public void onPositiveClicked() {
                            mainActivity.getSupportFragmentManager().popBackStack();
                        }
                    }, "나가시겠습니까?", "예");

                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                } else {
                    mainActivity.getSupportFragmentManager().popBackStack();
                }
            }
        };
        mainActivity.getOnBackPressedDispatcher().addCallback(this, callback);
        // CreateBoardFragment 에서 뒤로가기 버튼을 눌렀을 때 이벤트 정의 -> 제목이나 내용에 글이 쓰여져있는 상황에서 뒤로가기 버튼을 눌렀을 때, 나가겠냐고 물어보는 다이얼로그를 띄워줌
        return view;
    }

    public void insertDB (String title, String content, String date, String id) {
        // 진행상태 표시 -> UI 관련 작업은 UI Thread 에서만 가능
        /*try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
            data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                progressDialog.dismiss();

                if (sb.toString().equals("Insert 성공")) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "등록 완료", Toast.LENGTH_LONG).show();
                            mainActivity.changeFragment(ViewBoardFragment.newInstance(new Board(title, content, date, id), "Create"));
                        }
                    });
                } else {
                    Toast.makeText(mainActivity, "등록 실패", Toast.LENGTH_LONG).show();
                }

                // insert를 통해 레코드를 추가했기 때문에 다시 select를 해서 데이터를 갱신해야함 -> 안그러면 방금 작성한 글은 리싸이클러뷰에 보이지 않음
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        progressDialog = new ProgressDialog(mainActivity);  // 진행상황 표시
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        Call<String> call = RetrofitClient.getInstance().getApiService().insertBoard(title, content, date, id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());
                    progressDialog.dismiss();

                    if (response.body().equals("Insert Success")) {
                        Toast.makeText(mainActivity, "등록 완료", Toast.LENGTH_LONG).show();
                        mainActivity.changeFragment(ViewBoardFragment.newInstance(new Board(title, content, date, id), "Create"));
                    } else {
                        Toast.makeText(mainActivity, "등록 실패", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", "에러 : " + t.getMessage());
            }
        });

    }
}
