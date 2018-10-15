package com.example.a20134833.studentattend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a20134833.studentattend.databinding.ActivityBeaconListBinding;

import java.util.Vector;

/**
 * Created by 20134833 on 2018-05-05.
 */

public class BeaconAdapter extends RecyclerView.Adapter {

    private Vector<Item> items;
    public Context context;

    BeaconReceive BR = new BeaconReceive();
    public static AttendReq Areq = new AttendReq();

    BeaconAdapter(Vector<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityBeaconListBinding binding = ActivityBeaconListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ItemHolders(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ActivityBeaconListBinding binding;
        try {
            ItemHolders itemViewHolder = (ItemHolders) holder;
            binding = itemViewHolder.binding;
            binding.TM.setText(""+items.get(position).getM());
            binding.TN.setText(""+items.get(position).getN());
            String dayS = items.get(position).getD();
            dayS = dayS.replaceAll("\\[", "");
            dayS = dayS.replaceAll("\\]", "");
            binding.TDay.setText(dayS);
            binding.TDistance.setText(items.get(position).getL()+"");   // distance 를 출석상태로 바꿈

            // 여기가 리스트에서 클릭하는거
            binding.ImageAttend.setOnClickListener(new View.OnClickListener()    {
                @Override
                public void onClick(View v) {
                    String temp = binding.TDistance.getText().toString();
                    showdialog(BR.userid, binding.TM.getText().toString(), binding.TN.getText().toString(), binding.TDay.getText().toString(), temp);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showdialog(final String id, final String m, final String n, final String d, final String s)   {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(m + " - " + n);
        builder.setMessage(d + " 일자 " + s);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //BR.StopBLE2();
                        String temp = null;
                        if(s.equals("시작"))   {
                            temp = "S";
                        }   else    {
                            temp = "E";
                        }
                        String day = temp + d;
                        Areq.AttendReq_Asycn(id, m, n, day);
                        Log.i("BeaconAdapter - ", id+"/"+m+"/"+n+"/"+day);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ItemHolders extends RecyclerView.ViewHolder {

        ActivityBeaconListBinding binding;

        ItemHolders(ActivityBeaconListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
