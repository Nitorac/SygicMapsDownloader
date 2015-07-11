package com.nitorac.sygicdownloader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.nitorac.sygicdownloader.R;

public class SettingsActivity extends PreferenceActivity {

    public String str(int id){
        return getResources().getString(id);
    }

    public boolean needMainActivityReload = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Preference monthPref = getPreferenceScreen().findPreference("month");
        Preference yearPref = getPreferenceScreen().findPreference("year");
        CheckBoxPreference autoDownloadPref = (CheckBoxPreference)getPreferenceScreen().findPreference("startAutoDownload");
        ListPreference dataList = (ListPreference)getPreferenceScreen().findPreference("data");

        monthPref.setSummary(str(R.string.monthPrefSummary) +" "+ java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt(prefs.getString("month", "01"))));
        yearPref.setSummary(str(R.string.yearPrefSummary) + " " + Integer.parseInt(prefs.getString("year", "2013")));
        if(!prefs.getString("data", "null").equals("null")) {
            dataList.setSummary(str(R.string.curModeData) + " " + getResources().getStringArray(R.array.dataConnection)[dataGetInt(prefs.getString("data", "null"))]);
        }else{
            dataList.setSummary(str(R.string.noConnectionMode));
        }

        if(prefs.getBoolean("startAutoDownload", false)){
            autoDownloadPref.setChecked(true);
        }else{
            autoDownloadPref.setChecked(false);
        }

        monthPref.setOnPreferenceChangeListener(monthCheckListener);
        yearPref.setOnPreferenceChangeListener(yearCheckListener);
        dataList.setOnPreferenceChangeListener(dataListCheckListener);
    }

    public int dataGetInt(String str){
        if(str.equals(getResources().getStringArray(R.array.dataConnection)[0])){
            return 0;
        }else if(str.equals(getResources().getStringArray(R.array.dataConnection)[1])){
            return 1;
        }
        return -1;
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
                pref.setSummary(str(R.string.monthPrefSummary) +" "+ java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt((String) newValue)));
                needMainActivityReload = true;
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
                needMainActivityReload = true;
                return true;
            }
            // If now create a message to the user
            Toast.makeText(SettingsActivity.this, str(R.string.yearPrefOutBounds), Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    Preference.OnPreferenceChangeListener dataListCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {

            if(newValue.equals(getResources().getStringArray(R.array.dataConnection)[1]) || newValue.equals(getResources().getStringArray(R.array.dataConnection)[0])){
                pref.setSummary(str(R.string.curModeData) + " " + newValue);
                needMainActivityReload = true;
                return true;
            }
            Toast.makeText(SettingsActivity.this, str(R.string.invalidInput), Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    @Override
    public void onBackPressed(){
        if(needMainActivityReload) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("checkMaj", false);
            startActivity(i);
            finish();
        }else{
            finish();
        }
    }
}