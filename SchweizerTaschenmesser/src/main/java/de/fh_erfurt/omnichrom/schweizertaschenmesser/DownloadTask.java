package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for API requests and extract specific information
 * written by Christopher Lippek
 */
public class DownloadTask extends AsyncTask<String, Void, String>
{
    /**
     * kursInfo -> saves the information from result
     * info -> hold the information from result->amount string
     * exchangeRateInfo -> has the information from result->exchangerate
     */
    public String kursInfo;
    public String info;
    public String exchangeRateInfo;

    /**
     * amountAPI -> TextView for the info value
     * exchangeRate -> TextView for the exchangeRateInfo value
     */
    private TextView amountAPI;
    private TextView exchangeRate;

    /**
     * Constructor that sets the 2 Textview
     * we can show the information from this class in other classes
     * @param field -> TextViews from another class for amount information
     * @param exchange -> TextView from another class for exchangeRate value
     */
    public DownloadTask(TextView field, TextView exchange)
    {
        this.amountAPI = field;
        this.exchangeRate = exchange;
    }

    /**
     * this function makes a Request to the url, to get all information
     * after the connection was successful,
     * this function writes letter for letter in result and returns result
     * if the connection fails the function returns null with a Log information
     * @param urls -> path/s to the API
     * @return -> the whole information from the API Request
     */
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

    /**
     *extract specific information from the result and set the TextView for other classes
     * kursInfo -> holds the information under the keyword result
     * info -> get the value under the keyword amount
     * exchangeRate -> information with the keyword value
     * @param s -> Information from the API Request
     */
    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        try
        {
            //return the file without status
            JSONObject jsonObject = new JSONObject(s);
            kursInfo = jsonObject.getString("result");
            Log.i("KursInfo: ", kursInfo);

            //specific information with the keyword amount
            JSONObject sourceInfo = new JSONObject(kursInfo);
            info = sourceInfo.getString("amount");
            Log.i("amount: ",info);

            JSONObject valueInfo = new JSONObject(kursInfo);
            exchangeRateInfo = valueInfo.getString("value");
            Log.i("exchangeRate: ",exchangeRateInfo);

            this.amountAPI.setText("Umgerechneter Wert: "+info);
            this.exchangeRate.setText("Umrechnungskurs: "+exchangeRateInfo);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
