package org.techtown.bangboard2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ViewBoardFragment extends Fragment {

    MainActivity mainActivity;

    TextView textTitle;
    TextView textUser;
    TextView textDate;
    TextView textContent;

    Button btnUpdate;
    Button btnDelete;

    int position;

    String title;
    String user;
    String date;
    String content;

    public static Fragment newInstance (int position, String flag) {
        ViewBoardFragment fragment = new ViewBoardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("flag", flag);
        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment newInstance (Board item, String flag) {
        ViewBoardFragment fragment = new ViewBoardFragment();
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("content", item.getContent());
        args.putString("date", item.getDate());
        args.putString("user", item.getUid());
        args.putString("flag", flag);
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
        View view = inflater.inflate(R.layout.viewboard, container, false);

        textTitle = view.findViewById(R.id.textTitle);
        textUser = view.findViewById(R.id.textUser);
        textDate = view.findViewById(R.id.textDate);
        textContent = view.findViewById(R.id.textContent);

        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);

        if (getArguments().getString("flag").equals("List")) {  // ListBoardFragment에서 RecyclerView의 아이템을 눌렀을 때
            position = getArguments().getInt("position");
            Board item = ListBoardFragment.adapter.getItem(position);  // 받은 position을 이용해 Adapter에서 해당 Board 아이템을 가져옴

            title = item.getTitle();
            user = item.getUid();
            date = item.getDate();
            content = item.getContent();
        } else if (getArguments().getString("flag").equals("Create")) {  // CreateBoardFragment에서 등록 버튼을 눌렀을 때 -> 작성한 글을 ViewBoardFragment에서 바로 보여줌
            title = getArguments().getString("title");
            user = getArguments().getString("user");
            date = getArguments().getString("date");
            content = getArguments().getString("content");
        }

        textTitle.setText(title);
        textUser.setText(user);
        textDate.setText(date);
        textContent.setText(content);

        if (LoginFragment.id.equals(user)) {  // 로그인 한 id와 가져온 id가 같으면, 즉 이 글을 작성한 사람이면
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);  // 수정, 삭제 버튼이 보이도록 함 -> 이 글을 작성한 사람만 글을 수정하고 삭제할 수 있도록 하는 것
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {  // 수정 버튼 클릭 시 이벤트
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(UpdateBoardFragment.newInstance(title, content, date, user));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {  // 삭제 버튼 클릭 시 이벤트
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new CustomDialog(mainActivity, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        DeleteDB(title, content, date, user);
                    }
                }, "삭제하시겠습니까?", "예");
                dialog.show();
            }
        });

        Button btnList = view.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {  // 목록 버튼 클릭 시 이벤트
            @Override
            public void onClick(View view) {
                /*mainActivity.getSupportFragmentManager().beginTransaction().remove(ViewBoardFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                mainActivity.getSupportFragmentManager().popBackStack();*/
                mainActivity.changeFragment(new ListBoardFragment());
            }
        });

        Button btnWrite = view.findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {  // 글쓰기 버튼 클릭 시 이벤트
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(new CreateBoardFragment());
            }
        });
        return view;
    }

    public void DeleteDB (String title, String content, String date, String user) {
        /*try {
            URL url = new URL(MainActivity.DB_URL + "delete.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
            data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));

            writer.write(data);
            writer.flush();
            writer.close();

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = reader.readLine()) !=null) {
                    sb.append(line);
                }

                reader.close();

                if (sb.toString().equals("Delete 성공")) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "삭제 성공", Toast.LENGTH_LONG).show();
                        }
                    });
                    mainActivity.changeFragment(new ListBoardFragment());
                } else {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainActivity, "삭제 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Call<String> call = RetrofitClient.getInstance().getApiService().deleteBoard(title, content, date, user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    if (response.body().equals("Delete Success")) {
                        Toast.makeText(mainActivity, "삭제 성공", Toast.LENGTH_LONG).show();
                        mainActivity.changeFragment(new ListBoardFragment());
                    } else {
                        Toast.makeText(mainActivity, "삭제 실패", Toast.LENGTH_LONG).show();
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
