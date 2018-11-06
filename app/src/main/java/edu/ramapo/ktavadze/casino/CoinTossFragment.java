package edu.ramapo.ktavadze.casino;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CoinTossFragment extends Fragment {
    private static final String TAG = "CoinTossFragment";

    private View mView;

    private int mGuess;

    public CoinTossFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_coin_toss, null);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Coin Toss");

        addListeners();
    }

    private void addListeners() {
        // Add heads listener
        final Button buttonTossHeads = mView.findViewById(R.id.button_toss_heads);
        buttonTossHeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGuess = 1;
            }
        });

        // Add tails listener
        final Button buttonTossTails = mView.findViewById(R.id.button_toss_tails);
        buttonTossTails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGuess = 2;
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void processGuess() {
        //
    }
}
