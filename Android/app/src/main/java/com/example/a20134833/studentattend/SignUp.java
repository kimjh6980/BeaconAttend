package com.example.a20134833.studentattend;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 20134833 on 2018-05-12.
 */

public class SignUp {

    public static final String url = "http://117.16.23.140/beaconattend/SignUp.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private static Context context;
    private int error_message = 0;
    public static MainActivity mainact =  new MainActivity();

    public SignUp() {
    }

    public SignUp(Context c) {
        this.context = c;
    }

    public String returnvalue;

    public void SignUp_Asycn(final String id, final String pw, final String phonenum, final String sign) {
        (new AsyncTask<MainActivity, Void, String>() {

            @Override
            protected String doInBackground(MainActivity... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, id, pw, phonenum, sign);
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

        public int requestPost(String url, final String id, String pw, String phone, String sign) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", id)
                    .add("pw", pw)
                    .add("phonenum", phone)
                    .add("sign", sign)
                    .build();


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
                        Toast.makeText(mainact, responseBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}