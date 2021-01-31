package org.techtown.bangboard2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class Result2Fragment extends Fragment {
    boolean isOpened1 = false;
    boolean isOpened2 = false;

    MainActivity mainActivity;

    ImageView result2re1ImageView;
    TextView result2recommend1;
    TextView result2re1Point;
    TextView result2re1Position;
    TextView result2re1Method;

    ImageView result2re2ImageView;
    TextView result2recommend2;
    TextView result2re2Point;
    TextView result2re2Position;
    TextView result2re2Method;

    LinearLayout result2AddLayout1;
    LinearLayout result2AddLayout2;

    Equipment re1;
    Equipment re2;

    TextView textTouch;
    public static Fragment newInstance (Equipment re1, Equipment re2) {
        Result2Fragment fragment = new Result2Fragment();
        Bundle args = new Bundle();
        args.putParcelable("re1", re1);
        args.putParcelable("re2", re2);
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
        View view = inflater.inflate(R.layout.result_2, container, false);

        result2re1ImageView = view.findViewById(R.id.result2re1ImageView);
        result2recommend1 = view.findViewById(R.id.result2recommend1);  // 추천 운동기구 이름 1
        result2re1Point = view.findViewById(R.id.result2re1Point);
        result2re1Position = view.findViewById(R.id.result2re1Position);
        result2re1Method = view.findViewById(R.id.result2re1Method);

        result2re2ImageView = view.findViewById(R.id.result2re2ImageView);
        result2recommend2 = view.findViewById(R.id.result2recommend2);  // 추천 운동기구 이름 2
        result2re2Point = view.findViewById(R.id.result2re2Point);
        result2re2Position = view.findViewById(R.id.result2re2Position);
        result2re2Method = view.findViewById(R.id.result2re2Method);

        result2AddLayout1 = view.findViewById(R.id.result2AddLayout1);
        result2AddLayout2 = view.findViewById(R.id.result2AddLayout2);
        textTouch = view.findViewById(R.id.textTouch);

        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(1000);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(Animation.REVERSE);
        textTouch.startAnimation(anim);

        if (getArguments() != null) {
            re1 = getArguments().getParcelable("re1");
            re2 = getArguments().getParcelable("re2");
        }

        Glide.with(this).load(MainActivity.DB_URL + re1.getImage()).into(result2re1ImageView);
        Glide.with(this).load(MainActivity.DB_URL + re2.getImage()).into(result2re2ImageView);

        result2recommend1.setText(re1.getName());
        result2re1Point.setText(re1.getPoint());
        result2re1Position.setText(re1.getPosition());
        result2re1Method.setText(re1.getMethod());
        result2recommend1.setOnClickListener(new View.OnClickListener() {  // 첫 번째 추천운동기구 이름을 터치하면,
            @Override
            public void onClick(View view) {
                if (isOpened2) {
                    showLayout2();
                    showLayout1();
                } else {
                    showLayout1();
                }
            }
        });

        result2recommend2.setText(re2.getName());
        result2re2Point.setText(re2.getPoint());
        result2re2Position.setText(re2.getPosition());
        result2re2Method.setText(re2.getMethod());
        result2recommend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpened1) {
                    showLayout1();
                    showLayout2();
                } else {
                    showLayout2();
                }
            }
        });
        return view;
    }
    public void showLayout1() {
        int dpValue = 700;
        float d = mainActivity.getResources().getDisplayMetrics().density;

        int height = (int) (dpValue * d);

        ValueAnimator va = isOpened1 ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                result2AddLayout1.getLayoutParams().height = value;
                result2AddLayout1.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!isOpened1) {
                    result2AddLayout1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isOpened1) {
                    result2AddLayout1.setVisibility(View.GONE);
                }
                isOpened1 = !isOpened1;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        va.start();
    }

    public void showLayout2() {
        int dpValue = 700;
        float d = mainActivity.getResources().getDisplayMetrics().density;

        int height = (int) (dpValue * d);

        ValueAnimator va = isOpened2 ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                result2AddLayout2.getLayoutParams().height = value;
                result2AddLayout2.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!isOpened2) {
                    result2AddLayout2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isOpened2) {
                    result2AddLayout2.setVisibility(View.GONE);
                }
                isOpened2 = !isOpened2;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        va.start();
    }

}
