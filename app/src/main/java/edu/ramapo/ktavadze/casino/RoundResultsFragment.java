package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class RoundResultsFragment extends Fragment {
    private static final String TAG = "RoundResults";

    private Context mContext;
    private View mView;

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    private TextView mTextRoundComputerPile;
    private TextView mTextRoundComputerSpades;
    private TextView mTextRoundComputerEarned;
    private TextView mTextRoundComputerTotal;
    private TextView mTextRoundHumanPile;
    private TextView mTextRoundHumanSpades;
    private TextView mTextRoundHumanEarned;
    private TextView mTextRoundHumanTotal;
    private Button mButtonRoundContinue;

    private CardsRecyclerAdapter mComputerPileAdapter;
    private CardsRecyclerAdapter mHumanPileAdapter;

    public RoundResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        mTournament = ((MainActivity)mContext).mTournament;

        if (mTournament == null) {
            ((MainActivity)mContext).loadFragment(new MainMenuFragment());
        }
        else {
            mComputer = mTournament.getComputer();
            mHuman = mTournament.getHuman();
            mRound = mTournament.getRound();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_round_results, null);

        mTextRoundComputerPile = mView.findViewById(R.id.text_round_computer_pile);
        mTextRoundComputerSpades = mView.findViewById(R.id.text_round_computer_spades);
        mTextRoundComputerEarned = mView.findViewById(R.id.text_round_computer_earned);
        mTextRoundComputerTotal = mView.findViewById(R.id.text_round_computer_total);
        mTextRoundHumanPile = mView.findViewById(R.id.text_round_human_pile);
        mTextRoundHumanSpades = mView.findViewById(R.id.text_round_human_spades);
        mTextRoundHumanEarned = mView.findViewById(R.id.text_round_human_earned);
        mTextRoundHumanTotal = mView.findViewById(R.id.text_round_human_total);
        mButtonRoundContinue = mView.findViewById(R.id.button_round_continue);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Round Results");

        addListener();

        initView();

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }

    private void addListener() {
        // Add continue listener
        mButtonRoundContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTournament.isOver()) {
                    ((MainActivity)mContext).loadFragment(new TournamentResultFragment());
                }
                else {
                    mTournament.update();

                    ((MainActivity)mContext).loadFragment(new GameFragment());
                }
            }
        });

        Log.d(TAG, "addListener: Listener added");
    }

    private void initView() {
        // Init computer pile recycler
        final RecyclerView recyclerRoundComputerPile = mView.findViewById(R.id.recycler_round_computer_pile);
        mComputerPileAdapter = new CardsRecyclerAdapter(mContext, mComputer.getPile().getCards());
        recyclerRoundComputerPile.setAdapter(mComputerPileAdapter);
        recyclerRoundComputerPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init human pile recycler
        final RecyclerView recyclerRoundHumanPile = mView.findViewById(R.id.recycler_round_human_pile);
        mHumanPileAdapter = new CardsRecyclerAdapter(mContext, mHuman.getPile().getCards());
        recyclerRoundHumanPile.setAdapter(mHumanPileAdapter);
        recyclerRoundHumanPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        HashMap<String, Integer> results = mRound.getResults();

        // Display results
        mTextRoundComputerPile.setText(String.valueOf(results.get("computerPile").toString()));
        mTextRoundComputerSpades.setText(String.valueOf(results.get("computerSpades").toString()));
        mTextRoundComputerEarned.setText(String.valueOf(results.get("computerScore").toString()));
        mTextRoundComputerTotal.setText(String.valueOf(mComputer.getScore()));
        mTextRoundHumanPile.setText(String.valueOf(results.get("humanPile").toString()));
        mTextRoundHumanSpades.setText(String.valueOf(results.get("humanSpades").toString()));
        mTextRoundHumanEarned.setText(String.valueOf(results.get("humanScore").toString()));
        mTextRoundHumanTotal.setText(String.valueOf(mHuman.getScore()));
    }
}
