package com.nitorac.sygicdownloader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nitorac.sygicdownloader.downloader.DownloaderActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nitorac.
 */
public class NetworkLoop extends AsyncTask<Void, Void, String[]> {

    public NetworkLoop(MainActivity ma) {
        this.screen = ma;
        this.progress = new ProgressDialog(this.screen);
    }

    private volatile MainActivity screen;
    private ProgressDialog progress;

    public String str(int id){return screen.getResources().getString(id);}

    @Override
    protected void onPreExecute() {
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.setMessage(str(R.string.updateGetting));
        this.progress.setIndeterminate(true);
        this.progress.setCanceledOnTouchOutside(false);
        this.progress.setCancelable(false);
        this.progress.show();
    }

    @Override
    protected String[] doInBackground(Void... params) {
        DateFormat monthStart = new SimpleDateFormat("MM");
        DateFormat yearStart = new SimpleDateFormat("yyyy");
        int nowMonth = Integer.parseInt(monthStart.format(Calendar.getInstance().getTime()));
        //java.text.MessageFormat.format("{0,number,#00}", month);
        int nowYear = Integer.parseInt(yearStart.format(Calendar.getInstance().getTime()));
        boolean stopLoop = false;
        List<String> finalArray = new ArrayList<String>();

        int month = nowMonth;
        int year = nowYear;
        int i = 0;

        while(month<=12 && !stopLoop){

            String strMonth = java.text.MessageFormat.format("{0,number,#00}", month);
            String url = "http://cdn.sygic.com/in-app-data/maps/" + MainActivity.continent_chosen + "/"+ MainActivity.prefix +"." + String.valueOf(year) + "." + strMonth + "/" + MainActivity.country_chosen + "." + MainActivity.prefix + "." + String.valueOf(year) + "." + strMonth + "/" + MainActivity.country_chosen + ".pak";

            if(month == screen.txtMonth && year == screen.txtYear){
                stopLoop = true;
            }

            if(isURLReachable(url)){
                finalArray.add(strMonth + "/" + String.valueOf(year));
                i+=1;
            }

            if(month == 1){
                month = 12;
                year -= 1;
            }
            month-=1;
        }
        String[] returnArray = new String[finalArray.size()];
        returnArray = finalArray.toArray(returnArray);
        return returnArray;
    }

    static public boolean isURLReachable(String urlToTest) {
        try {
            URL url = new URL(urlToTest);   // Change to "http://google.com" for www  test.
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(10 * 1000);          // 10 s.
            urlc.connect();
            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                Log.wtf("Connection", "Réussie !");
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e1) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if(this.progress.isShowing()) this.progress.dismiss();
        this.screen.setListAdapter(result);
        this.screen.maj_list = result;
        this.screen.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                TextView txtView = (TextView) container.findViewById(R.id.maj_label);
                final String maj = txtView.getText().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(screen);
                alertDialogBuilder.setTitle(str(R.string.confirmation));
                alertDialogBuilder.setIcon(R.drawable.ic_warning);
                alertDialogBuilder
                        .setMessage(String.format(str(R.string.updateMessage),maj))
                        .setCancelable(true)
                        .setPositiveButton(str(R.string.download),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String[] date_arr = maj.split("/");

                                        Intent intent = new Intent(screen, DownloaderActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("month", date_arr[0]);
                                        b.putString("year", date_arr[1]);
                                        intent.putExtras(b); //Put your id to your next Intent
                                        screen.startActivity(intent);
                                        screen.finish();
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
        });
    }
}