package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

/**
 * logic for the spirit level activity
 * <br>
 * calculates and sets the positions of the bubbles
 * @author Daniel Contu
 */
public class SpiritLevel extends AppCompatActivity implements SensorEventListener
{
    /** debug mode - show/hide debug text views */
    private boolean DEBUG = false;
    /** Debug Text Views */
    private TextView xText, yText;
    /** bubble images for the spirit level */
    private ImageView xBubble, yBubble;
    /** boundaries for the bubbles */
    private Space xBubbleBound, yBubbleBound;
    /** sensor for data */
    private Sensor mySensor;
    /** sensor manager for sensor events */
    private SensorManager SM;

    /**
     * set params on activity creation
     * @inheritDoc
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spirit_level);

        // create sensor manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // register sensor listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign text views
        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);

        // Assign image views
        xBubble = (ImageView)findViewById(R.id.spiritLevelBubbleHorizontal);
        yBubble = (ImageView)findViewById(R.id.spiritLevelBubbleVertical);

        // Assign bubble boundaries
        xBubbleBound = (Space)findViewById(R.id.bubbleHorizontalBoundaries);
        yBubbleBound = (Space)findViewById(R.id.bubbleVerticalBoundaries);

        if (DEBUG) {
            xText.setText("");
            yText.setText("");
        }
    }

    /**
     * @exclude
     * not in use
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    /**
     * gets the sensor data, when smartphone moves
     * @param event data
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0]; // x value
        float y = event.values[1]; // y value
        float z = event.values[2]; // z value

        changeBubblePosition(x, y);
    }

    /**
     * calculates and changes the positions of the spirit level bubbles
     * @param x axis of sensor
     * @param y axis of sensor
     */
    protected void changeBubblePosition(float x, float y) {
        if (DEBUG) {
            xText.setText(new StringBuilder().append("X: ").append(x).toString());
            yText.setText(new StringBuilder().append("Y: ").append(y).toString());
        }

        // get actual position
        float xBubblePos = xBubble.getX();
        float yBubblePos = yBubble.getY();

        // get boundaries
        float xBubbleBounds[] = new float[2];
        float yBubbleBounds[] = new float[2];
        xBubbleBounds[0] = xBubbleBound.getX();
        xBubbleBounds[1] = xBubbleBound.getWidth();
        yBubbleBounds[0] = yBubbleBound.getY();
        yBubbleBounds[1] = yBubbleBound.getHeight();

        // calculate new position:
        // (left side of the boundary + half of the width/height) -> the middle of the boundary
        // half of the width/height * [-1,1] -> how far left/right the bubble is from the middle
        // - bubble width/2 -> set the middle of the bubble
        if (x < 0)
            xBubblePos = xBubbleBounds[0] + (xBubbleBounds[1]/2) + (xBubbleBounds[1]/2) * Math.max(x,-1) - ((float)xBubble.getWidth()/2);
        else
            xBubblePos = xBubbleBounds[0] + (xBubbleBounds[1]/2) + (xBubbleBounds[1]/2) * Math.min(x,1) - ((float)xBubble.getWidth()/2);

        if (y < 0)
            yBubblePos = yBubbleBounds[0] + (yBubbleBounds[1]/2) + (yBubbleBounds[1]/2) * Math.max(y,-1) * (-1) - ((float)yBubble.getHeight()/2);
        else
            yBubblePos = yBubbleBounds[0] + (yBubbleBounds[1]/2) + (yBubbleBounds[1]/2) * Math.min(y,1) * (-1) - ((float)yBubble.getHeight()/2);

        // set new position
        xBubble.setX(xBubblePos);
        yBubble.setY(yBubblePos);
    }
}
