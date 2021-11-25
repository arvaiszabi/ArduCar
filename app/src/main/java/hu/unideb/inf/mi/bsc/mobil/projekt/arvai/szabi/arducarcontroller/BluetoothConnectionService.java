package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectionService extends Thread {

    private BluetoothAdapter deviceAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice arduCar;
    private BluetoothSocket blueSocket;
    private OutputStream blueOutStream;
    private final UUID arduCarUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private Context appContext;
    private boolean connectedSuccessfully = false;
    private boolean isNotified = false;

    public BluetoothConnectionService(BluetoothDevice arduCar,Context context) {
        this.arduCar = arduCar;
        this.appContext = context;
    }
    public boolean connectCarViaBluetooth()
    {
        run();
        return connectedSuccessfully;
    }

    @Override
    public void run() {
        deviceAdapter.cancelDiscovery();
        try {
            blueSocket = arduCar.createRfcommSocketToServiceRecord(arduCarUUID);
            blueSocket.connect();
            Toast.makeText(appContext,"Sikeres kapcsolódás!",Toast.LENGTH_LONG).show();
            connectedSuccessfully = true;
            if(connectedSuccessfully)
                blueOutStream = blueSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commandToArduCar(Context context, byte command){
            try {
                blueOutStream.write(command);
                isNotified = false;
            } catch (IOException e) {
                connectedSuccessfully = false;
                ConnectionData.getInstance().connected = false;
                e.printStackTrace();
                if(!isNotified)
                {
                    isNotified = true;
                    Toast.makeText(context, "Hiba a Bluetooth kapcsolatban! Próbálj újra csatlakozni!", Toast.LENGTH_SHORT).show();
                }
            }
        }
}