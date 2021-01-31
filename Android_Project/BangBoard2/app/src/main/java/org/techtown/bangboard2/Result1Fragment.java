package org.techtown.bangboard2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class Result1Fragment extends Fragment {

    boolean isScaled = false;
    boolean isOpened = false;

    MainActivity mainActivity;

    ImageView imageView;
    ImageView result1ImageView;
    RelativeLayout layout;
    LinearLayout result1AddLayout;

    TextView result1Name;
    TextView result1Point;
    TextView result1Position;
    TextView result1Method;

    TextView textTouch;

    Equipment result;

    public static Fragment newInstance (Equipment result) {
        Result1Fragment fragment = new Result1Fragment();
        Bundle args = new Bundle();
        args.putParcelable("result", result);
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
        View view = inflater.inflate(R.layout.result_1, container, false);

        imageView = view.findViewById(R.id.imageView);
        result1ImageView = view.findViewById(R.id.result1ImageView);
        layout = view.findViewById(R.id.layout);
        result1Name = view.findViewById(R.id.result1Name);
        result1Point = view.findViewById(R.id.result1Point);
        result1Position = view.findViewById(R.id.result1Position);
        result1Method = view.findViewById(R.id.result1Method);
        result1AddLayout = view.findViewById(R.id.result1AddLayout);
        textTouch = view.findViewById(R.id.textTouch);

        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(1000);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(Animation.REVERSE);
        textTouch.startAnimation(anim);

        if (getArguments() != null) {
            result = getArguments().getParcelable("result");
        }

        imageView.setImageBitmap(MainStartFragment.bitmap);

        Glide.with(this).load(MainActivity.DB_URL + result.getImage()).into(result1ImageView);

        result1Name.setText(result.getName());
        result1Point.setText(result.getPoint());
        result1Position.setText(result.getPosition());
        result1Method.setText(result.getMethod());

        result1Name.setOnClickListener(new View.OnClickListener() {  // 운동기구 이름 클릭 시 이벤트 -> 추가적인 정보들이 나옴
            @Override
            public void onClick(View view) {
                textTouch.clearAnimation();
                textTouch.setVisibility(View.GONE);
                showLayout();
            }
        });

        ResultMainFragment.layoutResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScaled) {
                    changeScale();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScaled) {
                    changeScale();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScale();
            }
        });

        /*result1AddLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  // 뷰의 넓이와 높이 동적으로 구하기
            @Override
            public void onGlobalLayout() {
                int width = result1AddLayout.getMeasuredWidth();
                int height = result1AddLayout.getMeasuredHeight();

                Log.i("Bang", "테스트값 width : " + width + ", " + "테스트값 height : " + height);
            }
        });*/

        return view;
    }

    public void changeScale () {  // 이미지뷰 클릭 시 확대시키는 메소드 -> ValueAnimator 이용
        int oldValue = 150;
        int newValue = 400;
        float d = mainActivity.getResources().getDisplayMetrics().density;

        int oldParam = (int) (oldValue * d);
        int newParam = (int) (newValue * d);

        ValueAnimator va = isScaled ? ValueAnimator.ofInt(newParam, oldParam) : ValueAnimator.ofInt(oldParam, newParam);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                imageView.getLayoutParams().width = value;
                imageView.getLayoutParams().height = value;
                imageView.requestLayout();
            }
        });
        va.start();
        isScaled = !isScaled;  // isScaled 값을 참이면 거짓으로, 거짓이면 참으로 변경
    }

    public void showLayout() {
        int dpValue = 680;
        float d = mainActivity.getResources().getDisplayMetrics().density;

        int height = (int) (dpValue * d);

        ValueAnimator va = isOpened ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                result1AddLayout.getLayoutParams().height = value;
                result1AddLayout.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (isOpened == false) {
                    result1AddLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isOpened) {
                    result1AddLayout.setVisibility(View.GONE);
                }
                isOpened = !isOpened;
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
