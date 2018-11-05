package edu.ramapo.ktavadze.casino;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    private boolean loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment != null && !currentFragment.getClass().equals(fragment.getClass())) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
