package com.example.a20134833.studentattend;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BeaconTransmitter extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "sampleCreateBeacon";
    public static Context beacontransmitterContext;

    final Calendar cal = Calendar.getInstance();

    boolean statusbool = false;
    Button button;

    boolean status;
    profAddday addday = new profAddday();

    org.altbeacon.beacon.BeaconTransmitter beaconTransmitter;
    BeaconParser beaconParser;
    BeaconManager beaconManager;
    Beacon beacon;

    String profidS;
    TextView profid;
    TextView  classtitle;
    EditText t1, t2, t3;

    List<String> list;
    ListView classListView;

    RadioButton startbtn, endbtn;
    RadioGroup attendradio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_transmitter);

        beacontransmitterContext = this;

        button = findViewById(R.id.Attend);

        classtitle = findViewById(R.id.classtitle);
        t1 = findViewById(R.id.t1);
        t1.setFocusable(false);
        t1.setClickable(false);
        t2 = findViewById(R.id.t2);
        t2.setFocusable(false);
        t2.setClickable(false);
        t3 = findViewById(R.id.t3);
        t3.setFocusable(false);
        t3.setClickable(true);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BeaconTransmitter.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String Syear = String.valueOf(year).substring(2);
                        String Smon = String.format("%02d", month+1);
                        String Sdate = String.format("%02d", date);
                        String msg = Syear+Smon+Sdate;
                        t3.setText(msg);
                        //String msg = String.format("%d 년 %d 월 %d 일", year, month+1, date);
                        //Toast.makeText(BeaconTransmitter.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

//                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        Intent intent = getIntent();
        profidS = intent.getStringExtra("id");

        profid = findViewById(R.id.profID);

        profid.setText("접속 ID : " + profidS);

        classListView =  findViewById(R.id.classListView);
        list = new ArrayList<>();
        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        //리스트뷰의 어댑터를 지정해준다.
        classListView.setAdapter(adapter);

        beacontransmitter_Asycn(profidS);

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select_item = (String)parent.getItemAtPosition(position);
                String[] value = select_item.split("/");
                String[] value2 = value[1].split("_");
                classtitle.setText("교과명 : " + value[0]);
                t1.setText(value2[0]);
                t2.setText(value2[1]);
                String test = "수업명: "+value[0] +"\n교과번호: "+value2[0]+"\n분반: "+value2[1];
                Toast.makeText(BeaconTransmitter.this, test, Toast.LENGTH_SHORT).show();
            }
        });

        startbtn = findViewById(R.id.radio_Start);
        endbtn = findViewById(R.id.radio_End);
        startbtn.setChecked(true);

        attendradio = findViewById(R.id.attendradio);
        attendradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)  {
                    case R.id.radio_Start:
                        Toast.makeText(BeaconTransmitter.this, "시작모드", Toast.LENGTH_SHORT).show();
                        status = false;
                        break;
                    case R.id.radio_End:
                        Toast.makeText(BeaconTransmitter.this, "종료모드", Toast.LENGTH_SHORT).show();
                        status = true;
                        break;
                }
            }
        });

        // 퍼미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }
        beaconManager = BeaconManager.getInstanceForApplication(this);
    }

    public void additem(String name, String num)   {
        String data = name +"/"+num;
        list.add(data);
    }

    public void button(View view)    {
        //String uuid = "2f234454-cf6d-4a0f-adf2-"+t1.getText().toString()+t2.getText().toString()+t3.getText().toString();
        String uuid = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";

        String id2 = t1.getText().toString();
        String id3 = t2.getText().toString();
        int dayy = Integer.valueOf(t3.getText().toString());

        Long[] day = {Long.valueOf(dayy)};
        if (!statusbool) {   // false = 꺼진상태 -> 켜야됨
            // 비콘 생성 후 시작. 실제 가장 필요한 소스
            beacon = new Beacon.Builder()
                    .setId1(uuid)  // uuid for beacon
                    .setId2(id2)  // major
                    .setId3(id3)  // minor
                    .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                    .setTxPower(-59)  // Power in dB
                    //.setDataFields(Arrays.asList(new Long[]{0l}))  // Remove this for beacon layouts without d: fields
                    .setDataFields(Arrays.asList(day))
                    .build();
            Log.e("beacon data =", String.valueOf(beacon.getDataFields()));
            beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-27");
        /* beacon layout
        ALTBEACON   "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"
        EDDYSTONE  TLM  "x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"
        EDDYSTONE  UID  "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"
        EDDYSTONE  URL  "s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"
        IBEACON  "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        m: 에는 비턴 타입을 나타내는 문자열을 매칭하며 한번만 기술한다.
        i: ID에 해당하는 필드로 여러 개를 정의하여 매칭 할 수 있다.
        p: power calibration 필드로 한번만 기술한다.
        d: 추가 데이터 필드로 여러 개를 정의하여 매칭 할 수 있다.
         */

            beaconTransmitter = new org.altbeacon.beacon.BeaconTransmitter(getApplicationContext(), beaconParser);
            Log.e("beaconParser/", String.valueOf(beaconParser));
            Log.e("Transmitter/", String.valueOf(beaconTransmitter));

            beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    Log.d(TAG, "onStartSuccess: ");
                    statusbool = true;
                }

                @Override
                public void onStartFailure(int errorCode) {
                    super.onStartFailure(errorCode);
                    Log.d(TAG, "onStartFailure: " + errorCode);
                }
            });
        } else    {   // true = 켜진상태 -> 꺼야됨
            beaconTransmitter.stopAdvertising();
            statusbool = false;
        }

    }

    // 퍼미션 요청후 callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void AddDay(View view) {
        //false = 시작, true = 종료
        String classnum = t1.getText().toString() +"_"+t2.getText().toString();
        String adddayv = t3.getText().toString();
        if(!status) {
            adddayv = "S"+adddayv;
        }   else    {
            adddayv = "E"+adddayv;
        }
        Toast.makeText(beacontransmitterContext, classnum +"/"+adddayv, Toast.LENGTH_SHORT).show();
        //addday.profAddday_Asycn(classnum, adddayv);
    }

    //------------------------------------------
    public static final String url = "http://117.16.23.140/beaconattend/Classlist.php";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private static Context context;
    private int error_message = 0;

    public void beacontransmitter_Asycn(final String id) {
        (new AsyncTask<BeaconTransmitter, Void, String>() {

            @Override
            protected String doInBackground(BeaconTransmitter... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, id);
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

        public int requestPost(String url, final String id) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", id).build();


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
                        JSONArray jsonArray = new JSONArray(responseBody);
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject getclass =  jsonArray.getJSONObject(i);
                            String name = getclass.getString("name");
                            String num = getclass.getString("class");
                            additem(name, num);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}