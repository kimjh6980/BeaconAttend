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

public class profAddclass {
    public static final String url = "http://117.16.23.140/beaconattend/Addclass.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private static Context context;
    private int error_message = 0;
    public static MainActivity mainact =  new MainActivity();
    BeaconTransmitter beaconTransmitter = new BeaconTransmitter();

    public profAddclass() {
    }

    public profAddclass(Context c) {
        this.context = c;
    }

    public String returnvalue;



    public void profAddclass_Asycn(final String id, final String cname, final String cnum, final String ckname) {
        (new AsyncTask<BeaconTransmitter, Void, String>() {

            @Override
            protected String doInBackground(BeaconTransmitter... mainActivities) {
                profAddclass.ConnectServer connectServerPost = new profAddclass.ConnectServer();
                connectServerPost.requestPost(url, id, cname, cnum, ckname);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result){
                    beaconTransmitter.beacontransmitter_Asycn(id);

            }
        }).execute();

        return;
    }

    class ConnectServer {//Client 생성

        public int requestPost(String url, String id, String cname, String cnum, String ckname) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", id)
                    .add("cname", cname)
                    .add("cnum", cnum)
                    .add("ckname", ckname).build();


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
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        final String finalMsg = responseBody;
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
