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

/**
 * MainMenuFragment Class to implement the main menu view.
 */

public class MainMenuFragment extends Fragment {
    private static final String TAG = "MainMenuFragment";

    private Context mContext;

    private Button mButtonMenuStartGame;
    private Button mButtonMenuLoadGame;
    private Button mButtonMenuQuitGame;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main_menu, null);

        mButtonMenuStartGame = view.findViewById(R.id.button_menu_start_game);
        mButtonMenuLoadGame = view.findViewById(R.id.button_menu_load_game);
        mButtonMenuQuitGame = view.findViewById(R.id.button_menu_quit_game);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Main Menu");

        addListeners();
    }

    /**
     Adds listeners to essential UI components.
     */
    private void addListeners() {
        // Add start game listener
        mButtonMenuStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new CoinTossFragment());
            }
        });

        // Add load game listener
        mButtonMenuLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new FilesFragment());
            }
        });

        // Add quit game listener
        mButtonMenuQuitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).finish();
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }
}
