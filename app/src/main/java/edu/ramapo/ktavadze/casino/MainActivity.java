package edu.ramapo.ktavadze.casino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Deck deck = new Deck();

        Human human = new Human();
        human.setHand(deck.drawSet());

        Computer computer = new Computer();
        computer.setHand(deck.drawSet());

        Set looseSet = deck.drawSet();
        ArrayList<Build> builds = new ArrayList<>();

        Table table = new Table(looseSet, builds);

        Log.d(TAG, "Table: " + table.stringify());

        Round round = new Round(1, table, deck);

        Log.d(TAG, "Round: " + round.stringify(computer, human));
    }
}
