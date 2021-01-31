package org.techtown.bangboard2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultMainFragment extends Fragment {

    MainActivity mainActivity;

    ViewPager2 pager2;

    static RelativeLayout layoutResult;

    ArrayList<Equipment> resultorrecommend;

    public static Fragment newInstance (ArrayList<Equipment> resultorrecommend) {
        ResultMainFragment fragment = new ResultMainFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("resultorrecommend", resultorrecommend);
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
        View view = inflater.inflate(R.layout.result_main, container, false);

        layoutResult = view.findViewById(R.id.layoutResult);
        pager2 = view.findViewById(R.id.pager2);
        pager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager2.setOffscreenPageLimit(3);

        Button  button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(MainStartFragment.newInstance(LoginFragment.id));
            }
        });
        if (getArguments() != null) {  // 이 ResultMainFragment로 넘어온 데이터를 받는 코드 -> 결과 데이터를 담고있다.
            resultorrecommend = getArguments().getParcelableArrayList("resultorrecommend");
        }

        PagerAdapter adapter = new PagerAdapter(mainActivity.getSupportFragmentManager(), mainActivity.getLifecycle());
        pager2.setAdapter(adapter);

        return view;
    }

    class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0 :
                    return Result1Fragment.newInstance(resultorrecommend.get(0));  // 인식 결과를 Result1Fragment로 보내줌

                case 1 :
                    return Result2Fragment.newInstance(resultorrecommend.get(1), resultorrecommend.get(2));  // 추천 운동기구 2개를 Result2Fragment로 보내줌
                case 2 :
                    return Result3Fragment.newInstance(resultorrecommend);
                default:
                    return Result1Fragment.newInstance(resultorrecommend.get(0));

            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    /*public Bitmap downloadImage (String path) {

        try {
            URL url = new URL("http://10.0.2.2/" + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

                bitmap = BitmapFactory.decodeStream(bis);

                bis.close();
            }

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }*/
}











