package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class SelectActivity extends AppCompatActivity {

    private BluetoothAdapter myAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> devicesToList = new ArrayList<>();
    private BluetoothDevice device;

    private DeviceListAdapter listAdapter;
    private static final int BLUETOOTH_ENABLE_REQUEST = 0;

    private ListView deviceList;
    String instruction = "Válaszd ki az autódat! Ne feledd, először párosítanod kell a telefonnal, hogy megjelenjen a listában!";
    private SharedPreferences sharedPreferences;
    private String sharedPrefName = "apppref";



    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ENABLE_REQUEST) {
            if (myAdapter.isEnabled()) {
                deviceListing();
            } else {
                Toast.makeText(getApplicationContext(),"Bluetooth bekapcsolása sikertelen",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        deviceList = (ListView) findViewById(R.id.pairedDevices);
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        sharedPreferences = getSharedPreferences(sharedPrefName, MODE_PRIVATE);
        if(!myAdapter.isEnabled())
        {
            Intent bluSwitchON = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluSwitchON,BLUETOOTH_ENABLE_REQUEST);
        }
        else {
            deviceListing();
        }

        if (savedInstanceState == null)
        {
            Dialog(instruction);
        }

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                device = (BluetoothDevice) deviceList.getItemAtPosition(position);
                boolean success;
                BluetoothConnectionService startoConnect = new BluetoothConnectionService(device,getApplicationContext());
                success = startoConnect.connectCarViaBluetooth();

                if(success)
                {
                    ConnectionData.getInstance().myBtService = startoConnect;
                    ConnectionData.getInstance().connected = true;
                    Intent back = new Intent();
                    setResult(RESULT_OK,back);
                    finish();

                }
            }
        });
    }

    private void deviceListing() {
        pairedDevices = myAdapter.getBondedDevices();
        for (BluetoothDevice device:pairedDevices) {
            devicesToList.add(device);
        }
        listAdapter = new DeviceListAdapter(getApplicationContext(), R.layout.adapter_view_layout, devicesToList);
        deviceList.setAdapter(listAdapter);
    }
    private void Dialog(String message) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(SelectActivity.this);
        mDialog.setMessage(message);
        mDialog.setCancelable(true);

        mDialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog myDialog = mDialog.create();
        myDialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}