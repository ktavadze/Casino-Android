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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Round Results");

        setHasOptionsMenu(true);

        initView();

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "Close Results")
                .setIcon(R.drawable.ic_quit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                if (mTournament.isOver()) {
                    ((MainActivity)mContext).loadFragment(new TournamentResultFragment());
                }
                else {
                    mTournament.update();

                    ((MainActivity)mContext).loadFragment(new RoundFragment());
                }

                return true;
            default:
                // Invoke superclass to handle unrecognized action.
                return super.onOptionsItemSelected(item);
        }
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
