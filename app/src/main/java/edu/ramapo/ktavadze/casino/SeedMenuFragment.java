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

public class SeedMenuFragment extends Fragment {
    private static final String TAG = "SeedMenuFragment";

    private Context mContext;

    private Button mButtonMenuNewDeck;
    private Button mButtonMenuSeedDeck;

    public SeedMenuFragment() {
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

        View view = inflater.inflate(R.layout.fragment_seed_menu, null);

        mButtonMenuNewDeck = view.findViewById(R.id.button_menu_new_deck);
        mButtonMenuSeedDeck = view.findViewById(R.id.button_menu_seed_deck);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Seed Menu");

        addListeners();
    }

    private void addListeners() {
        // Add new deck listener
        mButtonMenuNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).loadFragment(new GameFragment());
            }
        });

        // Add seed deck listener
        mButtonMenuSeedDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        Log.d(TAG, "addListeners: Listeners added");
    }
}
