package com.example.a20134833.studentattend;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a20134833.studentattend.MainActivity.MainActContext;

/**
 * Created by 20134833 on 2018-05-04.
 */

public class SignIn {

    public static final String url = "http://117.16.23.140/beaconattend/SignIn.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private static Context context;
    private int error_message = 0;
    public static MainActivity mainact =  new MainActivity();

    public SignIn() {
    }

    public SignIn(Context c) {
        this.context = c;
    }

    public String returnvalue;

    public void signin_Asycn(final String id, final String pw, final String phonenum) {
        (new AsyncTask<MainActivity, Void, String>() {

            @Override
            protected String doInBackground(MainActivity... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, id, pw, phonenum);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result){

            }
        }).execute();

        return;
    }

    class ConnectServer {//Client 생성

        public int requestPost(String url, final String id, String pw, String phone) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", id)
                    .add("pw", pw)
                    .add("phonenum", phone).build();


            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        responseBody = response.body().string();
                        returnvalue = responseBody;
                        Sin_Process(id, returnvalue);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }

    public void Sin_Process(final String id, final String value) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (value.equals("Success")) { // 로그인 성공 -> 화면 전환 (비콘 받을 준비)
                    Intent BdataIntent = new Intent(MainActContext.getApplicationContext(), BeaconReceive.class);
                    BdataIntent.putExtra("id", id);
                    BdataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity mainact = (MainActivity)MainActivity.MainAct;
                    MainActContext.startActivity(BdataIntent);
                    mainact.finish();

                } else if (value.equals("pwIncorrect")) {
                    Toast.makeText(MainActContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (value.equals("NoData")) {
                    Toast.makeText(MainActContext, "데이터를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActContext, "로그인 할 수 없습니다.\n" + value, Toast.LENGTH_SHORT).show();
                }

            }
        }, 0);
    }
}