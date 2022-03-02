package com.example.androbluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.myViewHolder>{

    Context context;
    private List<BluetoothDevice> data;


    public DeviceListAdapter(Context context, List<BluetoothDevice> data) {
        this.context = context;
        this.data = data;
        context.registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        BluetoothDevice device	= data.get(position);
        holder.nameTv.setText(device.getName());
        holder.addressTv.setText(device.getAddress());
        holder.pairBtn.setText((device.getBondState() == BluetoothDevice.BOND_BONDED) ? "Unpair" : "Pair");

        holder.pairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);
            }
            else {
                Toast.makeText(context, "Pairing...", Toast.LENGTH_SHORT).show();
            pairDevice(device);
                }

            }
        });
    }

        private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView addressTv;
        Button pairBtn;

      public myViewHolder(@NonNull View itemView) {
          super(itemView);

          nameTv = itemView.findViewById(R.id.tv_name);
          addressTv = itemView.findViewById(R.id.tv_address);
          pairBtn = itemView.findViewById(R.id.btn_pair);
      }

  }

        public final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {

                    Toast.makeText(context, "Paired", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){

                    Toast.makeText(context, "Unpaired", Toast.LENGTH_SHORT).show();
                }
            }

            notifyDataSetChanged();
        }
    };
}
