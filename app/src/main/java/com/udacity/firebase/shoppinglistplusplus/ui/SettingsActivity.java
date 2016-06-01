package com.udacity.firebase.shoppinglistplusplus.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

/**
 * SettingsActivity represents preference screen and functionality
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.PrefScreenTheme);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SortPreferenceFragment())
                .commit();
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class SortPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            /* Load the preferences from an XML resource */
            addPreferencesFromResource(R.xml.preference_screen);

            /**
             * Bind preference summary to value for lists and meals sorting list preferences
             */
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_name_sort_order_lists)));
        }

        /**
         * When preference is changed, save it's new value to default shared preferences
         *
         * @param preference
         * @param newValue
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            setPreferenceSummary(preference, newValue);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor spe = sharedPref.edit();
            spe.putString(Constants.KEY_PREF_SORT_ORDER_LISTS, newValue.toString()).apply();
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
        /* Set the listener to watch for value changes. */
            preference.setOnPreferenceChangeListener(this);
            /* Trigger the listener immediately with the preference's current value. */
            setPreferenceSummary(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        /**
         * Sets preference summary to appropriate value
         *
         * @param preference
         * @param value
         */
        private void setPreferenceSummary(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);

                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            }
        }
    }
}