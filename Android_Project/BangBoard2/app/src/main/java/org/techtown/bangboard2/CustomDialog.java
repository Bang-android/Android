package org.techtown.bangboard2;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Objects;

public class CustomDialog extends Dialog {

    Context context;

    CustomDialogClickListener customDialogClickListener;

    String message;
    String btnYesText;

    public CustomDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener, String message, String btnYesText) {
        super(context);

        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
        this.message = message;
        this.btnYesText = btnYesText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 다이얼로그의 배경을 투명으로 만듦 -> 둥근 모서리의 커스텀 다이얼로그를 만들 수 있음

        Button btnYes = findViewById(R.id.btnYes);
        Button btnNo = findViewById(R.id.btnNo);
        TextView textView = findViewById(R.id.textView);

        textView.setText(message);
        btnYes.setText(btnYesText);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogClickListener.onPositiveClicked(); // CustomDialog를 생성할 때 이 동작을 정의해줌
                dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
