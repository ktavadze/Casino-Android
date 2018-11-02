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

        Log.d(TAG, "Deck: " + deck.stringify());

        Card c3 = new Card("C3");
        Card ha = new Card("HA");
        Card s2 = new Card("S2");
        Card dx = new Card("DX");

        Card c2 = new Card("C2");
        Card s8 = new Card("S8");

        Card h4 = new Card("H4");
        Card d3 = new Card("D3");

        Set looseSet = new Set();
        looseSet.addCard(c3);
        looseSet.addCard(ha);
        looseSet.addCard(s2);
        looseSet.addCard(dx);

        Set firstSet = new Set();
        firstSet.addCard(c2);
        firstSet.addCard(s8);

        Set secondSet = new Set();
        secondSet.addCard(h4);
        secondSet.addCard(d3);

        Build firstBuild = new Build(false, firstSet);
        Build secondBuild = new Build(true, secondSet);

        ArrayList<Build> builds = new ArrayList<>();
        builds.add(firstBuild);
        builds.add(secondBuild);

        Table table = new Table(looseSet, builds);

        Log.d(TAG, "Table: " + table.stringify());
    }
}
