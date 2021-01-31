package org.techtown.bangboard2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    MainActivity mainActivity;

    EditText editText;
    EditText editText2;
    EditText editText3;

    TextView textidCheck;
    TextView textpasswordCheck;
    TextView textpasswordCheck2;

    String id;
    String password;

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
        View view = inflater.inflate(R.layout.register, container, false);

        editText = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);

        textidCheck = view.findViewById(R.id.textidCheck);
        textpasswordCheck = view.findViewById(R.id.textpasswordCheck);
        textpasswordCheck2 = view.findViewById(R.id.textpasswordCheck2);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {  // 문자 입력이 끝났을 때 -> 문자 입력이 끝나고 검사를 해야 함
                id = editable.toString();

                // 정규 표현식을 사용하여 입력한 문자열을 검사
                if (id.length() >= 4 && id.length() <= 20 && Pattern.matches("^[a-z0-9]*$", id)) {  // 글자수가 4~20자 이고, 영어 소문자와 숫자만 가능
                    idCheck(id);
                } else if (id.length() == 0) {
                    textidCheck.setVisibility(View.VISIBLE);
                    textidCheck.setTextColor(Color.parseColor("#F15246"));
                    textidCheck.setText("필수 정보입니다.");
                } else {
                    textidCheck.setVisibility(View.VISIBLE);
                    textidCheck.setTextColor(Color.parseColor("#F15246"));
                    textidCheck.setText("4~20자의 영문 소문자와 숫자만 사용 가능합니다.");
                }
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();

                // 정규 표현식을 사용하여 입력한 문자열을 검사
                if (password.length() >= 4 && password.length() <= 20 && Pattern.matches("^[a-zA-Z0-9]*$", password)) {  // 글자수가 4~20자 이고, 영어 소문자와 대문자, 숫자만 가능
                    textpasswordCheck.setVisibility(View.VISIBLE);
                    textpasswordCheck.setTextColor(Color.parseColor("#21BC27"));
                    textpasswordCheck.setText("적합한 비밀번호입니다.");
                } else if (password.length() == 0) {
                    textpasswordCheck.setVisibility(View.VISIBLE);
                    textpasswordCheck.setTextColor(Color.parseColor("#F15246"));
                    textpasswordCheck.setText("필수 정보입니다.");
                } else {
                    textpasswordCheck.setVisibility(View.VISIBLE);
                    textpasswordCheck.setTextColor(Color.parseColor("#F15246"));
                    textpasswordCheck.setText("4~20자의 영문 소문자와 대문자, 숫자만 사용 가능합니다.");
                }
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password2 = editable.toString();

                // 정규 표현식을 사용하여 입력한 문자열을 검사
                if (password2.equals(password)) {
                    textpasswordCheck2.setVisibility(View.VISIBLE);
                    textpasswordCheck2.setTextColor(Color.parseColor("#21BC27"));
                    textpasswordCheck2.setText("비밀번호가 일치합니다.");
                } else if (password2.length() == 0) {
                    textpasswordCheck2.setVisibility(View.VISIBLE);
                    textpasswordCheck2.setTextColor(Color.parseColor("#F15246"));
                    textpasswordCheck2.setText("필수 정보입니다.");
                } else {
                    textpasswordCheck2.setVisibility(View.VISIBLE);
                    textpasswordCheck2.setTextColor(Color.parseColor("#F15246"));
                    textpasswordCheck2.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textidCheck.getTextColors().getDefaultColor() == -14566361
                && textpasswordCheck.getTextColors().getDefaultColor() == -14566361
                && textpasswordCheck2.getTextColors().getDefaultColor() == -14566361) {  // 입력한 정보들의 검사가 모두 통과인 경우 -> 색깔이 모두 초록색인 경우
                    registerDB(id, password);
                } else {
                    Toast.makeText(mainActivity, "입력한 정보들을 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public void idCheck (String id) {
        /*try {
            URL url = new URL(MainActivity.DB_URL + "register_id_check.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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

                if (sb.toString().equals("사용 가능")) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textidCheck.setVisibility(View.VISIBLE);
                            textidCheck.setTextColor(Color.parseColor("#21BC27"));
                            textidCheck.setText("사용 가능한 아이디입니다.");
                            Log.i("Bang", "Color : " + textidCheck.getTextColors().toString());
                        }
                    });
                } else {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textidCheck.setVisibility(View.VISIBLE);
                            textidCheck.setTextColor(Color.parseColor("#F15246"));
                            textidCheck.setText("이미 사용중인 아이디입니다.");
                            Log.i("Bang", "Color : " + textidCheck.getTextColors().getDefaultColor());
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Call<String> call = RetrofitClient.getInstance().getApiService().idCheck(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    if (response.body().equals("사용 가능")) {
                        textidCheck.setVisibility(View.VISIBLE);
                        textidCheck.setTextColor(Color.parseColor("#21BC27"));
                        textidCheck.setText("사용 가능한 아이디입니다.");
                    } else {
                        textidCheck.setVisibility(View.VISIBLE);
                        textidCheck.setTextColor(Color.parseColor("#F15246"));
                        textidCheck.setText("이미 사용중인 아이디입니다.");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", "에러 : " + t.getMessage());
            }
        });
    }

    public void registerDB (String id, String password) {
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

                if (sb.toString().equals("success")) {  // 회원가입 성공 시, 토스트 메시지를 띄우고 로그인 화면으로 이동
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "회원가입 성공", Toast.LENGTH_LONG).show();
                        }
                    });
                    mainActivity.getSupportFragmentManager().beginTransaction().remove(RegisterFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                    mainActivity.getSupportFragmentManager().popBackStack();
                } else {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "회원가입 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Call<String> call = RetrofitClient.getInstance().getApiService().getUserRegist(id, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    if (response.body().equals("Regist Success")) {
                        Toast.makeText(mainActivity, "회원가입 성공", Toast.LENGTH_LONG).show();
                        mainActivity.getSupportFragmentManager().beginTransaction().remove(RegisterFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                        mainActivity.getSupportFragmentManager().popBackStack();  // 현재 회원가입 화면을 지우고, popBackStack()을 하여 뒤로가기 함
                                                                                  // 이렇게 하면 로그인 화면에서 뒤로가기를 해도 다시 회원가입 화면이 나타나지 않음
                    } else {
                        Toast.makeText(mainActivity, "회원가입 실패", Toast.LENGTH_LONG).show();
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
