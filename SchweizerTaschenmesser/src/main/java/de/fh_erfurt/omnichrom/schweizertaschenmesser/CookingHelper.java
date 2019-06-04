package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The user can choose a cooking time by the seekbar or with the dropdown menu
 * after he press the start button, the button turns into a stopping button
 * and the countdown starts
 * when the timer is finish a sound is played
 * written by Christopher Lippek
 */
public class CookingHelper extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    /**
     * mediaPlayer -> for playing the sound when the timer is finish
     * timeTextView -> TextView to show the left time in the frontend
     * dropdown -> dropdown list for the food
     * items -> the food in the dropdown list
     * whatCookingTime -> time that is chosen by selecting a food from the dropdown list
     * rotate -> bool variable to change the direction from the animate rotation
     * oven -> shows the celsius of the oven for a specific food in the frontend
     * selfoven -> shows the celsius of the selfoven for a specific food in the frontend
     * time -> value for the seekbar value
     * counterIsActive -> bool that decides over starting the timer or stopping the timer
     * goButton -> Button to start the timer
     * timer -> seekbar if the user whats to choose his time at this way
     * countDownTimer -> timer for the function
     * imageView is the background image
     */
    public MediaPlayer mediaPlayer;
    public TextView timeTextView;
    private Spinner dropdown;
    private String[] items;
    public String whatCookingTime;
    private boolean rotate;
    private TextView oven;
    private TextView selfoven;
    public int time;
    boolean counterIsActive = false;
    Button goButton;
    SeekBar timer;
    CountDownTimer countDownTimer;
    public ImageView imageView;

    /**
     * starts the timer
     * 1. set every unused element to INVISIBLE and the button text at Stopp
     * 2. start the background animation
     * 3. update the time every second
     * finish: reset the timer
     * @param view -> button from frontend
     */
    public void startEggTimer(View view)
    {
        dropdown.setVisibility(View.INVISIBLE);
        ChangerEditTextTimer();

        //if the timer is still running this function stops the timer
        if(counterIsActive)
        {
            ResetTimer();
        }


        //timer starts if the time is set
        else if(!whatCookingTime.isEmpty())
        {
            counterIsActive = true;
            timer.setEnabled(false);
            goButton.setText("STOP!");

            //timer for the background animation and counting the seconds
            countDownTimer = new CountDownTimer(time * 1000, 1000)
            {
                public void onTick(long l)
                {
                    //rotation animation
                    UpdateTimer((int) l / 1000);
                    if(rotate)
                    {
                        imageView.animate().rotation(20).setDuration(10);
                        rotate = false;
                    }
                    else
                    {
                        imageView.animate().rotation(-20).setDuration(10);
                        rotate = true;
                    }
                }
                //if the timer is finish its triggers a sound and reset the timer
                public void onFinish()
                {
                    mediaPlayer.start();
                    ResetTimer();
                }
            }.start();
        }

        else
        {
            counterIsActive = true;
            timer.setEnabled(false);
            goButton.setText("STOP!");
            countDownTimer = new CountDownTimer(time * 1000, 1000)
            {
                public void onTick(long l)
                {
                    UpdateTimer((int) l / 1000);
                    if(rotate)
                    {
                        imageView.animate().rotation(20).setDuration(10);
                        rotate = false;
                    }
                    else
                    {
                        imageView.animate().rotation(-20).setDuration(10);
                        rotate = true;
                    }

                }

                public void onFinish()
                {
                    mediaPlayer.start();
                    ResetTimer();
                    imageView.animate().rotation(0);
                    rotate = true;
                }
            }.start();
        }
    }

    /**
     * convert the seconds in a for humans easier to read time
     * @param secondsLeft -> seconds that left until the timer is finish
     */
    public void UpdateTimer(int secondsLeft)
    {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes*60;

        String secondString = Integer.toString(seconds);
        if(seconds <= 9)
        {
            secondString = "0" + secondString;
        }

        timeTextView.setText(Integer.toString(minutes) + " : " + secondString);
    }

    /**
     * resets all values
     * and set their value on the standard mode
     */
    public void ResetTimer()
    {
        timeTextView.setText("0:30");
        timer.setEnabled(true);
        timer.setProgress(30);
        counterIsActive = false;
        countDownTimer.cancel();
        goButton.setText("GO!");
        imageView.animate().rotation(0);
        rotate = true;
        whatCookingTime = "";
        oven.setText("");
        selfoven.setText("");
        dropdown.setVisibility(View.VISIBLE);
    }

    /**
     * initialise the variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        oven = findViewById(R.id.ovenTextView);
        selfoven = findViewById(R.id.selfOvenTextView);
        timer = (SeekBar) findViewById(R.id.timeSeekBar);
        mediaPlayer = MediaPlayer.create(this, R.raw.doorbell);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        whatCookingTime = "";

        dropdown = (Spinner) findViewById(R.id.spinner1);
        dropdown.setOnItemSelectedListener(this);
        items = new String[]{"", "pizza", "chicken nuggets", "mozzarella sticks", "fischstäbchen", "chili cheese nuggets"
        , "chicken wings", "pommes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        rotate = true;
        imageView = (ImageView) findViewById(R.id.imageView);
        goButton = (Button) findViewById(R.id.goButton);

        //seekbar to chose the time
        timer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                UpdateTimer(progress);
                time = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    /**
     * set time and Textviews based on the selected value from the dropdown menu
     */
    public void ChangerEditTextTimer()
    {
        if (whatCookingTime.equals("pizza"))
        {
            //12 minutes are 60 seconds multiplied with 12
            time = 60*12;
            oven.setText("Backofen: 220 Grad Celsius");
            selfoven.setText("Umluftofen: 180 Grad Celsius");
        }
        else if (whatCookingTime.equals("chicken nuggets"))
        {
            time = 60*15;
            oven.setText("Backofen: 200 Grad Celsius");
            selfoven.setText("Umluftofen: 160 Grad Celsius");
        }
        else if (whatCookingTime.equals("mozzarella sticks"))
        {
            time = 60*5;
            oven.setText("Backofen: 220 Grad Celsius");
            selfoven.setText("Umluftofen: 180 Grad Celsius");
        }
        else if (whatCookingTime.equals("fischstäbchen"))
        {
            time = 60*15;
            oven.setText("Backofen: 220 Grad Celsius");
            selfoven.setText("");
            Toast.makeText(this, "Nach 10 Minuten bitte wenden", Toast.LENGTH_SHORT).show();
        }
        else if (whatCookingTime.equals("chili cheese nuggets"))
        {
            time = 60*9;
            oven.setText("Backofen: 220 Grad Celsius");
            selfoven.setText("Umluftofen: 180 Grad Celsius");
        }
        else if(whatCookingTime.equals("chicken wings"))
        {
            time = 60*16;
            oven.setText("Backofen: 220 Grad Celsius");
            selfoven.setText("Umluftofen: 200 Grad Celsius");
        }
        else if(whatCookingTime.equals("pommes"))
        {
            time = 60*18;
            oven.setText("Backofen: 200 Grad Celsius");
            selfoven.setText("Umluftofen: 180 Grad Celsius");
        }
        else
        {

        }
    }

    /**
     * set whatCookingTime on the item from the dropdown menu
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        whatCookingTime = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
