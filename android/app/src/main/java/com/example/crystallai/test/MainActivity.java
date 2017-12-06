package com.example.crystallai.test;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bluetoothUtility.BluetoothConnectionListener;
import bluetoothUtility.ConnectionHandler;

public class MainActivity extends AppCompatActivity {
    private Button connect;
    private Button connectStatus;
    private Button connectConfirm;
    private Spinner sortT, agitateT;
    private Spinner demo;
    private ImageButton sort;
    private ImageButton agitate;
    private Switch lock;
    private Thread lockThread;
    private int time = 0;
    private String RxStr = "";
    private Handler heartBeathandler = new Handler();
    private Thread heartBeatThread = new Thread();
    //private static final String[] inst = {"0", "1", "2", "3", "4"};
    private ArrayAdapter<String> adapter;
    //    private String receivedString = "";
    private boolean bluetoothConnectedLock = false;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                ConnectionHandler.getInstance().sendBytes(("I").getBytes("UTF-8"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        connect = (Button) findViewById(R.id.button);
        connectStatus = (Button) findViewById(R.id.button2);
        connectConfirm = (Button) findViewById(R.id.button3);
        sortT = (Spinner) findViewById(R.id.SortTubes);
        demo = (Spinner) findViewById(R.id.checkBox);
        sort = (ImageButton) findViewById(R.id.imageButton2);
        agitate = (ImageButton) findViewById(R.id.imageButton);
        lock = (Switch) findViewById(R.id.switch2);
        agitateT = (Spinner) findViewById(R.id.Agitate);
        setButtonListeners(connect, sort, agitate);
        sortT.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        agitateT.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        demo.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lockModeOn();
                    lockCheckedListener(17000);
                } else {
                    //do stuff when Switch if OFF
                    lockThreadTerminator();
                    lockModeOff();
                }
            }
        });
        changeConnectButton();
        changeConnectionState();
        heartBeat();
    }

    private void initBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(getApplicationContext(), "This device does not support bluetooth.", Toast.LENGTH_LONG).show();
            return;
        }
        while (!adapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    void changeConnectionState() {//This function is used to change the connection button to green or red according to the state of the Connection
        if (ConnectionHandler.getInstance().isConnected()) {
            connectStatus.setText("Connected");
            connectStatus.setBackgroundColor(0xff00ff00);
        } else {
            connectStatus.setText("Disconnected");
            connectStatus.setBackgroundColor(Color.RED);
        }
    }

    void changeConnectButton() {//This function is used to change the connection button to green or red according to the state of the Connection
        if (ConnectionHandler.getInstance().isConnected()) {
            if (!bluetoothConnectedLock) {
                ConnectionHandler.getInstance().addBluetoothConnectionListener(new BluetoothConnectionListener() {
                    @Override
                    public void dataReceived(final byte[] bytes) {

                        try {
                            String newStr = new String(bytes, "UTF-8");
                            if (newStr.contains("K")) {
                                RxStr = newStr;
                                Log.d("testing", "data received" + RxStr);
                            }

                        }
                        catch (UnsupportedEncodingException e) {
                            RxStr = "";
                            Toast.makeText(getApplicationContext(), "Unable to receive data", Toast.LENGTH_LONG).show();
                        }
                        //                        }
                    }
                    //                    );
                });
            }
            bluetoothConnectedLock = true;
            connect.setText("Press to Disconnect");
            connectStatus.setBackgroundColor(0xff00ff00);
        } else {
            connectStatus.setText("Connect");
        }
    }

    private void lockModeOn() {
        sortT.setEnabled(false);
        agitateT.setEnabled(false);
        sort.setEnabled(false);
        agitate.setEnabled(false);
        demo.setEnabled(false);
        Toast.makeText(getApplicationContext(), "Robotic Arm In Demo Motion", Toast.LENGTH_SHORT).show();
    }

    private void lockModeOff() {
        sortT.setEnabled(true);
        agitateT.setEnabled(true);
        sort.setEnabled(true);
        agitate.setEnabled(true);
        demo.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Exitted Demo Motion", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBluetooth();
    }

    //Send "I" letter to Bluetooth Continuously
    private void lockCheckedListener(final int time) {
        Toast.makeText(getApplicationContext(), "In the Process of Sending Letter", Toast.LENGTH_SHORT).show();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // What do you want the thread to do
                try {
                    while (true) {
                        long futureTime = (System.currentTimeMillis() + time);
                        //                while (System.currentTimeMillis() < futureTime) {
                        synchronized (this) {
                            handler.sendEmptyMessage(0);
                            Log.w("microsemi_demo", "Empty message has been sent");
                            wait(futureTime - System.currentTimeMillis());
                        }
                    }
                }
                catch (InterruptedException e) {
                }
            }
        };
        lockThread = new Thread(r);
        lockThread.start();
    }

    private void lockThreadTerminator() {
        lockThread.interrupt();
        Log.w("microsemi_demo", "threadLocker terminator called");
        lockThread = null;
    }

    private void setButtonListeners(final Button connect, final ImageButton sort, final ImageButton agitate) {
        //Connects with Device via Bluetooth
        assert connect != null;
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionHandler.getInstance().isConnected()) {
                    try {
                        ConnectionHandler.getInstance().disconnect();
                    }
                    catch (IOException e) {
                        System.out.println("Info: Unknown Bluetooth exception" + " when disconnecting a bonded device");
                    }
                }
                Intent settingIntent = new Intent(MainActivity.this, BluetoothSettingsActivity.class);
                startActivity(settingIntent);
            }
        });
        assert sort != null;
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortT.performClick();
            }
        });
        assert agitate != null;
        agitate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agitateT.performClick();
            }
        });

    }

    //Code for Sending the Letter "K" Every 5 Seconds
    private void heartBeat() { //This function will handle the information received from bluetooth
        //        Toast.makeText(getApplicationContext(), "In the heartbeat thread", Toast.LENGTH_SHORT).show();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    //                    long futureTime = (System.currentTimeMillis() + 6000);//6 secs
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (RxStr.contains("K")) {
                                Log.d("testing", "testa" + RxStr + "working");
                                //                                Toast.makeText(getApplicationContext(), "Ready to Use", Toast.LENGTH_SHORT).show();
                                connectConfirm.setText("Ready to Use");
                                connectConfirm.setBackgroundColor(Color.GREEN);
                                //                        sortT.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                            } else {
                                Log.d("testing", "testa" + RxStr + "notworking");
                                //                                Toast.makeText(getApplicationContext(), "Not Ready to Use", Toast.LENGTH_SHORT).show();
                                connectConfirm.setText("Not Ready to Use");
                                connectConfirm.setBackgroundColor(Color.RED);
                            }
                            RxStr = "";
                        }
                    });

                }
                catch (Exception e) {
                    Log.d("testing exception", e.toString());
                }
                //                    wait(futureTime - System.currentTimeMillis());
            }
        };
        //        heartBeatThread = new Thread(r);
        //        heartBeatThread.start();
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleWithFixedDelay(r, 0, 6, TimeUnit.SECONDS);
    }

}