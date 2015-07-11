package com.nitorac.sygicdownloader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitorac.sygicdownloader.downloader.DownloaderActivity;
import com.nitorac.sygicdownloader.liststartitems.Items_Maj;
import com.nitorac.sygicdownloader.liststartitems.MajAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nitorac.
 */
public class MainActivity extends ActionBarActivity {


    public static String continent_chosen = "";
    public static String country_chosen = "";
    public static String prefix = "";
    public static String country_name_chosen = "null";
    public static int flag_drawable_chosen = 0;
    public static int month_maj = 0;
    public static int year_maj = 0;
    public String[] maj_list;

    public boolean hasConnectedWifi = false;
    public boolean hasConnectedMobile = false;

    public ListView listView;

    public TextView lastMAJ;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public int txtMonth = 1;
    public int txtYear = 2013;

    public String downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/SygicDownloader.apk";
    public String downloadUrl = "http://electroteam.bl.ee/SygicDL/SygicDL.apk1";
    public String checkUrl = "http://electroteam.bl.ee/SygicDL/version.txt";

    public boolean checkMaj = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        Bundle b = getIntent().getExtras();
        if(b!=null){
            checkMaj = b.getBoolean("checkMaj");
        }

        txtMonth = Integer.parseInt(prefs.getString("month", "01"));
        txtYear = Integer.parseInt(prefs.getString("year", "2013"));
        listView = (ListView) findViewById(R.id.listView);
        lastMAJ = (TextView) findViewById(R.id.lastMaj);

        String dataConnection = prefs.getString("data", "null");
        final String getCountry = prefs.getString("country", "null");
        final String getCountryCode = prefs.getString("country_code", "null");
        final String getContinent = prefs.getString("continent", "null");
        final int getFlagDrawable = prefs.getInt("flag", 0);
        final boolean getNoRecurrentDialog = prefs.getBoolean("countryNoRepeat", false);
        final boolean getValueDialogYes = prefs.getBoolean("valueDialog", false);

        ActionBar actionBar = getSupportActionBar();

