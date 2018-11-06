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
    private View mView;

    private Button mButtonTossHeads;
    private Button mButtonTossTails;
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

        mView = inflater.inflate(R.layout.fragment_coin_toss, null);

        mButtonTossHeads = mView.findViewById(R.id.button_toss_heads);
        mButtonTossTails = mView.findViewById(R.id.button_toss_tails);
        mTextTossResult = mView.findViewById(R.id.text_toss_result);
        mButtonTossContinue = mView.findViewById(R.id.button_toss_continue);

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
        mButtonTossHeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(0);
            }
        });

        // Add tails listener
        mButtonTossTails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess(1);
            }
        });

        // Add continue listener
        mButtonTossContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getContext()).loadFragment(new CoinTossFragment());
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void processGuess(int guess) {
        Random rand = new Random();

        int coin = rand.nextInt(2);

        // Hide guess buttons
        mButtonTossHeads.setVisibility(View.GONE);
        mButtonTossTails.setVisibility(View.GONE);

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
        }
        else {
            final int colorRed = ContextCompat.getColor(mContext, R.color.colorRed);
            mTextTossResult.setBackgroundColor(colorRed);
        }
    }
}
