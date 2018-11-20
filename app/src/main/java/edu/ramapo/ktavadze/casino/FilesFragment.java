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

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesFragment extends Fragment {
    private static final String TAG = "FilesFragment";

    private Context mContext;
    private View mView;

    private Tournament mTournament;

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

        mTournament = ((MainActivity)mContext).mTournament;

        mFiles = new ArrayList<>();
        mFiles.addAll(Arrays.asList(mContext.fileList()));
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
                actionLoadGame("case1");

                return true;
            case R.id.action_case_two:
                actionLoadGame("case2");

                return true;
            case R.id.action_case_three:
                actionLoadGame("case3");

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
                // TODO
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

    public void actionLoadGame(String aFile) {
        // Load
        try {
            FileInputStream inFile = mContext.openFileInput("game.txt");
            InputStreamReader inReader = new InputStreamReader(inFile);

            int bufferSize;
            char[] inBuffer = new char[64];
            StringBuilder stringBuilder = new StringBuilder();

            while ((bufferSize = inReader.read(inBuffer)) > 0) {
                String bufferString = String.copyValueOf(inBuffer, 0, bufferSize);
                stringBuilder.append(bufferString);
            }

            inReader.close();

            Log.d(TAG, "actionLoadGame: Game loaded\n" + stringBuilder.toString());

            // ((MainActivity)mContext).loadFragment(new GameFragment());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // List
        String [] list = mContext.fileList();

        if (list.length == 0) {
            Log.d(TAG, "actionLoadGame: List empty");
        }
        else {
            Log.d(TAG, "actionLoadGame: " + Arrays.toString(list));
        }
    }
}
