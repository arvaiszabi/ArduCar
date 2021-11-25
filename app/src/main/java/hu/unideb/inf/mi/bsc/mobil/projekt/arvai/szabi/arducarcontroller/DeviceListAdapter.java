package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
    private static final String TAG = "DeviceListAdapter";
    private ArrayList<BluetoothDevice> mDevices;
    private Context myContext;
    private int mResource;

    public DeviceListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BluetoothDevice> objects) {
        super(context, resource, objects);
        mDevices = objects;
        myContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BluetoothDevice device = mDevices.get(position);
        String name = device.getName();
        String address = device.getAddress();
        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView devName = (TextView) convertView.findViewById(R.id.deviceName);
        TextView devAddress = (TextView) convertView.findViewById(R.id.deviceAddress);

        devName.setText(name);
        devAddress.setText(address);

        return convertView;
    }
}