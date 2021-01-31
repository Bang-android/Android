package org.techtown.bangboard2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class Result3Fragment extends Fragment {

    boolean isOpened_result = false;
    boolean isOpened_re1 = false;
    boolean isOpened_re2 = false;

    MainActivity mainActivity;

    TextView resultTextView;
    TextView re1TextView;
    TextView re2TextView;

    YouTubePlayerView youTubePlayer_result;
    YouTubePlayerView youTubePlayer_re1;
    YouTubePlayerView youtubePlayer_re2;

    ArrayList<Equipment> resultorrecommend;  // 첫 번쨰 인덱스의 Equipment 객체가 인식 결과이고 나머지 두 개는 추천 운동기구 Equipment 객체이다.

    public static Fragment newInstance (ArrayList<Equipment> resultorrecommend) {
        Result3Fragment fragment = new Result3Fragment();
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
        View view = inflater.inflate(R.layout.result_3, container, false);

        resultTextView = view.findViewById(R.id.resultTextView);
        re1TextView = view.findViewById(R.id.re1TextView);
        re2TextView = view.findViewById(R.id.re2TextView);

        youTubePlayer_result = view.findViewById(R.id.youtubePlayer_result);
        youTubePlayer_re1 = view.findViewById(R.id.youtubePlayer_re1);
        youtubePlayer_re2 = view.findViewById(R.id.youtubePlayer_re2);

        if (getArguments() != null) {
            resultorrecommend = getArguments().getParcelableArrayList("resultorrecommend");
        }

        resultTextView.setText(resultorrecommend.get(0).getName());
        re1TextView.setText(resultorrecommend.get(1).getName());
        re2TextView.setText(resultorrecommend.get(2).getName());

        youTubePlayer_result.setEnableAutomaticInitialization(false);
        youTubePlayer_result.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(resultorrecommend.get(0).getYoutube_id(), 0);
            }
        });
        youTubePlayer_re1.setEnableAutomaticInitialization(false);
        youTubePlayer_re1.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(resultorrecommend.get(1).getYoutube_id(), 0);
            }
        });
        youtubePlayer_re2.setEnableAutomaticInitialization(false);
        youtubePlayer_re2.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(resultorrecommend.get(2).getYoutube_id(), 0);
            }
        });

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayout(0, youTubePlayer_result);
            }
        });

        re1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayout(1, youTubePlayer_re1);
            }
        });

        re2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayout(2, youtubePlayer_re2);
            }
        });

        return view;
    }

    public void showLayout(int flag, YouTubePlayerView youTubePlayerView) {
        Log.i("Bang", "isOpened_result : " + isOpened_result + ", " + "isOpened_re1 : " + isOpened_re1 + ", " + "isOpened_re2 : " + isOpened_re2);
        int dpValue = 230;
        float d = mainActivity.getResources().getDisplayMetrics().density;

        int height = (int) (dpValue * d);

        ValueAnimator va = null;
        if (flag == 0) {
            va = isOpened_result ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        } else if (flag == 1) {
            va = isOpened_re1 ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        } else if (flag == 2) {
            va = isOpened_re2 ? ValueAnimator.ofInt(height, 0) : ValueAnimator.ofInt(0, height);
        }
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                youTubePlayerView.getLayoutParams().height = value;
                youTubePlayerView.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (flag == 0) {
                    if (!isOpened_result) {
                        youTubePlayerView.setVisibility(View.VISIBLE);
                    }
                } else if (flag == 1) {
                    if (!isOpened_re1) {
                        youTubePlayerView.setVisibility(View.VISIBLE);
                    }
                } else if (flag == 2) {
                    if (!isOpened_re2) {
                        youTubePlayerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (flag == 0) {
                    if (isOpened_result) {
                        youTubePlayerView.setVisibility(View.GONE);
                    }
                    isOpened_result = !isOpened_result;
                } else if (flag == 1) {
                    if (isOpened_re1) {
                        youTubePlayerView.setVisibility(View.GONE);
                    }
                    isOpened_re1 = !isOpened_re1;
                } else if (flag == 2) {
                    if (isOpened_re2) {
                        youTubePlayerView.setVisibility(View.GONE);
                    }
                    isOpened_re2 = !isOpened_re2;
                }
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
