package com.nitorac.sygicdownloader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nitorac.
 */
public class Updater {

    private Activity act;

    public Updater(Activity act){
        this.act = act;
    }

    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;

    String curVersion;
    String newVersion;

    public boolean isUpdateAvailable(String versionName){
        curVersion = str(R.string.versionName);
        newVersion = versionName;

        String[] features_detected = newVersion.split("\\.");
        String[] features_current = curVersion.split("\\.");

        int major_detected = Integer.parseInt(features_detected[0]);
        int minor_detected = Integer.parseInt(features_detected[1]);

        int major_current = Integer.parseInt(features_current[0]);
        int minor_current = Integer.parseInt(features_current[1]);

        if(major_detected > major_current){
            return true;
        }else if(major_detected == major_current && minor_detected > minor_current){
            return true;
        }
        return false;
    }

    public void updaterMain(final String filePath, final String download_url, String check_url_txt){
        DetectUpdateTask dut = new DetectUpdateTask();
        dut.execute(filePath,download_url,check_url_txt);
    }

    public void updateDL(final String filePath, final String download_url, String versionName) {
        if(isUpdateAvailable(versionName)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
            alertDialogBuilder.setTitle(str(R.string.updateTitle));
            alertDialogBuilder.setIcon(R.drawable.ic_program_update);
            alertDialogBuilder
                    .setMessage(String.format(str(R.string.updatePrgramMessage),curVersion,newVersion))
                    .setCancelable(false)
                    .setPositiveButton(str(R.string.download),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showProgress(filePath);

                                    new Thread(new Runnable() {
                                        public void run() {
                                            downloadFile(filePath, download_url);
                                        }
                                    }).start();
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
    }

    void downloadFile(final String filePath,final String download_url){

        try {
            URL url = new URL(download_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();
            //set the path where we want to save the file
            //create a new file, to save the downloaded file
            File file = new File(filePath);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            act.runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float) downloadedSize / totalSize) * 100;
                        //cur_val.setText("Downloaded " + downloadedSize + "Ko / " + totalSize + "Ko (" + (int)per + "%)" );
                        cur_val.setText(String.format(str(R.string.updateProgress), downloadedSize, totalSize, (int)per));
                    }
                });
            }
            //close the output stream when complete //

            fileOutput.close();

            act.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                    intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    act.startActivity(intent);
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err){
        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(act, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path){
        dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle(str(R.string.downloading) + " ...");
        dialog.setCancelable(false);

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText(str(R.string.downloadFileFrom) +" "+ file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText(str(R.string.startingDownload));
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
    }

    public String str(int id){return act.getResources().getString(id);}

    private class DetectUpdateTask extends AsyncTask<String, Void, String>
    {
        public String file = "";
        public String download = "";

        @Override
        protected String doInBackground(String... params) {
            file = params[0];
            download = params[1];
            String returnStr = "0.0";
            try {
                URL url = new URL(params[2]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    returnStr = str;
                }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return returnStr;
        }

        @Override
        protected void onPostExecute(String result){
            updateDL(file,download,result);
        }
    }
}
