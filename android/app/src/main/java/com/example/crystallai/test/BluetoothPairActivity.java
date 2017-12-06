package com.example.crystallai.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import bluetoothUtility.BluetoothDeviceAdapter;

/**
 * Created by johnlcy on 2/24/2017.
 */

public class BluetoothPairActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        // initialize objects
        ListView deviceList = (ListView) findViewById(R.id.pair_list);
        Context context = getApplicationContext();
        Button buttonSubmit = (Button) findViewById(R.id.button_done_pairing);
        setButtonEvents(context, buttonSubmit);

        // initialize bluetooth adapter
        final BluetoothDeviceAdapter deviceAdapter = new
                BluetoothDeviceAdapter(getApplicationContext(), 0);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        setBluetoothReceiver(deviceAdapter);
        adapter.startDiscovery();


        // initialize device list
        deviceList.setAdapter(deviceAdapter);
        setPairingHandler(context, deviceList, deviceAdapter);
    }

    /**
     * @author Ivan Pavic, adapted by Sahil
     * @param deviceAdapter
     */
    private void setBluetoothReceiver(final BluetoothDeviceAdapter deviceAdapter) {
        // get notify bluetooth events
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceAdapter.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        };

        // receive only new device events
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    /**
     *
     * @param context
     * @param deviceList
     * @param deviceAdapter
     */
    private void setPairingHandler(final Context context,
                                   ListView deviceList,
                                   final BluetoothDeviceAdapter deviceAdapter) {
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BluetoothDevice device = deviceAdapter.getItem(position);
                // bond device
                if(!device.createBond()) {
                    Toast.makeText(context, "Devices already bonded!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Devices paired successfully!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setButtonEvents(final Context context, Button buttonDone) {
        assert buttonDone != null;
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // no selection, return to setting activity
                Intent settingIntent = new Intent(BluetoothPairActivity.this,
                        BluetoothSettingsActivity.class);
                startActivity(settingIntent);
            }
        });
    }

}
