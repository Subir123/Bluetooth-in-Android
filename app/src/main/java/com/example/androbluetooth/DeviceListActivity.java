package com.example.androbluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mDeviceList = getIntent().getExtras().getParcelableArrayList("deviceList");
        recyclerView = (RecyclerView) findViewById(R.id.lv_paired);

        recyclerView.setLayoutManager(new LinearLayoutManager(DeviceListActivity.this));

        //This below single line code will add a line between each list item.
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mAdapter = new DeviceListAdapter(this, mDeviceList);

        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver( mAdapter.mPairReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}