package com.nitorac.sygicdownloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.nitorac.sygicdownloader.R;

public class SettingsActivity extends PreferenceActivity {

    public String str(int id){
        return getResources().getString(id);
    }

    public boolean needMainActivityReload = false;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        Preference monthPref = getPreferenceScreen().findPreference("month");
        Preference yearPref = getPreferenceScreen().findPreference("year");
        CheckBoxPreference autoDownloadPref = (CheckBoxPreference)getPreferenceScreen().findPreference("startAutoDownload");
        ListPreference dataList = (ListPreference)getPreferenceScreen().findPreference("data");
        Preference maj = getPreferenceScreen().findPreference("ignore");
        Preference doNotAsk = getPreferenceScreen().findPreference("ignore2");

        boolean getValueDialogYes = prefs.getBoolean("countryNoRepeat", false);

        monthPref.setSummary(str(R.string.monthPrefSummary) + " " + java.text.MessageFormat.format("{0,number,#00}", Integer.parseInt(prefs.getString("month", "01"))));
        yearPref.setSummary(str(R.string.yearPrefSummary) + " " + Integer.parseInt(prefs.getString("year", "2013")));
        maj.setSummary(String.format(str(R.string.curVersion),str(R.string.versionName)));
        if(getValueDialogYes) {
            doNotAsk.setSummary(str(R.string.foundDoNotAskAgainPref));
            doNotAsk.setEnabled(true);
        }else{
            doNotAsk.setSummary(str(R.string.noDoNotAskPref));
            doNotAsk.setEnabled(false);
        }

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
        maj.setOnPreferenceClickListener(updateCheckListener);
        doNotAsk.setOnPreferenceClickListener(doNotAskListener);
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

    Preference.OnPreferenceClickListener updateCheckListener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference pref) {
            CheckUpdateAsync cua = new CheckUpdateAsync(SettingsActivity.this);
            cua.execute();
            return true;
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

    Preference.OnPreferenceClickListener doNotAskListener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(final Preference pref) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
            alertDialogBuilder.setTitle(str(R.string.doNotAskAgain));
            alertDialogBuilder.setIcon(R.drawable.ic_warning);
            alertDialogBuilder
                    .setMessage(str(R.string.doNotAskMessage))
                    .setCancelable(false)
                    .setNegativeButton(str(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton(str(R.string.desactivate),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editor.putBoolean("countryNoRepeat", false);
                                    editor.commit();
                                    pref.setSummary(str(R.string.noDoNotAskPref));
                                    pref.setEnabled(false);
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
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

    private class CheckUpdateAsync extends AsyncTask<Void, Void, Void>{

        private volatile SettingsActivity screen;
        private ProgressDialog progress;
        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;

        public String str(int id){return screen.getResources().getString(id);}

        public CheckUpdateAsync(SettingsActivity ma){
            this.screen = ma;
            this.progress = new ProgressDialog(this.screen);
            this.alertDialogBuilder = new AlertDialog.Builder(this.screen);
        }

        @Override
        protected void onPreExecute(){
            if(android.os.Build.VERSION.SDK_INT >= 11) {
                this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                this.progress.setMessage(str(R.string.updateGetting));
                this.progress.setIndeterminate(true);
                this.progress.setCanceledOnTouchOutside(false);
                this.progress.setCancelable(false);
                this.progress.show();
            }else {
                LayoutInflater inflater = this.screen.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.process_dialog, null);
                alertDialogBuilder.setTitle(str(R.string.pleaseWait));
                alertDialogBuilder.setIcon(this.screen.getResources().getDrawable(R.drawable.ic_progress));
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setCancelable(false);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Updater updater = new Updater(SettingsActivity.this);
            updater.updaterMain(MainActivity.downloadDir,MainActivity.downloadUrl,MainActivity.checkUrl,true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(android.os.Build.VERSION.SDK_INT >= 11){
                if(this.progress.isShowing()) this.progress.dismiss();
            }else {
                if (this.alertDialog.isShowing()) this.alertDialog.dismiss();
            }
        }
    }
}