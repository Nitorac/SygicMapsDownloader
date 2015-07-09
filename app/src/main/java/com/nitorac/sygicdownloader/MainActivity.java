package com.nitorac.sygicdownloader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    public static int month_maj = 0;
    public static int year_maj = 0;
    public String[] maj_list;

    public String pass_country;
    public String pass_country_code;

    public ListView listView;

    public TextView lastMAJ;

    public int txtMonth = 1;
    public int txtYear = 2013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        txtMonth = Integer.parseInt(prefs.getString("month", "01"));
        txtYear = Integer.parseInt(prefs.getString("year", "2013"));

        listView = (ListView) findViewById(R.id.listView);

        lastMAJ = (TextView) findViewById(R.id.lastMaj);

        Bundle b = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();

            getContinent();
            actionBar.setLogo(R.mipmap.ic_launcher);

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

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

    public boolean checkBeforeAfter(int year, int month, int defYear, int defMonth) {
            if(year > defYear){
                return true;
            }else if(year == defYear){
                return month > defMonth;
            }else{
                return false;
            }
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
            Bitmap flagIcon = data.getParcelableExtra("FLAG");
            String name_country = data.getStringExtra("NAME");

            pass_country = name_country;
            pass_country_code = country_chosen;

            getSupportActionBar().setLogo(new BitmapDrawable(getResources(), flagIcon));
            getSupportActionBar().setTitle("  " + name_country + " : Cartes Sygic");
            }catch(Exception e){
                Toast.makeText(this,"Failed !",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                failed=true;
            }
            if(!failed) {
                prefix = prefixReturn(country_chosen);
                lastMaj();
                new NetworkLoop(this).execute();
            }
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

    public void lastMaj(){
        String mapsDir = DownloaderActivity.sygicSearch() + "/Sygic/Maps";
        File mapsDirFiles = new File(mapsDir);
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        String[] arrayMaps;

        for (File f : mapsDirFiles.listFiles()) {
            if (f.getName().startsWith(country_chosen + "."+prefix+".")) {
                arrayMaps = f.getName().split("\\.");
                Log.i("DetectMap", f.getName() + " trouvée");
                results.add(i, arrayMaps[3] + "/" + arrayMaps[2]);
                i++;
            }
        }
        if(results.size() > 1){
            lastMAJ.setTextColor(getResources().getColor(R.color.Error));
            lastMAJ.setText("Plus de 2 cartes du même pays !");
        }else if(results.size() == 0){
            lastMAJ.setTextColor(getResources().getColor(R.color.Blue));
            lastMAJ.setText("Aucune carte existante de ce pays");
        }else if(results.size() == 1) {
            lastMAJ.setTextColor(getResources().getColor(R.color.Finished));
            lastMAJ.setText("Date de la carte : " + results.get(0));
            String[] maj = results.get(0).split("/");
            month_maj = Integer.parseInt(maj[0]);
            year_maj = Integer.parseInt(maj[1]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Quitter l'application ?");
        alertDialogBuilder.setIcon(R.drawable.ic_action_name);
        alertDialogBuilder
                .setMessage("Voulez-vous vraiment quitter l'application ?")
                .setCancelable(false)
                .setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
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
