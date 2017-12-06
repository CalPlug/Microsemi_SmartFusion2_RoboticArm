package com.example.crystallai.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

import bluetoothUtility.BluetoothDeviceAdapter;
import bluetoothUtility.ConnectionHandler;

/**
 * Created by johnlcy on 2/24/2017.
 */

public class BluetoothSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // initialize objects
        ListView deviceList = (ListView) findViewById(R.id.connect_list);
        final Context context = getApplicationContext();
        final Button buttonPair = (Button) findViewById(R.id.button_pair);
        final Button buttonDone = (Button) findViewById(R.id.button_submit);
        setButtonEvents(buttonPair, buttonDone);

        // set bluetooth adapter
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        final BluetoothDeviceAdapter deviceAdapter = new BluetoothDeviceAdapter(context, 0);

        // initialize device list
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice i : devices) {
            deviceAdapter.add(i);
        }
        deviceList.setAdapter(deviceAdapter);

        // set connection handler
        setConnectionHandler(context, deviceList, deviceAdapter);
    }

    /**
     *
     * @param context
     * @param deviceList
     * @param deviceAdapter
     */
    private void setConnectionHandler(final Context context,
                                      ListView deviceList,
                                      final BluetoothDeviceAdapter deviceAdapter) {
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * connect to selected device
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                boolean connected = true;
                BluetoothDevice device = deviceAdapter.getItem(position);

                try {
                    ConnectionHandler.getInstance().connect(device);
                    Toast.makeText(context,
                            "Connected to " + device.getName(),
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    connected = false;
                    Toast.makeText(context,
                            "Device " + device.getName() + " is not in range.",
                            Toast.LENGTH_LONG).show();
                }

                if (connected) {
                    // return to main activity
                    Intent mainIntent = new Intent(BluetoothSettingsActivity.this,
                            MainActivity.class);
                    startActivity(mainIntent);
                }
                //finish();
            }
        });
    }

    private void setButtonEvents(Button buttonPair, Button buttonDone) {
        assert buttonPair != null;
        buttonPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start pair activity
                Intent pairIntent = new Intent(BluetoothSettingsActivity.this,
                        BluetoothPairActivity.class);
                startActivity(pairIntent);
            }
        });

        assert buttonDone != null;
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to main activity
                Intent mainIntent = new Intent(BluetoothSettingsActivity.this,
                        MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }


}
