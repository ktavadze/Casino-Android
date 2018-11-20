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
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesFragment extends Fragment {
    private static final String TAG = "FilesFragment";

    private Context mContext;
    private View mView;

    private ArrayList<String> mFiles;

    private Button mButtonFilesCancel;
    private Button mButtonFilesClear;

    private FilesRecyclerAdapter mFilesAdapter;

    public FilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        mFiles = new ArrayList<>();
        mFiles.addAll(Arrays.asList(mContext.fileList()));
        mFiles.remove("instant-run");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_files, null);

        mButtonFilesCancel = mView.findViewById(R.id.button_files_cancel);
        mButtonFilesClear = mView.findViewById(R.id.button_files_clear);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Files");

        setHasOptionsMenu(true);

        addListeners();

        initView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_files, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_case_one:
                actionLoadAsset("case1.txt");

                return true;
            case R.id.action_case_two:
                actionLoadAsset("case2.txt");

                return true;
            case R.id.action_case_three:
                actionLoadAsset("case3.txt");

                return true;
            default:
                // Invoke superclass to handle unrecognized action.
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListeners() {
        // Add cancel listener
        mButtonFilesCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());
            }
        });

        // Add clear listener
        mButtonFilesClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFiles();
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }

    private void initView() {
        // Init files recycler
        final RecyclerView recyclerFiles = mView.findViewById(R.id.recycler_files);
        mFilesAdapter = new FilesRecyclerAdapter(FilesFragment.this, mFiles);
        recyclerFiles.setAdapter(mFilesAdapter);
        recyclerFiles.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    /**********************************************************************
     Function Name: actionLoadAsset
     Purpose: To load an asset
     Parameters:
     aName, a String
     **********************************************************************/
    public void actionLoadAsset(String aName) {
        // Load
        try {
            InputStream inFile = mContext.getAssets().open(aName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));

            int roundNumber = 0;
            int computerScore = 0;
            Set computerHand = new Set();
            Set computerPile = new Set();
            int humanScore = 0;
            Set humanHand = new Set();
            Set humanPile = new Set();
            Set looseSet = new Set();
            ArrayList<Build> builds = new ArrayList<>();
            ArrayList<Card> deckCards = new ArrayList<>();
            boolean humanIsNext = true;

            boolean readComputer = true;

            String line = reader.readLine();

            while (line != null) {
                if (line.contains("Round")) {
                    int index = line.indexOf(":");

                    roundNumber = Integer.parseInt(line.substring(index + 2));
                }
                else if (line.contains("Score")) {
                    int index = line.indexOf(":");

                    if (readComputer) {
                        computerScore = Integer.parseInt(line.substring(index + 2));
                    }
                    else {
                        humanScore = Integer.parseInt(line.substring(index + 2));
                    }
                }
                else if (line.contains("Hand")) {
                    if (line.length() > 9) {
                        int index = line.indexOf(":");

                        if (readComputer) {
                            String computerHandString = line.substring(index + 2);

                            String [] computerHandTokens = computerHandString.split(" ");

                            computerHand = generateSet(computerHandTokens);
                        }
                        else {
                            String humanHandString = line.substring(index + 2);

                            String [] humanHandTokens = humanHandString.split(" ");

                            humanHand = generateSet(humanHandTokens);
                        }
                    }
                }
                else if (line.contains("Pile")) {
                    int index = line.indexOf(":");

                    if (readComputer) {
                        if (line.length() > 9) {
                            String computerPileString = line.substring(index + 2);

                            String [] computerPileTokens = computerPileString.split(" ");

                            computerPile = generateSet(computerPileTokens);
                        }

                        readComputer = false;
                    }
                    else {
                        if (line.length() > 9) {
                            String humanPileString = line.substring(index + 2);

                            String [] humanPileTokens = humanPileString.split(" ");

                            humanPile = generateSet(humanPileTokens);
                        }
                    }
                }
                else if (line.contains("Table")) {
                    int looseSetStartIndex = 7;

                    if (line.contains("[")) {
                        looseSetStartIndex = line.lastIndexOf("]") + 2;
                    }

                    String looseSetString = line.substring(looseSetStartIndex);

                    String [] looseSetTokens = looseSetString.split(" ");

                    looseSet = generateSet(looseSetTokens);
                }
                else if (line.contains("Build Owner")) {
                    Build build = generateBuild(line);

                    builds.add(build);
                }
                else if (line.contains("Deck")) {
                    if (line.length() > 6) {
                        int index = line.indexOf(":");

                        String deckString = line.substring(index + 2);

                        String [] deckTokens = deckString.split(" ");

                        for (String token : deckTokens) {
                            deckCards.add(new Card(token));
                        }
                    }
                }
                else if (line.contains("Next Player")) {
                    if (line.contains("Computer")) {
                        humanIsNext = false;
                    }
                    else {
                        humanIsNext = true;
                    }
                }

                line = reader.readLine();
            }

            reader.close();

            Computer computer = new Computer(!humanIsNext, computerScore, computerHand, computerPile);
            Human human = new Human(humanIsNext, humanScore, humanHand, humanPile);

            Table table = new Table(looseSet, builds);
            Deck deck = new Deck(deckCards);

            Round round = new Round(roundNumber, table, deck);

            ((MainActivity)mContext).mTournament = new Tournament(computer, human, round);

            ((MainActivity)mContext).loadFragment(new RoundFragment());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************
     Function Name: actionLoadFile
     Purpose: To load a file
     Parameters:
     aName, a String
     **********************************************************************/
    public void actionLoadFile(String aName) {
        // Load
        try {
            FileInputStream inFile = mContext.openFileInput(aName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));

            int roundNumber = 0;
            int computerScore = 0;
            Set computerHand = new Set();
            Set computerPile = new Set();
            int humanScore = 0;
            Set humanHand = new Set();
            Set humanPile = new Set();
            Set looseSet = new Set();
            ArrayList<Build> builds = new ArrayList<>();
            ArrayList<Card> deckCards = new ArrayList<>();
            boolean humanIsNext = true;

            boolean readComputer = true;

            String line = reader.readLine();

            while (line != null) {
                if (line.contains("Round")) {
                    int index = line.indexOf(":");

                    roundNumber = Integer.parseInt(line.substring(index + 2));
                }
                else if (line.contains("Score")) {
                    int index = line.indexOf(":");

                    if (readComputer) {
                        computerScore = Integer.parseInt(line.substring(index + 2));
                    }
                    else {
                        humanScore = Integer.parseInt(line.substring(index + 2));
                    }
                }
                else if (line.contains("Hand")) {
                    if (line.length() > 9) {
                        int index = line.indexOf(":");

                        if (readComputer) {
                            String computerHandString = line.substring(index + 2);

                            String [] computerHandTokens = computerHandString.split(" ");

                            computerHand = generateSet(computerHandTokens);
                        }
                        else {
                            String humanHandString = line.substring(index + 2);

                            String [] humanHandTokens = humanHandString.split(" ");

                            humanHand = generateSet(humanHandTokens);
                        }
                    }
                }
                else if (line.contains("Pile")) {
                    int index = line.indexOf(":");

                    if (readComputer) {
                        if (line.length() > 9) {
                            String computerPileString = line.substring(index + 2);

                            String [] computerPileTokens = computerPileString.split(" ");

                            computerPile = generateSet(computerPileTokens);
                        }

                        readComputer = false;
                    }
                    else {
                        if (line.length() > 9) {
                            String humanPileString = line.substring(index + 2);

                            String [] humanPileTokens = humanPileString.split(" ");

                            humanPile = generateSet(humanPileTokens);
                        }
                    }
                }
                else if (line.contains("Table")) {
                    int looseSetStartIndex = 7;

                    if (line.contains("[")) {
                        looseSetStartIndex = line.lastIndexOf("]") + 2;
                    }

                    String looseSetString = line.substring(looseSetStartIndex);

                    String [] looseSetTokens = looseSetString.split(" ");

                    looseSet = generateSet(looseSetTokens);
                }
                else if (line.contains("Build Owner")) {
                    Build build = generateBuild(line);

                    builds.add(build);
                }
                else if (line.contains("Deck")) {
                    if (line.length() > 6) {
                        int index = line.indexOf(":");

                        String deckString = line.substring(index + 2);

                        String [] deckTokens = deckString.split(" ");

                        for (String token : deckTokens) {
                            deckCards.add(new Card(token));
                        }
                    }
                }
                else if (line.contains("Next Player")) {
                    if (line.contains("Computer")) {
                        humanIsNext = false;
                    }
                    else {
                        humanIsNext = true;
                    }
                }

                line = reader.readLine();
            }

            reader.close();

            Computer computer = new Computer(!humanIsNext, computerScore, computerHand, computerPile);
            Human human = new Human(humanIsNext, humanScore, humanHand, humanPile);

            Table table = new Table(looseSet, builds);
            Deck deck = new Deck(deckCards);

            Round round = new Round(roundNumber, table, deck);

            ((MainActivity)mContext).mTournament = new Tournament(computer, human, round);

            ((MainActivity)mContext).loadFragment(new RoundFragment());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFiles() {
        String [] list = mContext.fileList();

        for (String file : list) {
            if (!file.equals("instant-run")) {
                mFiles.remove(file);

                mContext.deleteFile(file);
            }
        }

        mFilesAdapter.notifyDataSetChanged();
    }

    /**********************************************************************
     Function Name: seedDeck
     Purpose: To seed deck cards
     Parameters:
     aDeck, a Deck instance passed by reference
     Return Value: Whether the deck was successfully seeded, a boolean value
     **********************************************************************/
//    private void seedDeck(Deck & aDeck) {
//        ifstream infile("../Data/deck.txt");
//
//        if (infile.isOpen()) {
//            ArrayList<Card> cards;
//
//            String line;
//
//            while (getline(infile, line)) {
//                cards.add(Card(line));
//            }
//
//            aDeck = Deck(cards);
//        }
//        else {
//            Console::displayMessage("ERROR: cannot seed deck!");
//        }
//    }

    /**********************************************************************
     Function Name: generateSet
     Purpose: To generate a set from tokens
     Parameters:
     aTokens, a ArrayList of strings passed by value
     Return Value: Generated set, an Set instance
     **********************************************************************/
    private Set generateSet(String [] aTokens) {
        Set set = new Set();

        for (String token : aTokens) {
            set.addCard(new Card(token));
        }

        return set;
    }

    /**********************************************************************
     Function Name: generateBuild
     Purpose: To generate a build from tokens
     Parameters:
     aString, a String
     Return Value: Generated build, an Build instance
     **********************************************************************/
    private Build generateBuild(String aString) {
        Build build = new Build();

        if (aString.contains("Computer")) {
            build.isHuman(false);
        }
        else {
            build.isHuman(true);
        }

        int buildStartIndex = aString.indexOf("[");
        int buildEndIndex = aString.lastIndexOf("]");

        aString = aString.substring(buildStartIndex + 1, buildEndIndex).trim();

        while (!aString.isEmpty()) {
            int setStartIndex = aString.indexOf("[");
            int setEndIndex = aString.indexOf("]");

            String setString = aString.substring(setStartIndex + 1, setEndIndex);

            String [] setTokens = setString.split(" ");

            Set set = generateSet(setTokens);

            build.extend(set);

            aString = aString.substring(setEndIndex + 1);
        }

        return build;
    }
}
