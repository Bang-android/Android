package org.techtown.bangboard2;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraFragment extends Fragment {

    MainActivity mainActivity;

    ImageView imageView;

    String resultString;

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
        View view = inflater.inflate(R.layout.camera, container, false);

        imageView = view.findViewById(R.id.imageView);

        imageView.setImageBitmap(MainStartFragment.bitmap);  // MainStartFragment에서 생성된 bitmap 파일을 참조하여 imageView를 설정한다.

        LottieAnimationView lottieView = view.findViewById(R.id.lottieView);
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                lottieView.setFrame(0);// 프레임을 처음으로 설정해주지 않으면 ResultFragment 에서 뒤로가기를 했을 때 바로 이 메소드가 실행되어 ResultFragment로 넘어간다.
                receiveResult();
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });


        return view;
    }

    public void receiveResult() {  // -> test,test2,test3 이런식으로 결과 3개를 한번에 받아온다. 첫 번째가 인식 결과이고 나머지 두 개는 추천운동이다.
        /*try {
            //URL url = new URL(MainActivity.DB_URL + "ReadText_and_getEquipment.php");
            URL url = new URL("http://10.0.2.2/" + "test_equipment.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                Log.i("Bang/receiveResult", "echo : " + sb.toString());
                jsonResult(sb.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Retrofit2 를 이용한 코드로 수정
        Gson gson = new Gson();
        ArrayList<Equipment> resultorrecommend = new ArrayList  <Equipment>();

        Call<String> call = RetrofitClient.getInstance().getApiService().selectEquipment();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("Bang", response.body());

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Equipment resultItem = gson.fromJson(jsonArray.get(i).toString(), Equipment.class);
                            resultorrecommend.add(resultItem);
                        }
                        mainActivity.changeFragment(ResultMainFragment.newInstance(resultorrecommend));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", "에러 : " + t.getMessage());
            }
        });
    }


    /*public void jsonResult(String jsonData) {
        ArrayList<Equipment> resultorrecommend = new ArrayList<Equipment>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString("name");
                String point = item.getString("point");
                String position = item.getString("position");
                String method = item.getString("method");
                String image = item.getString("image");
                String youtube_id = item.getString("youtube_id");

                if (name.equals(resultString)) {  // json에서 읽어온 데이터가 첫 번째로 받은 인식 결과이면,
                    resultorrecommend.add(0, new Equipment(name, point, position, method, image, youtube_id));  // ArrayList의 첫 번째 인덱스에 넣는다.
                } else {
                    resultorrecommend.add(new Equipment(name, point, position, method, image, youtube_id));
                }
            }
            mainActivity.changeFragment(ResultMainFragment.newInstance(resultorrecommend));  // ResultMainFragment로 이동하면서 결과 데이터를 넘겨준다.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
