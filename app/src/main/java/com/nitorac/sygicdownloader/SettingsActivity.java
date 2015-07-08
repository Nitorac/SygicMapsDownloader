package com.nitorac.sygicdownloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.nitorac.sygicdownloader.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Preference monthPref = getPreferenceScreen().findPreference("month");
        Preference yearPref = getPreferenceScreen().findPreference("year");

        monthPref.setSummary("Entrez un mois entre 1 et 12\nMois de départ actuel : " + java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt(prefs.getString("month", "01"))));
        yearPref.setSummary("Entrez une année depuis 2000\nAnnée de départ actuelle : " + java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt(prefs.getString("year", "2013"))));

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
                Toast.makeText(SettingsActivity.this, "Entrée invalide", Toast.LENGTH_SHORT).show();
                return false;
            }
            int temp = Integer.parseInt((String)newValue);
            if (temp >= 1 && temp <= 12) {
                pref.setSummary("Entrez un mois entre 1 et 12\nMois de départ actuel : " + java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt((String)newValue)));
                return true;
            }
            // If now create a message to the user
            Toast.makeText(SettingsActivity.this, "Mois hors-limite", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SettingsActivity.this, "Entrée invalide", Toast.LENGTH_SHORT).show();
                return false;
            }
            int temp = Integer.parseInt((String)newValue);
            if (temp >= 2000) {
                pref.setSummary("Entrez une année depuis 2000\nAnnée de départ actuelle : " + java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt((String)newValue)));
                return true;
            }
            // If now create a message to the user
            Toast.makeText(SettingsActivity.this, "Année hors-limite", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
}