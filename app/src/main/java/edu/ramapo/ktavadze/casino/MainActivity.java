package edu.ramapo.ktavadze.casino;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public Tournament mTournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load main menu fragment
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_main_container, new MainMenuFragment())
                    .commit();
        }
    }

    public void loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_container);
        if (fragment != null && currentFragment != null) {
            if (!fragment.getClass().equals(currentFragment.getClass())) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_main_container, fragment)
                        .commit();

                Log.d(TAG, "loadFragment: Fragment was loaded");
            }
        }

        Log.d(TAG, "loadFragment: Fragment was not loaded");
    }
}
