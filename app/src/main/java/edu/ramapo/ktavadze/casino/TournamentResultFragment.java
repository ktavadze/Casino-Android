package edu.ramapo.ktavadze.casino;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TournamentResultFragment Class to implement the tournament result view.
 */

public class TournamentResultFragment extends Fragment {
    private static final String TAG = "TournamentResult";

    private Context mContext;
    private View mView;

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    private TextView mTextTournamentComputerTotal;
    private TextView mTextTournamentResult;
    private TextView mTextTournamentHumanTotal;

    public TournamentResultFragment() {
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

        mView = inflater.inflate(R.layout.fragment_tournament_result, null);

        mTextTournamentComputerTotal = mView.findViewById(R.id.text_tournament_computer_total);
        mTextTournamentResult = mView.findViewById(R.id.text_tournament_result);
        mTextTournamentHumanTotal = mView.findViewById(R.id.text_tournament_human_total);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Tournament Result");

        setHasOptionsMenu(true);

        initView();
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
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());

                return true;
            default:
                // Invoke superclass to handle unrecognized action.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     Initializes the view with essential UI components.
     */
    private void initView() {
        // Display result
        mTextTournamentComputerTotal.setText(String.valueOf(mComputer.getScore()));

        if (mComputer.getScore() > mHuman.getScore()) {
            mTextTournamentResult.setText("Computer wins!");
        }
        else if (mComputer.getScore() < mHuman.getScore()) {
            mTextTournamentResult.setText("Human wins!");
        }
        else {
            mTextTournamentResult.setText("It's a tie!");
        }

        mTextTournamentHumanTotal.setText(String.valueOf(mHuman.getScore()));

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }
}
