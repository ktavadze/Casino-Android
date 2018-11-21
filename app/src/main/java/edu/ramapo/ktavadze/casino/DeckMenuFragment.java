package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DeckMenuFragment extends Fragment {
    private static final String TAG = "DeckMenuFragment";

    private Context mContext;

    private Button mButtonMenuNewDeck;
    private Button mButtonMenuSeedDeck;

    public DeckMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_deck_menu, null);

        mButtonMenuNewDeck = view.findViewById(R.id.button_menu_new_deck);
        mButtonMenuSeedDeck = view.findViewById(R.id.button_menu_seed_deck);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Deck Menu");

        addListeners();
    }

    private void addListeners() {
        // Add new deck listener
        mButtonMenuNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new RoundFragment());
            }
        });

        // Add seed deck listener
        mButtonMenuSeedDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seedDeck();

                ((MainActivity)mContext).loadFragment(new RoundFragment());
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    /**********************************************************************
     Function Name: seedDeck
     Purpose: To seed the deck
     **********************************************************************/
    private void seedDeck() {
        try {
            InputStream inFile = mContext.getAssets().open("deck.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));

            ArrayList<Card> cards = new ArrayList<>();

            String line = reader.readLine();

            while (line != null) {
                cards.add(new Card(line));

                line = reader.readLine();
            }

            reader.close();

            ((MainActivity)mContext).mTournament.getRound().setDeck(new Deck(cards));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
