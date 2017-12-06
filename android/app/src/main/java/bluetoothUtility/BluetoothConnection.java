package bluetoothUtility;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author Sahil
 */

public class BluetoothConnection extends Thread {

    private InputStream input;
    private OutputStream output;
    private int lastValue;
    private boolean isStopped;
    private BluetoothSocket socket;
    private List<BluetoothConnectionListener> listeners;

    public BluetoothConnection(BluetoothDevice device, List<BluetoothConnectionListener> listeners) throws IOException {
        socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        input = socket.getInputStream();
        output = socket.getOutputStream();
        this.listeners = listeners;
        socket.connect();
    }

    @Override
    public void run() {
        int bytes;
        byte[] buffer = new byte[256];
        while (!isStopped) {
            try {
                bytes = input.read(buffer);
                if (bytes != -1) {
                    fire(buffer, bytes);
                }
            }
            catch (IOException e) {
                continue;
            }
        }
    }

    private void fire(byte[] buffer, int bytes) {
        byte[] forExport = new byte[bytes];
//        Log.d("testing_bluetooth", "fire run");
        for (int i = 0; i < bytes; i++) {
            forExport[i] = buffer[i];
        }
        for (BluetoothConnectionListener i : listeners) {
            i.dataReceived(forExport);
        }
    }

    public void write(byte[] bytes) throws IOException {
        output.write(bytes);
    }

    public void write(int b) throws IOException {
        output.write(b);
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }
}

