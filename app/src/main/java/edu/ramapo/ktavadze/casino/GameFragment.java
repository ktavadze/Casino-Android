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
import android.widget.LinearLayout;

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";

    private Context mContext;
    private View mView;

    private LinearLayout mLinearStartMenu;
    private LinearLayout mLinearMoveMenu;
    private LinearLayout mLinearBuildMenu;
    private LinearLayout mLinearEndMenu;

    private Button mButtonGetHelp;
    private Button mButtonMakeMove;
    private Button mButtonBuildMove;
    private Button mButtonCaptureMove;
    private Button mButtonTrailMove;
    private Button mButtonIncreaseMove;
    private Button mButtonExtendMove;
    private Button mButtonCreateMove;
    private Button mButtonCancelMove;
    private Button mButtonSubmitMove;

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        mTournament = ((MainActivity)mContext).mTournament;
        mComputer = mTournament.getComputer();
        mHuman = mTournament.getHuman();
        mRound = mTournament.getRound();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_game, null);

        mLinearStartMenu = mView.findViewById(R.id.linear_start_menu);
        mLinearMoveMenu = mView.findViewById(R.id.linear_move_menu);
        mLinearBuildMenu = mView.findViewById(R.id.linear_build_menu);
        mLinearEndMenu = mView.findViewById(R.id.linear_end_menu);

        mButtonMakeMove = mView.findViewById(R.id.button_make_move);
        mButtonGetHelp = mView.findViewById(R.id.button_get_help);
        mButtonGetHelp = mView.findViewById(R.id.button_get_help);
        mButtonBuildMove = mView.findViewById(R.id.button_build_move);
        mButtonCaptureMove = mView.findViewById(R.id.button_capture_move);
        mButtonTrailMove = mView.findViewById(R.id.button_trail_move);
        mButtonIncreaseMove = mView.findViewById(R.id.button_increase_move);
        mButtonExtendMove = mView.findViewById(R.id.button_extend_move);
        mButtonCreateMove = mView.findViewById(R.id.button_create_move);
        mButtonCancelMove = mView.findViewById(R.id.button_cancel_move);
        mButtonSubmitMove = mView.findViewById(R.id.button_submit_move);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Game");

        addListeners();

        mRound.startTurn(mComputer, mHuman);

        Log.d(TAG, "ROUND: \n" + mRound.stringify(mComputer, mHuman));
    }

    private void addListeners() {
        // Add get help listener
        mButtonGetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        // Add make move listener
        mButtonMakeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearStartMenu.setVisibility(View.GONE);
                mLinearMoveMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add build move listener
        mButtonBuildMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearBuildMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add capture move listener
        mButtonCaptureMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add trail move listener
        mButtonTrailMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add increase move listener
        mButtonIncreaseMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add extend move listener
        mButtonExtendMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add create move listener
        mButtonCreateMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add cancel move listener
        mButtonCancelMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearEndMenu.setVisibility(View.GONE);
                mLinearStartMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add submit move listener
        mButtonSubmitMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearEndMenu.setVisibility(View.GONE);
                mLinearStartMenu.setVisibility(View.VISIBLE);
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }
}
