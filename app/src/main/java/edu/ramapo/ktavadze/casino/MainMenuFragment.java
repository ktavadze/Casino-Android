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

public class MainMenuFragment extends Fragment {
    private static final String TAG = "MainMenuFragment";

    private Context mContext;
    private View mView;

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

        mView = inflater.inflate(R.layout.fragment_main_menu, null);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Main Menu");

        addListeners();
    }

    private void addListeners() {
        // Add start listener
        final Button buttonMenuStart = mView.findViewById(R.id.button_menu_start);
        buttonMenuStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new CoinTossFragment());
            }
        });

        // Add load listener
        final Button buttonMenuLoad = mView.findViewById(R.id.button_menu_load);
        buttonMenuLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());
            }
        });

        // Add quit listener
        final Button buttonMenuQuit = mView.findViewById(R.id.button_menu_quit);
        buttonMenuQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new MainMenuFragment());
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }
}
