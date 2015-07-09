package com.nitorac.sygicdownloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.nitorac.sygicdownloader.R;

public class SettingsActivity extends PreferenceActivity {

    public String str(int id){
        return getResources().getString(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Preference monthPref = getPreferenceScreen().findPreference("month");
        Preference yearPref = getPreferenceScreen().findPreference("year");

        monthPref.setSummary(str(R.string.monthPrefSummary) +" "+ java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt(prefs.getString("month", "01"))));
        yearPref.setSummary(str(R.string.yearPrefSummary)+" "+ Integer.parseInt(prefs.getString("year", "2013")));

        monthPref.setOnPreferenceChangeListener(monthCheckListener);
        yearPref.setOnPreferenceChangeListener(yearCheckListener);
    }

    Preference.OnPreferenceChangeListener monthCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            // Check that the string is an integer
            try{
                Integer.parseInt((String)newValue);
            }catch(Exception e){
                Toast.makeText(SettingsActivity.this, str(R.string.invalidInput), Toast.LENGTH_SHORT).show();
                return false;
            }
            int temp = Integer.parseInt((String)newValue);
            if (temp >= 1 && temp <= 12) {
                pref.setSummary(str(R.string.monthPrefSummary) +" "+ java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt((String)newValue)));
                return true;
            }
            // If now create a message to the user
            Toast.makeText(SettingsActivity.this, str(R.string.monthPrefOutBounds), Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    Preference.OnPreferenceChangeListener yearCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            // Check that the string is an integer
            try{
                Integer.parseInt((String)newValue);
            }catch(Exception e){
                Toast.makeText(SettingsActivity.this, str(R.string.invalidInput), Toast.LENGTH_SHORT).show();
                return false;
            }
            int temp = Integer.parseInt((String)newValue);
            if (temp >= 2000) {
                pref.setSummary(str(R.string.yearPrefSummary)+" "+ Integer.parseInt((String) newValue));
                return true;
            }
            // If now create a message to the user
            Toast.makeText(SettingsActivity.this, str(R.string.yearPrefOutBounds), Toast.LENGTH_SHORT).show();
            return false;
        }
    };
}