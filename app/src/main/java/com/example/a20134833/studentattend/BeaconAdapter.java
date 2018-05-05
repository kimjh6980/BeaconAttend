package com.example.a20134833.studentattend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.a20134833.studentattend.databinding.ActivityBeaconListBinding;

import java.util.Vector;

/**
 * Created by 20134833 on 2018-05-05.
 */

class BeaconAdapter extends RecyclerView.Adapter {

    private Vector<Item> items;
    private Context context;


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
        try {
            ItemHolders itemViewHolder = (ItemHolders) holder;
            final ActivityBeaconListBinding binding = itemViewHolder.binding;
            binding.TM.setText(""+items.get(position).getM()+"반");
            binding.TN.setText(""+items.get(position).getN()+"분반");
            String dayS = items.get(position).getD();
            dayS = dayS.replaceAll("\\[", "");
            dayS = dayS.replaceAll("\\]", "");
            binding.TDay.setText(dayS);
            binding.TDistance.setText(items.get(position).getL()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
