package bluetoothUtility;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.crystallai.test.R;

/**
 * ArrayAdapter for list of Bluetooth devices. Displays Bluetooth device name
 * and address.
 * @author Ivan Pavic, adapted by Sahil
 *
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    public BluetoothDeviceAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        TextView deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
        deviceName.setText(" " + getItem(position).getName());
        deviceAddress.setText(" " + getItem(position).getAddress());

        return convertView;
    }
}