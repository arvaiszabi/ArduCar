package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button Forward, Backward, steerLeft, steerRight, Connect, mSense;
    private ImageView btIcon;
    private TextView missingBlu;
    BluetoothConnectionService connection;
    private BluetoothAdapter phoneAdapter;

    private static final byte move_forward = 'F';
    private static final byte move_backward = 'B';
    private static final byte stop = 'S';
    private static final byte turn_left = 'L';
    private static final byte turn_right = 'R';

    boolean isConnected = false;
    IntentFilter btState = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    BroadcastReceiver btSateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                int statVal = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                {
                    if(statVal == BluetoothAdapter.STATE_ON)
                    {
                        btIcon.setImageResource(R.drawable.ic_bt_on_foreground);
                    }
                    else if(statVal == BluetoothAdapter.STATE_OFF)
                    {
                        btIcon.setImageResource(R.drawable.ic_bt_off_foreground);
                    }
                    else if(statVal == BluetoothAdapter.STATE_CONNECTED)
                    {
                        //btIcon.setImageResource(R.drawable.ic_bt_con);
                    }
                }
            }
            if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED))
            {
                btIcon.setImageResource(R.drawable.ic_bt_con_foreground);
            }
            if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED))
            {
                btIcon.setImageResource(R.drawable.ic_bt_on_foreground);
            }
        }
    };

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==RESULT_OK){
                    connection = (BluetoothConnectionService) result.getData().getSerializableExtra("BLUETOOTH");
                    isConnected = true;
                    statusConfig();
                }
                else if(result.getResultCode() == RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(),"Hiba a kommunikációban!",Toast.LENGTH_LONG).show();
                    statusConfig();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btState.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        btState.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        Forward = (Button) findViewById(R.id.moveForward);
        Backward = (Button) findViewById(R.id.moveBackward);
        steerLeft = (Button) findViewById(R.id.moveLeft);
        steerRight = (Button) findViewById(R.id.moveRight);
        Connect = (Button) findViewById(R.id.connectBt);
        mSense = (Button) findViewById(R.id.mSense);

        missingBlu = (TextView) findViewById(R.id.missingBlu);

        btIcon = (ImageView) findViewById(R.id.btImage);

        phoneAdapter = BluetoothAdapter.getDefaultAdapter();

        if(phoneAdapter == null)
        {
            missingBlu.setText("Az eszköz nem támogatja a Bluetooth alapú adatátvitelt!");
            Connect.setClickable(false);
            Connect.setAlpha(.1F);
        }
        else
        {
            statusConfig();
        }
        registerReceiver(btSateReceiver,btState);
        Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(MainActivity.this,move_forward);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(MainActivity.this,stop);
                }

                return false;
            }

        });
        Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(MainActivity.this,move_backward);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),stop);
                }

                return false;
            }

        });
        steerRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),turn_right);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),stop);
                }

                return false;
            }

        });
        steerLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),turn_left);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),stop);
                }

                return false;
            }

        });
    }

    private void statusConfig()
    {
        if(phoneAdapter.isEnabled())
        {
            btIcon.setImageResource(R.drawable.ic_bt_on_foreground);
        }
        else if(!phoneAdapter.isEnabled())
        {
            btIcon.setImageResource(R.drawable.ic_bt_off_foreground);
        }
        if(!ConnectionData.getInstance().connected)
        {
            Forward.setClickable(false);
            Forward.setAlpha(.1F);
            Backward.setClickable(false);
            Backward.setAlpha(.1F);
            steerRight.setClickable(false);
            steerRight.setAlpha(.1F);
            steerLeft.setClickable(false);
            steerLeft.setAlpha(.1F);
            mSense.setClickable(false);
            mSense.setAlpha(.1F);
            btIcon.setImageResource((R.drawable.ic_bt_on_foreground));
        }
        else
        {
            Forward.setClickable(true);
            Forward.setAlpha(1);
            Backward.setClickable(true);
            Backward.setAlpha(1);
            steerRight.setClickable(true);
            steerRight.setAlpha(1);
            steerLeft.setClickable(true);
            steerLeft.setAlpha(1);
            mSense.setClickable(true);
            mSense.setAlpha(1);
            btIcon.setImageResource(R.drawable.ic_bt_con_foreground);
        }
    }

    public void connectDevice(View view) {
        Intent blueConnect = new Intent(getApplicationContext(), SelectActivity.class);
        activityResultLauncher.launch(blueConnect);
    }

    public void mSenseLaunch(View view) {
        Intent mSenseUI = new Intent(getApplicationContext(), MSense_UI.class);
        startActivity(mSenseUI);
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusConfig();
    }
}