package com.example.a20134833.studentattend;

import android.content.Context;
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

import static com.example.a20134833.studentattend.BeaconTransmitter.beacontransmitterContext;

/**
 * Created by 20134833 on 2018-05-10.
 */

public class profAddday {
    public static final String url = "http://117.16.23.140/beaconattend/addDay.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private static Context context;
    private int error_message = 0;
    public static MainActivity mainact =  new MainActivity();

    public profAddday() {
    }

    public profAddday(Context c) {
        this.context = c;
    }

    public String returnvalue;

    public void profAddday_Asycn(final String classnum, final String addday) {
        (new AsyncTask<BeaconTransmitter, Void, String>() {

            @Override
            protected String doInBackground(BeaconTransmitter... mainActivities) {
                profAddday.ConnectServer connectServerPost = new profAddday.ConnectServer();
                connectServerPost.requestPost(url, classnum, addday);
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

        public int requestPost(String url, String classnum, String addday) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("class", classnum)
                    .add("addday", addday).build();


            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) {
                    String msg = null;
                    try {
                        responseBody = response.body().string();
                        if(responseBody.equals("Success"))   {
                            msg = "생성되었습니다.";
                        }   else    {
                            msg = "Error :\n" + responseBody;
                        }
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        final String finalMsg = msg;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 내용
                                Toast.makeText(beacontransmitterContext, finalMsg, Toast.LENGTH_SHORT).show();
                            }
                        }, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}
