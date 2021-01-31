package org.techtown.bangboard2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String DB_URL = "http://10.0.2.2/";

    static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();  // 첫 화면을 LoginFragment로 설정

        // 커스텀 액션바 적용
        getSupportActionBar().hide();  // 액션바 없는게 제일 괜찮은 것 같아서 숨겨놓은 상태임
        /*getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        View custom_actionbar = LayoutInflater.from(this).inflate(R.layout.custom_actionbar, null);
        textView = custom_actionbar.findViewById(R.id.textView);
        textView.setText("테스트");
        getSupportActionBar().setCustomView(custom_actionbar);
        Toolbar toolbar = (Toolbar)custom_actionbar.getParent();  // 커스텀 액션바를 적용했을 때...
        toolbar.setContentInsetsAbsolute(0, 0);  // 양쪽에 생기는 공백을 없애줌
        getSupportActionBar().setCustomView(custom_actionbar, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        // 커스텀 액션바 적용*/
    }

    public void changeFragment(Fragment newFragment) {
        String fragmentTag = newFragment.getClass().getSimpleName();  // 각 클래스명을 알아냄
        getSupportFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);  // 기존에 있던 프래그먼트를 없애줌 -> 프래그먼트들이 중복되는 문제 해결

        getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(fragmentTag).commit();
    }

}