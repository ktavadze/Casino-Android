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

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    private Move mMove;

    private LinearLayout mLinearStartMenu;
    private LinearLayout mLinearMoveMenu;
    private LinearLayout mLinearBuildMenu;
    private LinearLayout mLinearEndMenu;

    private CardsRecyclerAdapter mDeckAdapter;
    private ComputerHandRecyclerAdapter mComputerHandAdapter;
    private CardsRecyclerAdapter mComputerPileAdapter;
    private LooseSetRecyclerAdapter mLooseSetAdapter;
    private HumanHandRecyclerAdapter mHumanHandAdapter;
    private CardsRecyclerAdapter mHumanPileAdapter;

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

        initView();
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
                if (mComputer.isNext()) {
                    if (mRound.processMove(mComputer, mHuman, mMove)) {
                        updateView();
                    }

                    return;
                }

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

                updateView();
            }
        });

        // Add submit move listener
        final Button buttonSubmitMove = mView.findViewById(R.id.button_submit_move);
        buttonSubmitMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Fuck: " + mMove.getPlayerCard().getName());

                if (!mMove.getPlayerCard().isJoker()) {
                    if (mRound.processMove(mComputer, mHuman, mMove)) {
                        mLinearEndMenu.setVisibility(View.GONE);
                        mLinearStartMenu.setVisibility(View.VISIBLE);

                        updateView();
                    }
                }
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void initView() {
        // Init deck recycler
        final RecyclerView recyclerDeck = mView.findViewById(R.id.recycler_deck);
        mDeckAdapter = new CardsRecyclerAdapter(mContext, mRound.getDeck().getCards());
        recyclerDeck.setAdapter(mDeckAdapter);
        recyclerDeck.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init computer hand recycler
        final RecyclerView recyclerComputerHand = mView.findViewById(R.id.recycler_computer_hand);
        mComputerHandAdapter = new ComputerHandRecyclerAdapter(mContext, mComputer.getHand().getCards());
        recyclerComputerHand.setAdapter(mComputerHandAdapter);
        recyclerComputerHand.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init computer pile recycler
        final RecyclerView recyclerComputerPile = mView.findViewById(R.id.recycler_computer_pile);
        mComputerPileAdapter = new CardsRecyclerAdapter(mContext, mComputer.getPile().getCards());
        recyclerComputerPile.setAdapter(mComputerPileAdapter);
        recyclerComputerPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init loose set recycler
        final RecyclerView recyclerLooseSet = mView.findViewById(R.id.recycler_loose_set);
        mLooseSetAdapter = new LooseSetRecyclerAdapter(mContext, mRound.getTable().getLooseSet().getCards());
        recyclerLooseSet.setAdapter(mLooseSetAdapter);
        recyclerLooseSet.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init human hand recycler
        final RecyclerView recyclerHumanHand = mView.findViewById(R.id.recycler_human_hand);
        mHumanHandAdapter = new HumanHandRecyclerAdapter(mContext, mHuman.getHand().getCards(), mMove);
        recyclerHumanHand.setAdapter(mHumanHandAdapter);
        recyclerHumanHand.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        // Init human pile recycler
        final RecyclerView recyclerHumanPile = mView.findViewById(R.id.recycler_human_pile);
        mHumanPileAdapter = new CardsRecyclerAdapter(mContext, mHuman.getPile().getCards());
        recyclerHumanPile.setAdapter(mHumanPileAdapter);
        recyclerHumanPile.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    private void updateView() {
        mDeckAdapter.notifyDataSetChanged();
        mComputerHandAdapter.notifyDataSetChanged();
        mComputerPileAdapter.notifyDataSetChanged();
        mLooseSetAdapter.notifyDataSetChanged();
        mHumanHandAdapter.notifyDataSetChanged();
        mHumanPileAdapter.notifyDataSetChanged();

        if (mMove.getPlayerCardView() != null) {
            mMove.getPlayerCardView().setBackgroundColor(0);
        }

        for (View view : mMove.getLooseSetViews()) {
            view.setBackgroundColor(0);
        }

        for (View view : mMove.getBuildViews()) {
            view.setBackgroundColor(0);
        }

        mMove.clear();
    }
}
