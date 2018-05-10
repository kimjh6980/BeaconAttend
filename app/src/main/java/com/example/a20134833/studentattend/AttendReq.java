package com.example.a20134833.studentattend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import static com.example.a20134833.studentattend.BeaconReceive.BeaconReceiveContext;

/**
 * Created by 20134833 on 2018-05-10.
 */

public class AttendReq {

    public static final String url = "http://117.16.23.140/beaconattend/attend.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private Context context;
    private int error_message = 0;
    BeaconReceive BR = new BeaconReceive();

    public AttendReq() {
    }

    public AttendReq(Context c) {
        this.context = c;
    }

    public String returnvalue;

    public void AttendReq_Asycn(final String id, final String cname, final String cnum, final String day) {
        (new AsyncTask<BeaconReceive, Void, String>() {

            @Override
            protected String doInBackground(BeaconReceive... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, id, cname, cnum, day);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }).execute();

        return;
    }

    class ConnectServer {//Client 생성

        public int requestPost(final String url, final String id, final String cname, final String cnum, final String day) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", id)
                    .add("cname", cname)
                    .add("cnum", cnum)
                    .add("day", day).build();


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
                        AttendDialog(responseBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }

    public void AttendDialog(final String a)   {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String msg = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(BeaconReceiveContext);
                if(a.contains("yes"))    {
                    msg = "출석에 성공하였습니다.";
                }   else    {
                    msg = a;
                }
                builder.setTitle("Beacon출석하기");
                builder.setMessage(msg);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(a.contains("yes"))  {
                                    Toast.makeText(BeaconReceiveContext, "이제 끌꺼임", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();
            }
        }, 0);
    }
}