        haveNetworkConnection(dataConnection);

        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setTitle(" " + str(R.string.app_name));
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if(!hasConnectedMobile && !hasConnectedWifi){
            noConnectionDialog();
        }else if(hasConnectedMobile && !hasConnectedWifi && dataConnection.equals("null")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(str(R.string.noConnectionTitle));
            alertDialogBuilder.setIcon(R.drawable.ic_data_connection);
            alertDialogBuilder
                    .setMessage(str(R.string.wifiMobileConnectionMsg))
                    .setCancelable(false)
                    .setNegativeButton(str(R.string.wifi),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editor.putString("data", getResources().getStringArray(R.array.dataConnection)[1]);
                                    editor.commit();
                                    continueStuff(getCountry, getContinent, getCountryCode, getFlagDrawable, getNoRecurrentDialog, getValueDialogYes);
                                }
                            })
                    .setPositiveButton(str(R.string.wifiOnly),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    editor.putString("data", getResources().getStringArray(R.array.dataConnection)[0]);
                                    editor.commit();
                                    if (!hasConnectedWifi) {
                                        noConnectionDialog();
                                    }else{
                                        continueStuff(getCountry, getContinent, getCountryCode, getFlagDrawable, getNoRecurrentDialog, getValueDialogYes);
                                    }
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else if(hasConnectedWifi){
            continueStuff(getCountry, getContinent, getCountryCode, getFlagDrawable, getNoRecurrentDialog, getValueDialogYes);
        }


    }

    //TODO: Ajouter doNotAsk exit dans les paramètres
    //TODO: Ajouter un checkUpdate dans la preference activity

    public void continueStuff(String getCountry, String getContinent, String getCountryCode, int getFlagDrawable, boolean getNoRecurrentDialog, boolean getValueDialogYes){
        if(checkMaj) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(MainActivity.this);
                    updater.updaterMain(downloadDir, downloadUrl, checkUrl);
                }
            }).start();
        }
        if(!getCountry.equals("null") && !getContinent.equals("null") && !getCountryCode.equals("null") && getFlagDrawable != 0){
            if(!getNoRecurrentDialog){
                showCountryDialog(getCountry,getCountryCode,getContinent,getFlagDrawable);
            }else{
                if(getValueDialogYes) {
                    country_chosen = getCountryCode;
                    continent_chosen = getContinent;
                    flag_drawable_chosen = getFlagDrawable;
                    country_name_chosen = getCountry;
                    getSupportActionBar().setLogo(getResources().getDrawable(getFlagDrawable));
                    getSupportActionBar().setTitle(" " + getCountry + " : " + str(R.string.sygicMapTitle));
                    finishedDetectionPart();
                }else{
                    getContinent();
                }
            }
        }else{
            getContinent();
        }
    }

    public void showCountryDialog(final String country, final String country_code, final String continent, final int flag){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(str(R.string.countries));
        alertDialogBuilder.setIcon(R.drawable.ic_countries);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.checkbox_dialog, null);
        alertDialogBuilder
                .setMessage(String.format(str(R.string.useSameCountry), country))
                .setView(dialogView)
                .setCancelable(false)
                .setNegativeButton(str(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.doNotAskCheckBox);
                                if (checkBox.isChecked()) {
                                    editor.putBoolean("countryNoRepeat", true);
                                    editor.putBoolean("valueDialog", false);
                                }
                                editor.commit();
                                dialog.cancel();
                                getContinent();
                            }
                        })
                .setPositiveButton(str(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.doNotAskCheckBox);
                                if (checkBox.isChecked()) {
                                    editor.putBoolean("countryNoRepeat", true);
                                    editor.putBoolean("valueDialog", true);
                                }
                                editor.commit();

                                country_chosen = country_code;
                                continent_chosen = continent;
                                flag_drawable_chosen = flag;
                                country_name_chosen = country;
                                getSupportActionBar().setLogo(getResources().getDrawable(flag));
                                getSupportActionBar().setTitle(" " + country + " : " + str(R.string.sygicMapTitle));
                                finishedDetectionPart();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void noConnectionDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(str(R.string.noConnectionTitle));
        alertDialogBuilder.setIcon(R.drawable.ic_no_connection);
        alertDialogBuilder
                .setMessage(str(R.string.noConnection))
                .setCancelable(false)
                .setPositiveButton(str(R.string.quitTitle),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton(str(R.string.resetConnection),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.remove("data");
                                editor.commit();
                                Intent i = new Intent(MainActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void getContinent(){
        Intent continentIntent = new Intent(this, ContinentActivity.class);
        startActivityForResult(continentIntent, 1);
    }

    public void getCountry(){
        Intent countryIntent = new Intent(this, CountryActivity.class);
        startActivityForResult(countryIntent, 2);
    }

    public void setListAdapter(String[] maj){
        MajAdapter adapter = new MajAdapter(this, generateData(maj));
        listView.setAdapter(adapter);

        editor.putString("country_code", country_chosen);
        editor.putString("country", country_name_chosen);
        editor.putInt("flag", flag_drawable_chosen);
        editor.putString("continent", continent_chosen);
        editor.commit();
    }

    //Regarder ListStartActivity pour exemple
    public ArrayList<Items_Maj> generateData(String[] maj){
        ArrayList<Items_Maj> items = new ArrayList<>();

        for(int i = 0; i<maj.length;i++){
            String[] temp = maj[i].split("/");
            int month2Function = Integer.parseInt(temp[0]);
            int year2Function = Integer.parseInt(temp[1]);
            items.add(i, new Items_Maj(maj[i], checkBeforeAfter(year2Function,month2Function,year_maj,month_maj)));
        }

        return items;
    }

    private void haveNetworkConnection(String sharedPref) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    hasConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    if(sharedPref.equals(getResources().getStringArray(R.array.dataConnection)[0]) || sharedPref.equals("null"))
                        hasConnectedMobile = true;
        }
    }

    public boolean checkBeforeAfter(int year, int month, int defYear, int defMonth) {
        if (year > defYear) {
            return true;
        } else
            return year == defYear && month > defMonth;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean failed=false;
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            try {
            continent_chosen = data.getStringExtra("CONTINENT");
            }catch(Exception e){
                e.printStackTrace();
            }
            if(!continent_chosen.isEmpty())
                getCountry();
        }else if(requestCode==2) {
            try {
            country_chosen = data.getStringExtra("COUNTRY");
            String name_country = data.getStringExtra("NAME");
            int flagDrawable = data.getIntExtra("FLAG_DRAWABLE", 0);
            flag_drawable_chosen = flagDrawable;
            country_name_chosen = name_country;
            getSupportActionBar().setLogo(getResources().getDrawable(flagDrawable));
            getSupportActionBar().setTitle(" " + name_country + " : " + str(R.string.sygicMapTitle));
            }catch(Exception e){
                Toast.makeText(this,"Failed !",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                failed=true;
            }
            if(!failed) {
                finishedDetectionPart();
            }
        }
    }

    public void finishedDetectionPart(){
        prefix = prefixReturn(country_chosen);
        if(lastMaj()) {
            new NetworkLoop(this).execute();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(str(R.string.quitTitle));
            alertDialogBuilder.setIcon(R.drawable.ic_action_name);
            alertDialogBuilder
                    .setMessage(str(R.string.noDirMap))
                    .setCancelable(false)
                    .setPositiveButton(str(R.string.quitTitle),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public static String prefixReturn(String country_chosen){
        if(country_chosen.equals("dza")||country_chosen.equals("mar")||country_chosen.equals("tun")){
            return "nc";
        }else if(country_chosen.equals("aze")){
            return "en";
        }else if(country_chosen.equals("i01")||country_chosen.equals("i02")||country_chosen.equals("i03")||
                 country_chosen.equals("i04")||country_chosen.equals("i32")||country_chosen.equals("i07")||
                 country_chosen.equals("i08")||country_chosen.equals("i09")||country_chosen.equals("i10")||
                 country_chosen.equals("i11")||country_chosen.equals("i05")||country_chosen.equals("i06")||
                 country_chosen.equals("i12")||country_chosen.equals("i13")||country_chosen.equals("i14")||
                 country_chosen.equals("i15")||country_chosen.equals("i16")||country_chosen.equals("i17")||
                 country_chosen.equals("i18")||country_chosen.equals("i19")||country_chosen.equals("i20")||
                 country_chosen.equals("i21")||country_chosen.equals("i22")||country_chosen.equals("i31")||
                 country_chosen.equals("i23")||country_chosen.equals("i24")||country_chosen.equals("i25")||
                 country_chosen.equals("i26")||country_chosen.equals("i27")||country_chosen.equals("i28")||
                 country_chosen.equals("i29")||country_chosen.equals("i30")){
            return "mi";
        }else if(country_chosen.equals("kaz")||country_chosen.equals("isr")||country_chosen.equals("ven")){
            return "nt";
        }else if(country_chosen.equals("vnw")){
            return "im";
        }else if(country_chosen.equals("irn")){
            return "rh";
        }else if(country_chosen.equals("irq")){
            return "gt";
        }else if(country_chosen.equals("pak")){
            return "tr";
        }else if(country_chosen.equals("col")){
            return "st";
        }else{
            return "ta";
        }
    }

    public boolean lastMaj(){
        String mapsDir = DownloaderActivity.sygicSearch() + "/Sygic/Maps";
        File mapsDirFiles = new File(mapsDir);
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        String[] arrayMaps;

        if(!mapsDirFiles.exists()){
            return false;
        }else {
            for (File f : mapsDirFiles.listFiles()) {
                if (f.getName().startsWith(country_chosen + "." + prefix + ".")) {
                    arrayMaps = f.getName().split("\\.");
                    Log.i("DetectMap", f.getName() + " trouvée");
                    results.add(i, arrayMaps[3] + "/" + arrayMaps[2]);
                    i++;
                }
            }
        }
        if(results.size() > 1){
            lastMAJ.setTextColor(getResources().getColor(R.color.Error));
            lastMAJ.setText(str(R.string.duplicatedMaps));
        }else if(results.size() == 0){
            lastMAJ.setTextColor(getResources().getColor(R.color.Blue));
            lastMAJ.setText(str(R.string.noMaps));
        }else if(results.size() == 1) {
            lastMAJ.setTextColor(getResources().getColor(R.color.Finished));
            lastMAJ.setText(str(R.string.mapDetectUpdate) + " " + results.get(0));
            String[] maj = results.get(0).split("/");
            month_maj = Integer.parseInt(maj[0]);
            year_maj = Integer.parseInt(maj[1]);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public String str(int id){
        return getResources().getString(id);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(str(R.string.quitTitle));
        alertDialogBuilder.setIcon(R.drawable.ic_action_name);
        alertDialogBuilder
                .setMessage(str(R.string.quitMessage))
                .setCancelable(false)
                .setPositiveButton(str(R.string.quitTitle),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton(str(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_start clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
