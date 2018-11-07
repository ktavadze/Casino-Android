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

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";

    private Context mContext;
    private View mView;

    private Computer mComputer;
    private Human mHuman;
    private Round mRound;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        mComputer = ((MainActivity)mContext).mTournament.getComputer();
        mHuman = ((MainActivity)mContext).mTournament.getHuman();
        mRound = ((MainActivity)mContext).mTournament.getRound();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_game, null);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Game");

        Log.d(TAG, "onStart: \n" + mRound.stringify(mComputer, mHuman));
    }
}
