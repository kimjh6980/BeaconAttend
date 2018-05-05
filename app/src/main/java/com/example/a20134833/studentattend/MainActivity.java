package com.example.a20134833.studentattend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText ET_id, ET_pw, ET_phone;
    Button B_signup, B_signin;

    public static Activity MainAct;
    public static Context MainActContext;
    public static SignIn signin = new SignIn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainAct = MainActivity.this;
        MainActContext = this.getApplicationContext();

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String mPhoneNumber = (tMgr.getLine1Number() == "" ? tMgr.getLine1Number() : "01001010101");   // if null -> test
        Log.e("확인용", mPhoneNumber);

        ET_id = findViewById(R.id.ET_id);
        ET_pw = findViewById(R.id.ET_pw);
        ET_phone = findViewById(R.id.ET_phone);
        ET_phone.setText(mPhoneNumber);
        ET_phone.setFocusable(false);
        ET_phone.setClickable(false);

        B_signup = findViewById(R.id.B_SignUP);
        B_signup.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent BdataIntent = new Intent(MainActContext.getApplicationContext(), BeaconReceive.class);
                                            BdataIntent.putExtra("id", "guest");
                                            BdataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            MainActivity mainact = (MainActivity)MainActivity.MainAct;
                                            MainActContext.startActivity(BdataIntent);
                                            mainact.finish();
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
                                            signin.signin_Asycn(id, pw, phone);
                                        }
                                    }
        );
    }


}