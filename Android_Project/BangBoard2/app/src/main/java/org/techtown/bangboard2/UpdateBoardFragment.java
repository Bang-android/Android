package org.techtown.bangboard2;

import android.content.Context;
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

public class UpdateBoardFragment extends Fragment {

    MainActivity mainActivity;

    EditText editText;
    EditText editText2;

    String oldTitle;
    String oldContent;
    String oldDate;
    String newTitle;
    String newContent;
    String user;
    String newDate;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");

    public static Fragment newInstance (String oldTitle, String oldContent, String oldDate, String user) {
        UpdateBoardFragment fragment = new UpdateBoardFragment();
        Bundle args = new Bundle();
        args.putString("oldTitle", oldTitle);
        args.putString("oldContent", oldContent);
        args.putString("oldDate", oldDate);
        args.putString("user", user);
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
        View view = inflater.inflate(R.layout.updateboard, container, false);

        editText = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);

        if (getArguments() != null) {  // ViewBoardFragment에서 수정 버튼을 눌렀을 때 수정하기 위해 원래 있던 정보들을 가져옴
            oldTitle = getArguments().getString("oldTitle");
            oldContent = getArguments().getString("oldContent");
            oldDate = getArguments().getString("oldDate");
            user = getArguments().getString("user");
        }

        editText.setText(oldTitle);
        editText2.setText(oldContent);

        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {  // UpdateBoardFragment의 수정 버튼을 눌렀을 때 이벤트
            @Override
            public void onClick(View view) {
                newTitle = editText.getText().toString();
                newContent = editText2.getText().toString();
                long now = System.currentTimeMillis();  // 시간을 최산화한다
                Date date = new Date(now);
                newDate = sdf.format(date);

                UpdateDB(oldTitle, oldContent, oldDate, newTitle, newContent, newDate, user);
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

        return view;
    }

    public void UpdateDB (String oldTitle, String oldContent,String oldDate, String newTitle, String newContent, String newDate, String user) {
        /*try {
            URL url = new URL(MainActivity.DB_URL + "update.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = URLEncoder.encode("oldTitle", "UTF-8") + "=" + URLEncoder.encode(oldTitle, "UTF-8");
            data += "&" + URLEncoder.encode("oldContent", "UTF-8") + "=" + URLEncoder.encode(oldContent, "UTF-8");
            data += "&" + URLEncoder.encode("oldDate", "UTF-8") + "=" + URLEncoder.encode(oldDate, "UTF-8");
            data += "&" + URLEncoder.encode("newTitle", "UTF-8") + "=" + URLEncoder.encode(newTitle, "UTF-8");
            data += "&" + URLEncoder.encode("newContent", "UTF-8") + "=" + URLEncoder.encode(newContent, "UTF-8");
            data += "&" + URLEncoder.encode("newDate", "UTF-8") + "=" + URLEncoder.encode(newDate, "UTF-8");

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

                Log.i("Bang", "echo : " + sb.toString());

                if (sb.toString().equals("Update 성공")) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "수정 성공", Toast.LENGTH_LONG).show();
                        }
                    });
                    Board item = new Board(newTitle, newContent, newDate, user);
                    mainActivity.changeFragment(ViewBoardFragment.newInstance(item, "Create"));
                } else {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "수정 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Call<String> call = RetrofitClient.getInstance().getApiService().updateBoard(oldTitle, oldContent, oldDate, newTitle, newContent, newDate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    if (response.body().equals("Update Success")) {
                        Toast.makeText(mainActivity, "수정 성공", Toast.LENGTH_LONG).show();
                        mainActivity.changeFragment(ViewBoardFragment.newInstance(new Board(newTitle, newContent, newDate, user), "Create"));
                    } else {
                        Toast.makeText(mainActivity, "수정 실패", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", t.getMessage());
            }
        });
    }
}
