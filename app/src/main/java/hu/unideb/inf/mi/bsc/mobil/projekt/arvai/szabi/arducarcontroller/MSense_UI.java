package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MSense_UI extends AppCompatActivity implements  SensorEventListener{

    private ImageView directionImage;
    private Button modeChangerButton;
    private String welcome = "Üdvözöllek az mSENSE kezelőfelületen! \n \n" +
            "Az autó mozgatásához egyszerűen döntsd a telefont a megfelelő irányba. Az éppen aktuális utasítás a képernyő közepén jelenik meg.Szánj egy pár percet a gyakorlásra is, aztán az 'Indítás' gombra koppiva élesítheted a felületet. Ha ki szeretnéd kapcsolni az érzékelést, csak kappints a 'Leállítás' gombra! A telefont tartsd fekvő helyzetben a kamera bal oldalon legyen!";
    private String badOrientation = "Kezelőfelület letiltva, tartsd a telefont a megfelelő helyzetben, majd aktiváld újra a vezérlést!";
    private boolean isControllable = false;
    private boolean isNotified = false;
    private float axisX, axisY, axisZ;
    private SensorManager myManager;
    private Sensor acceleroSensor;
    private Display phoneDisplay;
    private SharedPreferences sharedPreferences;
    private String sharedPrefName = "apppref";

    private byte directive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msense_ui);

        directionImage = (ImageView) findViewById(R.id.directionImage);
        modeChangerButton = (Button) findViewById(R.id.modeControl);
        modeChangerButton.setText("Indítás");

        sharedPreferences = getSharedPreferences(sharedPrefName, MODE_PRIVATE);

        phoneDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        myManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acceleroSensor = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myManager.registerListener(this,acceleroSensor,SensorManager.SENSOR_DELAY_NORMAL);


        modeChangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isControllable)
                {
                    if(directive == 'S')
                    {
                        isControllable = true;
                        isNotified = false;
                        modeChangerButton.setText("Leállítás");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Biztonsági okokból csak STOP helyzetben válthatsz aktív vezérlésre!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    instantSTOP();
                    isControllable = false;
                    modeChangerButton.setText("Indítás");
                }
            }
        });

        if (savedInstanceState == null)
        {
            Dialog(welcome);
        }
        else
        {
            isControllable = savedInstanceState.getBoolean("BOOL");
        }

    }

    private void Dialog(String message) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(MSense_UI.this);
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
    public void onSensorChanged(SensorEvent event) {
        axisX = event.values[0];
        axisY = event.values[1];
        axisZ = event.values[2];
        setDirectives();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void setDirectives() {

        if(phoneDisplay.getRotation() == Surface.ROTATION_90) //állítva van
        {
            isNotified = false;
            if(axisZ > - 0.5 & axisZ < 5.0)
            {
                directionImage.setImageResource(R.mipmap.stop_icon_foreground);
                directive = 'S';
            }
            if(axisZ > 6.0)
            {
                directionImage.setImageResource(R.mipmap.forward_arrow_icon_foreground);
                directive = 'F';
            }//előremegy
            if(axisZ < - 1.0)
            {
                directionImage.setImageResource(R.mipmap.backward_arrow_icon_foreground);
                directive = 'B';
            }//hátramegy
            if(axisY < -4.0)
            {
                directionImage.setImageResource(R.mipmap.left_360_arrow_icon_foreground);
                directive = 'L';
            }//balra megy
            else if(axisY > 4.0)
            {
                directionImage.setImageResource(R.mipmap.right_360_arrow_icon_foreground);
                directive = 'R';
            }//jobbra megy
            if(isControllable)
                ConnectionData.getInstance().myBtService.commandToArduCar(getApplicationContext(),directive);
        }
        else //Nem megfelelő orientációban letilt az applikáció, stop jel az autónak.
        {
            instantSTOP();
            if(!isNotified & isControllable)
            {
                Toast.makeText(getApplicationContext(), badOrientation.toString(), Toast.LENGTH_SHORT).show();
                isNotified = true;
            }
            isControllable = false;
            modeChangerButton.setText("Indítás");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myManager.unregisterListener(this,acceleroSensor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myManager.unregisterListener(this,acceleroSensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myManager.registerListener(this,acceleroSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("BOOL", isControllable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        instantSTOP();
    }

    private void instantSTOP()
    {
        directive = 'S';
        ConnectionData.getInstance().myBtService.commandToArduCar(MSense_UI.this,directive);
    }
}