package com.example.a20134833.studentattend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ET_id, ET_pw;
    public static EditText ET_phone;
    Button B_signup, B_signin;
    RadioButton Bs, Bp;
    RadioGroup radiogroup;
    boolean status = false; // false = 학생, true = 교수

    public static Activity MainAct;
    public static Context MainActContext;
    public static SignIn signin = new SignIn();
    public static profSignIn profSignIn = new profSignIn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainAct = MainActivity.this;
        MainActContext = this.getApplicationContext();

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        String mPhoneNumber = "01001010101";
        mPhoneNumber = (tMgr.getLine1Number() != null || tMgr.getLine1Number() == "" ? tMgr.getLine1Number() : "01001010101");   // if null or "" -> test
        if(mPhoneNumber.contains("+82"))  {
            mPhoneNumber = mPhoneNumber.replace("+82", "0");
        }

        Log.e("확인용", mPhoneNumber);

        Bs = findViewById(R.id.radio_student);
        Bp = findViewById(R.id.radio_profess);
        Bs.setChecked(true);

        radiogroup = findViewById(R.id.radioGroup);

        ET_id = findViewById(R.id.ET_id);
        ET_pw = findViewById(R.id.ET_pw);
        ET_phone = findViewById(R.id.ET_phone);
        ET_phone.setText(mPhoneNumber);
        ET_phone.setFocusable(false);
        ET_phone.setClickable(false);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)  {
                    case R.id.radio_student:
                        Toast.makeText(MainActivity.this, "학생용으로 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        status = false;
                        ET_id.setHint("8자리 학번 입력");
                        break;
                    case R.id.radio_profess:
                        Toast.makeText(MainActivity.this, "교수용으로 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        status = true;
                        ET_id.setHint("6자리 교번 입력");
                        break;
                }
            }
        });

        B_signup = findViewById(R.id.B_SignUP);
        B_signup.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            SignUpDialog dialog = new SignUpDialog(MainActivity.this);
                                            dialog.show();
                                        }
                                    }
        );
        B_signin = findViewById(R.id.B_SignIn);
        B_signin.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            String id = ET_id.getText().toString();
                                            String pw = ET_pw.getText().toString();
                                            String phone = ET_phone.getText().toString();
                                            Log.e("SignIn Value = ", ET_id.getText().toString() + ET_pw.getText().toString() + ET_phone.getText().toString());
                                            if(!status) {
                                                signin.signin_Asycn(id, pw, phone);
                                            }   else    {
                                                profSignIn.profSignIn_Asycn(id, pw, phone);
                                            }

                                        }
                                    }
        );
    }


}