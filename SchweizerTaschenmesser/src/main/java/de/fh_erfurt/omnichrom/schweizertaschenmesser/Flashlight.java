package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

/**
 * logic for the flashlight activity
 * <br>
 * enables the camera control and can turn the flashlight on and off
 * <br>
 * Credits: On/Off Buttons: https://dryicons.com/vector/on-off-button-vectors
 * @author Daniel Contu
 */
public class Flashlight extends AppCompatActivity
{
    /** button to turn flashlight on and off */
    private ImageView buttonTorchOnOff;
    /** button to enable camera permission */
    private Button buttonEnableCamera;
    /** request camera permission number */
    private static final int CAMERA_REQUEST = 50;
    /** current state of flashlight */
    private boolean flashLightStatus = false;

    /**
     * set button click events, find flash and set state
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        // get views
        buttonTorchOnOff = (ImageView) findViewById(R.id.imageTorchSwitch);
        buttonEnableCamera = (Button) findViewById(R.id.button);

        // find flash
        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        // get user permission
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        // enable / disable buttons
        buttonEnableCamera.setEnabled(!isEnabled);
        buttonTorchOnOff.setEnabled(isEnabled);

        // enable camera listener
        buttonEnableCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Flashlight.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        });

        // torch switch listener
        buttonTorchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (flashLightStatus)
                        flashLightOff();
                    else
                        flashLightOn();
                } else {
                    Toast.makeText(Flashlight.this, "Keine Taschenlampe gefunden.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // current state
        if (isEnabled)
            buttonEnableCamera.setText("Kamera Licht erlaubt");
    }

    /**
     * turns the flashlight on
     */
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            buttonTorchOnOff.setImageResource(R.drawable.ic_on_button);
        } catch (CameraAccessException e) {
        }
    }

    /**
     * turns the flashlight off
     */
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            buttonTorchOnOff.setImageResource(R.drawable.ic_off_button);
        } catch (CameraAccessException e) {
        }
    }

    /**
     * gets permission request results and enables/disables the buttons
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_REQUEST :
                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonEnableCamera.setEnabled(false);
                    buttonEnableCamera.setText("Kamera Licht erlaubt");
                    buttonTorchOnOff.setEnabled(true);
                } else {
                    Toast.makeText(Flashlight.this, "Kamera Licht Erlaubnis entzogen", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
