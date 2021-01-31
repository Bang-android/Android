package org.techtown.bangboard2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    MainActivity mainActivity;

    EditText editText;
    EditText editText2;

    static String id;  // 다른 프래그먼트에서 참조해야하므로 static 으로 선언

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
        View view = inflater.inflate(R.layout.login, container, false);

        editText = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);

        Button button = view.findViewById(R.id.button);  // 로그인 버튼 클릭 시 이벤트
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = editText.getText().toString();
                String password = editText2.getText().toString();

                loginDB(id, password);
            }
        });

        TextView textView = view.findViewById(R.id.textView);  // 회원가입 텍스트뷰  클릭 시 이벤트
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(new RegisterFragment());
            }
        });

        return view;
    }

    public void loginDB (String id, String password) {

        /*try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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

                if (sb.toString().equals("success")) {  // 결과가 "success" 일 때만 ListBoardFragment로 이동
                    mainActivity.changeFragment(MainStartFragment.newInstance(id));
                } else {
                    mainActivity.runOnUiThread(new Runnable() {  // 토스트 메시지를 띄우는건 UI Thread 에서 해야함
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Call<String> call = RetrofitClient.getInstance().getApiService().getUserLogin(id, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    if (response.body().equals("Login Success")) {
                        Toast.makeText(mainActivity, "로그인 성공", Toast.LENGTH_LONG).show();
                        mainActivity.changeFragment(MainStartFragment.newInstance(id));  // MainStartFragment로 이동하면서 현재 로그인한 id 값을 넘겨줌
                    } else {
                        Toast.makeText(mainActivity, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
