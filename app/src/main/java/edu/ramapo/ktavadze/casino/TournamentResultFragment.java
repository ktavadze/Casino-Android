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
import android.widget.TextView;

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
    private Button mButtonTournamentContinue;

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
        mButtonTournamentContinue = mView.findViewById(R.id.button_tournament_continue);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Tournament Result");

        addListener();

        initView();

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }

    private void addListener() {
        // Add continue listener
        mButtonTournamentContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());
            }
        });

        Log.d(TAG, "addListener: Listener added");
    }

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
    }
}
