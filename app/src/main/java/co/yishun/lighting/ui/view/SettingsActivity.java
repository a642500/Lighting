package co.yishun.lighting.ui.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.BaseActivity;

/**
 * Created by carlos on 3/24/16.
 */
public class SettingsActivity extends BaseActivity {
    public static final String TAG = "SettingsActivity";

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
    }
}
