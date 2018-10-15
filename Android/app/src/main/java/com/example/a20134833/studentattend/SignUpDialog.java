package com.example.a20134833.studentattend;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.a20134833.studentattend.MainActivity.MainActContext;

public class SignUpDialog extends Dialog implements View.OnClickListener {

    MainActivity mainActivity = new MainActivity();
    SignUp signUp = new SignUp();

    public SignUpDialog(@NonNull Context context) {
        super(context);
    }

    public SignUpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SignUpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    EditText UpID, UpPW, UpPhoneNum;
    TextView UpCancel, UpOK;
    RadioGroup UpRadioG;
    RadioButton UpRs, UpRp;

    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dialog);

        UpID = findViewById(R.id.UpID);
        UpPW = findViewById(R.id.UpPW);
        UpPhoneNum = findViewById(R.id.UpPhone);

        UpOK = findViewById(R.id.UpOK);
        UpCancel = findViewById(R.id.UpCancel);

        UpPhoneNum.setFocusable(false);
        UpPhoneNum.setClickable(false);

        UpPhoneNum.setText(mainActivity.ET_phone.getText().toString());

        UpOK.setOnClickListener(SignUpDialog.this);
        UpCancel.setOnClickListener(SignUpDialog.this);

        UpRs = findViewById(R.id.UpStudent);
        UpRp = findViewById(R.id.UpProf);
        UpRs.setChecked(true);

        UpRadioG = findViewById(R.id.UpRadioG);

        UpRadioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)  {
                    case R.id.UpStudent:
                        Toast.makeText(MainActContext, "학생회원으로 가입합니다.", Toast.LENGTH_SHORT).show();
                        status = false;
                        UpID.setHint("8자리 학번 입력");
                        break;
                    case R.id.UpProf:
                        Toast.makeText(MainActContext, "교수회원으로 가입합니다.", Toast.LENGTH_SHORT).show();
                        status = true;
                        UpID.setHint("6자리 교번 입력");
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String sign = null;
        switch (v.getId())  {
            case R.id.UpOK: // false = 학생, true = 교수
                if(!status) {
                    sign = "student";
                }   else    {
                    sign = "profess";
                }
                signUp.SignUp_Asycn(UpID.getText().toString(), UpPW.getText().toString(), UpPhoneNum.getText().toString(), sign);

                break;
            case R.id.UpCancel:
                break;
        }
    }
}
