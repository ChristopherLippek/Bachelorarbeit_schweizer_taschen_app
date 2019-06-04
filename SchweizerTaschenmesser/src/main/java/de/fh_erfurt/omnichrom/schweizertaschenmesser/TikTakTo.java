package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TikTakTo extends AppCompatActivity
{
    //Zustand welcher Spieler dran ist
    int counter = 0;
    //Belegung der Felder 0 = 1.Spieler , 1 = 2. Spieler und 2 = kein Spieler Feld ist leer
    int [] gameState = {2,2,2,2,2,2,2,2,2};
    //Bedingungen durch die man Spiel gewinnen kann
    int [][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    boolean isGameActive = true;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktakto);
        button = (Button) findViewById(R.id.playAgainButton);
        textView = (TextView) findViewById(R.id.winnerTextView);
    }

    //ImageView on the left top
    public void playerClick00(View view)
    {

        ImageView imageView00 = (ImageView) view;
        imageView00.setTranslationX(2000);
        //wandelt das Tag des bildes in ein int um -> tags = 0,...,8
        int tappedCounter = Integer.parseInt(imageView00.getTag().toString());
        //gameState bekommt einen Wert 0 = Spieler 1 und 1 ist Spieler 2
        gameState[tappedCounter] = counter;

        //Geht nur wenn die Variable true ist
        if (isGameActive)
        {
            //gucke ob das ImageView ein Bild anzeigt oder leer ist
            if(imageView00.getDrawable() == null && isGameActive)
            {
                //Spieler 1
                if(counter == 0)
                {
                    //setze ein Kreis in das vorher leer ImageView
                    imageView00.setImageResource(R.drawable.circle);
                    //Spieler 2 ist dran
                    counter++;
                }
                //Spieler 2
                else if (counter == 1)
                {
                    //setze ein Kreuz in das vorher leer ImageView
                    imageView00.setImageResource(R.drawable.cross);
                    //Spieler 1 ist dran
                    counter--;
                }
            }

            //Bild soll von rechts eingeflogen kommen
            imageView00.animate().translationXBy(-2000).setDuration(500);

            //guckt ob eine Siegesbedingung erfüllt ist
            for(int[] winningPosition: winningPositions)
            {
                if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2)
                {

                    if(counter == 1)
                    {
                        //spieler 1 hat gewonnen
                        isGameActive = false;
                        Button button = (Button) findViewById(R.id.playAgainButton);
                        button.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) findViewById(R.id.winnerTextView);
                        textView.setText("Spieler 1 hat Gewonnen");
                        textView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        //spieler 2 hat gewonnen
                        isGameActive = false;
                        Button button = (Button) findViewById(R.id.playAgainButton);
                        button.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) findViewById(R.id.winnerTextView);
                        textView.setText("Spieler 2 hat Gewonnen");
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    //unentschieden
                    isGameActive = false;
                    //guckt ob es noch leere Felder gibt wenn ja setzt es isGameActive wieder auf true
                    //damit das Spiel weiter gehen kann
                    for (int counterState: gameState)
                    {
                        if(counterState == 2) isGameActive = true;
                    }
                    //Wenn es kein leeres Feld mehr gibt ist isGameActive = false
                    //Weil es in der Schleife nicht auf true gesetzt wurde -> kein leeres Feld mehr
                    if(!isGameActive)
                    {
                        button.setVisibility(View.VISIBLE);
                        textView.setText("Unentschieden");
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void playAgain(View view)
    {
        //schaltet Button auf unsichtbar
        button.setVisibility(View.INVISIBLE);
        //schaltet Textview auf Unsichtbar
        textView.setVisibility(View.INVISIBLE);

        //erstelle Variable für das Layout damit wir auf die Kinder (Bilder) zugreifen können
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        //laufe alle durch gridLayout.getChildCount() gibt die Anzahl der Elemente innerhalb des Layouts zurück
        //da i bei 0 beginnt muss man < Anzahl
        for (int i = 0; i<gridLayout.getChildCount(); i++)
        {
            //hole ein Element (i = tag des Bildes)
            ImageView imageView00 = (ImageView) gridLayout.getChildAt(i);
            //setze das Bild auf null
            imageView00.setImageDrawable(null);
        }

        //setze gameState auf 2 zurück -> 2 = Kein Spieler hat das Feld belegt
        for (int i = 0; i< gameState.length; i++)
        {
            gameState[i] = 2;
        }

        //setze isGameActive auf True -> sobald Spiel beendet ist wird die Variable false
        //damit an nicht weiter spielen kann
        isGameActive = true;
        //0 = Spieler 1, 1 = Spieler 2, 2 = kein Spieler hat Feld belegt
        counter = 0;
    }
}