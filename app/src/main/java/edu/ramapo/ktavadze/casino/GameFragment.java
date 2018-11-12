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
import android.widget.LinearLayout;

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";

    private Context mContext;
    private View mView;

    private LinearLayout mLinearStartMenu;
    private LinearLayout mLinearMoveMenu;
    private LinearLayout mLinearBuildMenu;
    private LinearLayout mLinearEndMenu;

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    private Move mMove;

    public GameFragment() {
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

            mMove = new Move();
        }
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

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Game");

        addListeners();

        mRound.startTurn(mComputer, mHuman);

        Log.d(TAG, "ROUND: \n" + mRound.stringify(mComputer, mHuman));

        initRecyclers();
    }

    private void initRecyclers() {
        // Init deck recycler
        final RecyclerView recyclerDeck = mView.findViewById(R.id.recycler_deck);
        recyclerDeck.setAdapter(new CardsRecyclerAdapter(mContext, mRound.getDeck().getCards()));
        recyclerDeck.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init computer hand recycler
        final RecyclerView recyclerComputerHand = mView.findViewById(R.id.recycler_computer_hand);
        recyclerComputerHand.setAdapter(new ComputerHandRecyclerAdapter(mContext, mComputer.getHand().getCards()));
        recyclerComputerHand.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init computer pile recycler
        final RecyclerView recyclerComputerPile = mView.findViewById(R.id.recycler_computer_pile);
        recyclerComputerPile.setAdapter(new CardsRecyclerAdapter(mContext, mComputer.getPile().getCards()));
        recyclerComputerPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init loose set recycler
        final RecyclerView recyclerLooseSet = mView.findViewById(R.id.recycler_loose_set);
        recyclerLooseSet.setAdapter(new LooseSetRecyclerAdapter(mContext, mRound.getTable().getLooseSet().getCards()));
        recyclerLooseSet.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init human hand recycler
        final RecyclerView recyclerHumanHand = mView.findViewById(R.id.recycler_human_hand);
        recyclerHumanHand.setAdapter(new HumanHandRecyclerAdapter(mContext, mHuman.getHand().getCards(), mMove));
        recyclerHumanHand.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init human pile recycler
        final RecyclerView recyclerHumanPile = mView.findViewById(R.id.recycler_human_pile);
        recyclerHumanPile.setAdapter(new CardsRecyclerAdapter(mContext, mHuman.getPile().getCards()));
        recyclerHumanPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    private void addListeners() {
        // Add get help listener
        final Button buttonGetHelp = mView.findViewById(R.id.button_get_help);
        buttonGetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        // Add make move listener
        final Button buttonMakeMove = mView.findViewById(R.id.button_make_move);
        buttonMakeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearStartMenu.setVisibility(View.GONE);
                mLinearMoveMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add build move listener
        final Button buttonBuildMove = mView.findViewById(R.id.button_build_move);
        buttonBuildMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearBuildMenu.setVisibility(View.VISIBLE);
            }
        });

        // Add capture move listener
        final Button buttonCaptureMove = mView.findViewById(R.id.button_capture_move);
        buttonCaptureMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);

                mMove.setType("capture");
            }
        });

        // Add trail move listener
        final Button buttonTrailMove = mView.findViewById(R.id.button_trail_move);
        buttonTrailMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearMoveMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);

                mMove.setType("trail");
            }
        });

        // Add increase move listener
        final Button buttonIncreaseMove = mView.findViewById(R.id.button_increase_move);
        buttonIncreaseMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);

                mMove.setType("increase");
            }
        });

        // Add extend move listener
        final Button buttonExtendMove = mView.findViewById(R.id.button_extend_move);
        buttonExtendMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);

                mMove.setType("extend");
            }
        });

        // Add create move listener
        final Button buttonCreateMove = mView.findViewById(R.id.button_create_move);
        buttonCreateMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearBuildMenu.setVisibility(View.GONE);
                mLinearEndMenu.setVisibility(View.VISIBLE);

                mMove.setType("create");
            }
        });

        // Add cancel move listener
        final Button buttonCancelMove = mView.findViewById(R.id.button_cancel_move);
        buttonCancelMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearEndMenu.setVisibility(View.GONE);
                mLinearStartMenu.setVisibility(View.VISIBLE);

                mMove = new Move();
            }
        });

        // Add submit move listener
        final Button buttonSubmitMove = mView.findViewById(R.id.button_submit_move);
        buttonSubmitMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearEndMenu.setVisibility(View.GONE);
                mLinearStartMenu.setVisibility(View.VISIBLE);
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }
}
