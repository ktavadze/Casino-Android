package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

public class CoinTossFragment extends Fragment {
    private static final String TAG = "CoinTossFragment";

    private Context mContext;

    private Button mButtonGuessHeads;
    private Button mButtonGuessTails;

    public CoinTossFragment() {
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

        View view = inflater.inflate(R.layout.fragment_coin_toss, null);

        mButtonGuessHeads = view.findViewById(R.id.button_guess_heads);
        mButtonGuessTails = view.findViewById(R.id.button_guess_tails);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Coin Toss");

        addListeners();
    }

    private void addListeners() {
        // Add heads listener
        mButtonGuessHeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(0);
            }
        });

        // Add tails listener
        mButtonGuessTails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(1);
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void processGuess(int guess) {
        Random rand = new Random();

        final int coin = rand.nextInt(2);

        if (coin == guess) {
            final int colorGreen = ContextCompat.getColor(mContext, R.color.colorGreen);

            if (guess == 0) {
                mButtonGuessHeads.setBackgroundColor(colorGreen);
            }
            else {
                mButtonGuessTails.setBackgroundColor(colorGreen);
            }

            ((MainActivity)mContext).mTournament = new Tournament(true);
        }
        else {
            final int colorRed = ContextCompat.getColor(mContext, R.color.colorRed);

            if (guess == 0) {
                mButtonGuessHeads.setBackgroundColor(colorRed);
            }
            else {
                mButtonGuessTails.setBackgroundColor(colorRed);
            }

            ((MainActivity)mContext).mTournament = new Tournament(false);
        }

        // Overwrite heads listener
        mButtonGuessHeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).loadFragment(new SeedMenuFragment());
            }
        });

        // Overwrite tails listener
        mButtonGuessTails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).loadFragment(new SeedMenuFragment());
            }
        });
    }
}
