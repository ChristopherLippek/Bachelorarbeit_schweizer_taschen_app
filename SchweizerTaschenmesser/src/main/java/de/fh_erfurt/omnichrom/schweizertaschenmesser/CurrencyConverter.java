package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.util.Arrays.asList;

/**
 * the user can chose between a different currency
 * first the currency he has and second the currency he want
 * with a API Request he select the chosen datas from the internet
 * written by Christopher Lippek
 */
public class CurrencyConverter extends AppCompatActivity
{
    /**
     * source -> currency from convert
     * target -> currency  to convert
     * amount- > amount to convert
     * sourceTextView -> information from source
     * targetTextView -> information from target
     * amountFromAPI -> information from API Request (amount)
     * exchangeRateTextView ->value from API Request (exchangeRate)
     * task -> class for the API Request
     * amountEditText -> input from user (amount value)
     */
    String source;
    String target;
    String amount;
    TextView sourceTextView;
    TextView targetTextView;
    TextView amountFromAPI;
    TextView exchangeRateTextView;
    DownloadTask task;
    EditText amountEditText;

    /**
     * initialize every variable with a start value and loads the frontend
     * if you chose the source and the target variable, this function set the values for the Urls
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        sourceTextView = (TextView) findViewById(R.id.sourceTextView);
        targetTextView = (TextView) findViewById(R.id.targetTextView);
        amountEditText = (EditText) findViewById(R.id.editText);
        amountFromAPI = (TextView) findViewById(R.id.amountFromAPI);
        exchangeRateTextView = (TextView) findViewById(R.id.exchangeRateTextView);
        amount = "1";


        final String Currencies[] = {"EUR", "USD", "TND", "CHF", "BTC", "JPY", "RUB", "DKK", "LBP"};

        //1. initialization the ListView
        ListView sourceListView = findViewById(R.id.sourceListView);
        ListView targetListView = findViewById(R.id.targetListView);

        //2. creates a ArrayList for all possible currency
        final ArrayList<String> sourceList = new ArrayList<String>(asList("Euro", "US-Dollar", "Tunesischer Dinar", "Schweizer Franken", "Bit Coin", "Japanische Yen", "Russischer Rubel"
                ,"Dänische Krone", "Libanesische Pfund"));
        final ArrayList<String> targetList = new ArrayList<String>(asList("Euro", "US-Dollar", "Tunesischer Dinar", "Schweizer Franken", "Bit Coin", "Japanische Yen", "Russischer Rubel"
                ,"Dänische Krone", "Libanesische Pfund"));

        //3. creates a arrayAdapter for the lists in the frontend
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sourceList);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, targetList);

        //4. connect the list from the frontend with the adapter
        sourceListView.setAdapter(arrayAdapter);
        targetListView.setAdapter(arrayAdapter1);

        //5. creates a OnClickListener
        //reacts of the User input -> which currency is chosen by the user
        sourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //set the source value with the chosen currency from source List
                CurrencyConverter.this.source = Currencies[position];
                CurrencyConverter.this.sourceTextView.setText("Erster Wert: "+source);
            }
        });

        targetListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CurrencyConverter.this.target = Currencies[position];
                CurrencyConverter.this.targetTextView.setText("Zweiter Wert: "+target);
            }
        });
    }

    /**
     * When the User clicks the convert button this function sets the URL Strig +
     * user input (source, target and amount) and execute the API Request
     * @param view -> connect the Button from the frontend with this function
     */
    public void ClickedOnButton(View view)
    {
        //User input convert the EditText in a String value
        amount = amountEditText.getText().toString();
        if(source != null)
        {
            //String for the API Request
            String urlJSON = "https://api.wahrungsrechner.org/v1/quotes/"+ source +"/"+target+"/json?quantity="+amount+"&key=1688|E3WvB*_KKBT^qjjj59GX9CoCX~jx0kF5";
            try
            {
                //creates a new DownloadTask with the constructor
                task = new DownloadTask(amountFromAPI, exchangeRateTextView);
                //execute the API Request
                task.execute(urlJSON);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}

