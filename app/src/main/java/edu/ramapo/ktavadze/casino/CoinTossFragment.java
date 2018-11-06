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
import android.widget.TextView;

import java.util.Random;

public class CoinTossFragment extends Fragment {
    private static final String TAG = "CoinTossFragment";

    private Context mContext;

    private Button mButtonTossGuessHeads;
    private Button mButtonTossGuessTails;
    private TextView mTextTossResult;
    private Button mButtonTossContinue;

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

        mButtonTossGuessHeads = view.findViewById(R.id.button_toss_guess_heads);
        mButtonTossGuessTails = view.findViewById(R.id.button_toss_guess_tails);
        mTextTossResult = view.findViewById(R.id.text_toss_result);
        mButtonTossContinue = view.findViewById(R.id.button_toss_continue);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Coin Toss");

        addListeners();
    }

    private void addListeners() {
        // Add guess heads listener
        mButtonTossGuessHeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(0);
            }
        });

        // Add guess tails listener
        mButtonTossGuessTails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(1);
            }
        });

        // Add continue listener
        mButtonTossContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new SeedMenuFragment());
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void processGuess(int guess) {
        Random rand = new Random();

        int coin = rand.nextInt(2);

        // Hide guess buttons
        mButtonTossGuessHeads.setVisibility(View.GONE);
        mButtonTossGuessTails.setVisibility(View.GONE);

        // Show result
        mTextTossResult.setVisibility(View.VISIBLE);
        mButtonTossContinue.setVisibility(View.VISIBLE);

        String message = "";

        if (coin == 0) {
            message += "HEADS";
        }
        else {
            message += "TAILS";
        }

        mTextTossResult.setText(message);

        if (coin == guess) {
            final int colorGreen = ContextCompat.getColor(mContext, R.color.colorGreen);
            mTextTossResult.setBackgroundColor(colorGreen);

            ((MainActivity)mContext).mTournament = new Tournament(true);
        }
        else {
            final int colorRed = ContextCompat.getColor(mContext, R.color.colorRed);
            mTextTossResult.setBackgroundColor(colorRed);

            ((MainActivity)mContext).mTournament = new Tournament(false);
        }
    }
}
