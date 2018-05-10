package com.example.a20134833.studentattend;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.a20134833.studentattend.databinding.ActivityBeaconReceiveBinding;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.distance.AndroidModel;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class BeaconReceive extends BaseActivity implements BeaconConsumer {

    public static Context BeaconReceiveContext;

        private static final String BEACON_PARSER = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-27";

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private static final String TAG = "BeaconReceive";

    private static final int REQUEST_ENABLE_BT = 100;

    BluetoothAdapter mBluetoothAdapter;

    BeaconAdapter beaconAdapter;

    ActivityBeaconReceiveBinding binding;

    BeaconManager mBeaconManager;

    Vector<Item> items;

    LinearLayoutManager manager;

    static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beacon_receive);

        Intent intent = getIntent();
        userid = intent.getStringExtra("id");
        binding.UserID.setText("접속 ID : " + userid);

        BeaconReceiveContext = this;

        AndroidModel am = AndroidModel.forThisDevice();
        Log.d("getManufacturer()",am.getManufacturer());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mBeaconManager = BeaconManager.getInstanceForApplication(this);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_PARSER));
            //BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        }

        binding.ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBeaconManager.isBound(BeaconReceive.this)) {
                    StopBLE();
                } else {
                    StartBLE();
                }
            }
        });
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }



    public void StopBLE()  {
        binding.ScanBtn.setText("Start Search!");
        Log.i(TAG, "Stop BLE Scanning...");
        mBeaconManager.unbind(BeaconReceive.this);
    }
    private void StartBLE() {
        binding.ScanBtn.setText("now searching...");
        Log.i(TAG, "Start BLE Scanning...");
        mBeaconManager.bind(BeaconReceive.this);
    }
    public void StopBLE2()  {
        Log.i(TAG, "Stop BLE Scanning...");
        mBeaconManager.unbind(BeaconReceive.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            mBeaconManager = BeaconManager.getInstanceForApplication(this);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_PARSER));
            //BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        }

    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Iterator<Beacon> iterator = beacons.iterator();
                    items = new Vector<>();
                    while (iterator.hasNext()) {
                        Beacon beacon = iterator.next();
                        final double distance = Double.parseDouble(decimalFormat.format(beacon.getDistance()));
                        final String uuid = beacon.getId1().toString();
                        final int major = beacon.getId2().toInt();
                        final int minor = beacon.getId3().toInt();
                        final List<Long> datafield = beacon.getDataFields();
                        Log.e("UUID =", uuid);
                        Log.e("Datafield = ", String.valueOf(datafield));
                        //items.add(new Item(String.valueOf(major), String.valueOf(minor), String.valueOf(datafield), String.valueOf(distance)));
                        if (uuid.equals("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")) {
                            Log.e("find", major +"/"+minor+"/"+datafield);
                            items.add(new Item(String.valueOf(major), String.valueOf(minor), String.valueOf(datafield), String.valueOf(distance)));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                beaconAdapter = new BeaconAdapter(items, BeaconReceive.this);
                                binding.beaconListView.setLayoutManager(manager);
                                binding.beaconListView.setAdapter(beaconAdapter);
                                beaconAdapter.notifyDataSetChanged();
                            }
                        });
                        try {
                            Log.e("BeaconReceive", "Sleep 5000");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e("BeaconReceive", "Sleep Error");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }


            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
