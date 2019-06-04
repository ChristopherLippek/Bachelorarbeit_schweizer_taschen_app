package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class theethHelper extends AppCompatActivity
{

    private ImageView theethImage;

    private TextView timeTextView;
    private TextView info;

    private long time;

    private CountDownTimer countDownTimer;

    private Button button;

    private boolean timerIsStarted;


    public void getStarted(View view)
    {
        if(timerIsStarted)
        {
            ResetTimer();
        }

        else
        {
            button.setText("STOPP");
            countDownTimer = new CountDownTimer(180000, 1000)
            {
                public void onTick(long l)
                {
                    time = l/1000;
                    UpdateTimer((int) time);
                    timerIsStarted = true;
                    Log.i("Sekunden übrig", String.valueOf(time));

                    if(time == 179)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth3);
                        info.setText("Zuerst die Kaufläche des Unterkiefers");
                    }

                    if(time == 150)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth4);
                        info.setText("Nun die Kaufläche des Oberkiefers");
                    }

                    if(time == 120)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth1);
                        info.setText("Nun die Aussenfläche des Unterkiefers");
                    }

                    if(time == 90)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth2);
                        info.setText("Jetzt die Aussenfläche des Oberkiefers");
                    }

                    if(time == 60)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth5);
                        info.setText("gleich geschafft. Jetzt die Innenfläche des Unterkiefers");
                    }
                    if(time == 30)
                    {
                        theethImage.setBackgroundResource(R.drawable.teeth5);
                        info.setText("Jetzt nurnoch die Innenfläche des Oberkiefers");
                    }
                }
                public void onFinish()
                {
                    ResetTimer();
                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theethhelper);
        theethImage = (ImageView) findViewById(R.id.imageView2);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        button = (Button) findViewById(R.id.button3);
        timerIsStarted = false;
        info = (TextView) findViewById(R.id.textView8);
    }

    public void ResetTimer()
    {
        timeTextView.setText("3:00");
        countDownTimer.cancel();
        theethImage.setBackgroundResource(R.drawable.teeth3);
        time = 0;
        button.setText("LOS");
        timerIsStarted = false;
        info.setText("");
    }

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
}
