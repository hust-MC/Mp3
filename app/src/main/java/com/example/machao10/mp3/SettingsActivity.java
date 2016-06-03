package com.example.machao10.mp3;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    ListPreference listPlayMode, listLrcSize, listLrcColor, listRing, listNotification, listSms;
    EditTextPreference etAutoShutdown;
    SwitchPreference switchShake;

    private void initPreference() {
        listPlayMode = (ListPreference) findPreference(getString(R.string.key_play_mode));

        SettingsChangeListener listener = new SettingsChangeListener();
        listPlayMode.setOnPreferenceChangeListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        initPreference();
    }

    class SettingsChangeListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();

            return true;
        }
    }
}
