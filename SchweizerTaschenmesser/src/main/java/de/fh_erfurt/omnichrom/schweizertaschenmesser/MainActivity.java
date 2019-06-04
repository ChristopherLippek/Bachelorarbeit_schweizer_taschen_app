package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity
{

    private ImageView imageView_Compass;
    private ImageView imageView_CurrencyConverter;
    private ImageView imageView_CookingHelper;
    private ImageView imageView_theethHelper;
    private ImageView imageView_TikTakTo;

    private EditText cityEditText;

    private TextView temperatur;
    private TextView feeledTemp;

    private ImageView condition;

    private WeatherInfos task;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageView_Compass = (ImageView) findViewById(R.id.compassImageView);
        imageView_theethHelper = (ImageView) findViewById(R.id.imageView8);
        imageView_CurrencyConverter = (ImageView) findViewById(R.id.currencyConverterImageView);
        imageView_CookingHelper = (ImageView) findViewById(R.id.cookingHelperImageView);
        imageView_TikTakTo = (ImageView) findViewById(R.id.tikTakToImageView);

        cityEditText = (EditText) findViewById(R.id.editText2);

        temperatur = (TextView) findViewById(R.id.textView);
        feeledTemp = (TextView) findViewById(R.id.textView6);

        condition = (ImageView) findViewById(R.id.imageView3);



        //set a Onclick-Event

        imageView_Compass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCompass();
            }
        });

        imageView_CurrencyConverter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            { openCurrencyConverter(); }
        });

        imageView_CookingHelper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCookingHelper();
            }
        });

        imageView_theethHelper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                opentheethHelper();
            }
        });

        imageView_TikTakTo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openTikTakTo();
            }
        });

    }

    public void getWeatherButton(View view)
    {
        String cityInfo = cityEditText.getText().toString();
        if(!cityEditText.equals(""))
        {
            Log.i("City: ", cityInfo);
            String urlJSON = "https://api.apixu.com/v1/current.json?key=60d1e164a65b427790d121838193005&q=" + cityInfo;
            try
            {
                task = new WeatherInfos(temperatur, condition, feeledTemp);
                task.execute(urlJSON);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            //Fehler
        }

    }

    public void openCompass()
    {
        Intent intent_Compass = new Intent(this, Compass.class);
        startActivity(intent_Compass);
    }


    public void openCurrencyConverter()
    {
        Intent intent = new Intent(this, CurrencyConverter.class);
        startActivity(intent);
    }


    public void openCookingHelper()
    {
        Intent intent = new Intent(this, CookingHelper.class);
        startActivity(intent);
    }

    public void opentheethHelper()
    {
        Intent intent = new Intent(this, theethHelper.class);
        startActivity(intent);
    }

    public void openTikTakTo()
    {
        Intent intent = new Intent(this, TikTakTo.class);
        startActivity(intent);
    }
    //---------------------------------------------wetter--------------------------------//

    public class WeatherInfos extends AsyncTask<String, Void, String>
    {

        public String current;
        public String temp_c;
        public String condition;
        public String textWeather;
        public String feelingLike;

        private TextView currentTemp;
        private TextView feeled;

        private ImageView conditionImageView;

        private Context c;


        public WeatherInfos(TextView field,ImageView exchange, TextView feeling)
        {
            this.currentTemp = field;
            this.conditionImageView = exchange;
            this.feeled = feeling;
        }


        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while(data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }

            catch(Exception e)
            {
                e.printStackTrace();
                Log.i("Fehler", "Fehler ist aufgetreten");
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            try
            {

                JSONObject jsonObject = new JSONObject(s);
                current = jsonObject.getString("current");
                Log.i("current: ", current);


                JSONObject sourceInfo = new JSONObject(current);
                temp_c = sourceInfo.getString("temp_c");
                Log.i("amount: ",temp_c);

                JSONObject valueInfo = new JSONObject(current);
                condition = valueInfo.getString("condition");
                Log.i("exchangeRate: ",condition);

                JSONObject textInfo = new JSONObject(condition);
                textWeather = textInfo.getString("text");

                JSONObject feelingObject = new JSONObject(current);
                feelingLike = feelingObject.getString("feelslike_c");

                this.currentTemp.setText("Temperatur " + temp_c + " °C");
                feeled.setText("Gefuehlte Tempertur "+feelingLike + " °C");
                textWeather = textWeather.toLowerCase();

                Log.i("CONDITION TEXTWEATHER",textWeather);

                textWeather = textWeather.replace(" ","");

                Log.i("CONDITION AFTER TRIM ",textWeather);

                if(!textWeather.equals(""))
                {
                    Context c = getApplicationContext();
                    int id = c.getResources().getIdentifier("drawable/"+textWeather, null, c.getPackageName());
                    conditionImageView.setImageResource(id);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
}
}



