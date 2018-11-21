package edu.ramapo.ktavadze.casino;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class RoundFragment extends Fragment {
    private static final String TAG = "RoundFragment";

    private Context mContext;
    private View mView;

    private Tournament mTournament;
    private Player mComputer;
    private Player mHuman;
    private Round mRound;

    private Move mMove;

    private EditText mEditMessageLog;
    private Button mButtonMakeMove;
    private LinearLayout mLinearStartMenu;
    private LinearLayout mLinearMoveMenu;
    private LinearLayout mLinearBuildMenu;
    private LinearLayout mLinearEndMenu;

    private CardsRecyclerAdapter mDeckAdapter;
    private ComputerHandRecyclerAdapter mComputerHandAdapter;
    private CardsRecyclerAdapter mComputerPileAdapter;
    private BuildsRecyclerAdapter mBuildsAdapter;
    private LooseSetRecyclerAdapter mLooseSetAdapter;
    private HumanHandRecyclerAdapter mHumanHandAdapter;
    private CardsRecyclerAdapter mHumanPileAdapter;

    public RoundFragment() {
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

        mView = inflater.inflate(R.layout.fragment_round, null);

        mEditMessageLog = mView.findViewById(R.id.edit_message_log);
        mButtonMakeMove = mView.findViewById(R.id.button_make_move);
        mLinearStartMenu = mView.findViewById(R.id.linear_start_menu);
        mLinearMoveMenu = mView.findViewById(R.id.linear_move_menu);
        mLinearBuildMenu = mView.findViewById(R.id.linear_build_menu);
        mLinearEndMenu = mView.findViewById(R.id.linear_end_menu);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Round " + mRound.getNumber());

        setHasOptionsMenu(true);

        addListeners();

        mRound.start(mComputer, mHuman);

        initView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_game:
                actionSaveGame();

                return true;
            case R.id.action_quit_game:
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());

                return true;
            default:
                // Invoke superclass to handle unrecognized action.
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListeners() {
        // Add get help listener
        final Button buttonGetHelp = mView.findViewById(R.id.button_get_help);
        buttonGetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHuman.isNext()) {
                    mHuman.askForHelp(mRound.getTable());

                    logMessage(mHuman.getMessage());
                }
                else {
                    mComputer.askForHelp(mRound.getTable());

                    logMessage(mComputer.getMessage());
                }
            }
        });

        // Add make move listener
        mButtonMakeMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComputer.isNext()) {
                    if (mRound.processMove(mComputer, mHuman, mMove)) {
                        if (mRound.isOver(mComputer, mHuman)) {
                            ((MainActivity)mContext).loadFragment(new RoundResultsFragment());
                        }
                        else {
                            updateView();
                        }
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
            }
        });

        // Add submit move listener
        final Button buttonSubmitMove = mView.findViewById(R.id.button_submit_move);
        buttonSubmitMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validSelection()) {
                    if (mRound.processMove(mComputer, mHuman, mMove)) {
                        mLinearEndMenu.setVisibility(View.GONE);
                        mLinearStartMenu.setVisibility(View.VISIBLE);

                        if (mRound.isOver(mComputer, mHuman)) {
                            ((MainActivity)mContext).loadFragment(new RoundResultsFragment());
                        }
                        else {
                            updateView();
                        }
                    }
                    else if (mHuman.isNext()) {
                        logMessage(mHuman.getMessage());
                    }
                }
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void logMessage(String aMessage) {
        mEditMessageLog.append(aMessage + "\n");
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

        // Init builds recycler
        final RecyclerView recyclerBuilds = mView.findViewById(R.id.recycler_builds);
        mBuildsAdapter = new BuildsRecyclerAdapter(mContext, mRound.getTable().getBuilds(), mMove);
        recyclerBuilds.setAdapter(mBuildsAdapter);
        recyclerBuilds.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true));

        // Init loose set recycler
        final RecyclerView recyclerLooseSet = mView.findViewById(R.id.recycler_loose_set);
        mLooseSetAdapter = new LooseSetRecyclerAdapter(mContext, mRound.getTable().getLooseSet().getCards(), mMove);
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

        mEditMessageLog.setKeyListener(null);

        logMessage("Computer: " + mComputer.getScore());
        logMessage("Human: " + mHuman.getScore());

        if (mHuman.isNext()) {
            logMessage("\nHuman");

            mButtonMakeMove.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
        }
        else {
            logMessage("\nComputer");

            mButtonMakeMove.setBackgroundColor(getResources().getColor(R.color.colorRedDark));
        }

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }

    private void updateView() {
        mDeckAdapter.notifyDataSetChanged();
        mComputerHandAdapter.notifyDataSetChanged();
        mComputerPileAdapter.notifyDataSetChanged();
        mBuildsAdapter.notifyDataSetChanged();
        mLooseSetAdapter.notifyDataSetChanged();
        mHumanHandAdapter.notifyDataSetChanged();
        mHumanPileAdapter.notifyDataSetChanged();

        // Display turn info
        if (mHuman.isNext()) {
            logMessage(mComputer.getMessage());
            logMessage("\nHuman");

            mButtonMakeMove.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
        }
        else {
            logMessage(mHuman.getMessage());
            logMessage("\nComputer");

            mButtonMakeMove.setBackgroundColor(getResources().getColor(R.color.colorRedDark));
        }

        // Clear move
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

        Log.d(TAG, "Game state: \n" + mRound.stringify(mComputer, mHuman));
    }

    private boolean validSelection() {
        // Check player card
        if (mMove.getPlayerCard().isJoker()) {
            logMessage("Please select which card to play");

            return false;
        }

        // Check the rest
        switch (mMove.getType()) {
            case "increase":
                if (mMove.getBuilds().size() != 1) {
                    logMessage("Please select which build to increase");

                    return false;
                }
                break;
            case "extend":
                if (mMove.getBuilds().size() != 1) {
                    logMessage("Please select which build to extend");

                    return false;
                }
                break;
            case "create":
                if (mMove.getLooseSet().isEmpty()) {
                    logMessage("Please select loose card(s) to create");

                    return false;
                }
                break;
            case "capture":
                if (mMove.getLooseSet().isEmpty() && mMove.getBuilds().isEmpty()) {
                    logMessage("Please select something to capture");

                    return false;
                }
                break;
        }

        return true;
    }

    private void actionSaveGame() {
        // Build save game dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_save_game, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Save Game");

        // Define fields
        final EditText editSaveName = dialogView.findViewById(R.id.edit_save_name);

        // Define responses
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Set name
                String saveName = editSaveName.getText().toString().trim();

                if (saveName.isEmpty()) {
                    saveName = "game.txt";
                }

                // Save
                try {
                    FileOutputStream outFile = mContext.openFileOutput(saveName, Context.MODE_PRIVATE);
                    OutputStreamWriter outWriter = new OutputStreamWriter(outFile);
                    outWriter.write(mRound.stringify(mComputer, mHuman));
                    outWriter.close();

                    Log.d(TAG, "actionSaveGame: Game saved");

                    ((MainActivity)mContext).loadFragment(new MainMenuFragment());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO
            }
        });

        // Show save game dialog
        AlertDialog dialogSaveGame = dialogBuilder.create();
        dialogSaveGame.show();
    }
}
