package com.nitorac.sygicdownloader.downloader;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nitorac.sygicdownloader.MainActivity;
import com.nitorac.sygicdownloader.R;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

public class DownloaderActivity extends Activity {

    private ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 1;

    Button mStartAll,mCancelAll;

    ProgressBar mProgress1,mProgress2,mProgress3,mProgress4,mProgress5,mProgress6,mProgress7,mProgress8,mProgress9,mProgress10,mProgress11,mProgress12,mProgress13;
    TextView mProgress1Txt,mProgress2Txt,mProgress3Txt,mProgress4Txt,mProgress5Txt,mProgress6Txt,mProgress7Txt,mProgress8Txt,mProgress9Txt,mProgress10Txt,mProgress11Txt,mProgress12Txt,mProgress13Txt;

    public static String base_url;

    MyDownloadStatusListener myDownloadStatusListener = new MyDownloadStatusListener();

    int downloadId1;
    int downloadId2;
    int downloadId3;
    int downloadId4;
    int downloadId5;
    int downloadId6;
    int downloadId7;
    int downloadId8;
    int downloadId9;
    int downloadId10;
    int downloadId11;
    int downloadId12;
    int DownloadId13;

    private String month_usable;
    private String year_usable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            month_usable = b.getString("month");
            year_usable = b.getString("year");
        }

        base_url = "http://cdn.sygic.com/in-app-data/maps/" + MainActivity.continent_chosen + "/"+MainActivity.prefix+"." + year_usable + "." + month_usable + "/" + MainActivity.country_chosen + "."+MainActivity.prefix+"." + year_usable + "." + month_usable + "/" + MainActivity.country_chosen + ".";

        final String FILE1 = base_url + "2dc";
        final String FILE2 = base_url + "2dt";
        final String FILE3 = base_url + "cam";
        final String FILE4 = base_url + "hmp";
        final String FILE5 = base_url + "lma";
        final String FILE6 = base_url + "ne0";
        final String FILE7 = base_url + "ne1";
        final String FILE8 = base_url + "ne2";
        final String FILE9 = base_url + "nhs";
        final String FILE10 = base_url + "pak";
        final String FILE11 = base_url + "pnm";
        final String FILE12 = base_url + "poi";
        final String FILE13 = base_url + "dbp";

        mStartAll = (Button) findViewById(R.id.button5);
        mCancelAll = (Button) findViewById(R.id.button6);

        mProgress1Txt = (TextView) findViewById(R.id.progressTxt1);
        mProgress2Txt = (TextView) findViewById(R.id.progressTxt2);
        mProgress3Txt = (TextView) findViewById(R.id.progressTxt3);
        mProgress4Txt = (TextView) findViewById(R.id.progressTxt4);
        mProgress5Txt = (TextView) findViewById(R.id.progressTxt5);
        mProgress6Txt = (TextView) findViewById(R.id.progressTxt6);
        mProgress7Txt = (TextView) findViewById(R.id.progressTxt7);
        mProgress8Txt = (TextView) findViewById(R.id.progressTxt8);
        mProgress9Txt = (TextView) findViewById(R.id.progressTxt9);
        mProgress10Txt = (TextView) findViewById(R.id.progressTxt10);
        mProgress11Txt = (TextView) findViewById(R.id.progressTxt11);
        mProgress12Txt = (TextView) findViewById(R.id.progressTxt12);
        mProgress13Txt = (TextView) findViewById(R.id.progressTxt13);

        mProgress1 = (ProgressBar) findViewById(R.id.progress1);
        mProgress2 = (ProgressBar) findViewById(R.id.progress2);
        mProgress3 = (ProgressBar) findViewById(R.id.progress3);
        mProgress4 = (ProgressBar) findViewById(R.id.progress4);
        mProgress5 = (ProgressBar) findViewById(R.id.progress5);
        mProgress6 = (ProgressBar) findViewById(R.id.progress6);
        mProgress7 = (ProgressBar) findViewById(R.id.progress7);
        mProgress8 = (ProgressBar) findViewById(R.id.progress8);
        mProgress9 = (ProgressBar) findViewById(R.id.progress9);
        mProgress10 = (ProgressBar) findViewById(R.id.progress10);
        mProgress11 = (ProgressBar) findViewById(R.id.progress11);
        mProgress12 = (ProgressBar) findViewById(R.id.progress12);
        mProgress13 = (ProgressBar) findViewById(R.id.progress13);

        mProgress1.setMax(100);
        mProgress1.setProgress(0);

        mProgress2.setMax(100);
        mProgress2.setProgress(0);

        mProgress3.setMax(100);
        mProgress3.setProgress(0);

        mProgress4.setMax(100);
        mProgress4.setProgress(0);

        mProgress5.setMax(100);
        mProgress5.setProgress(0);

        mProgress6.setMax(100);
        mProgress6.setProgress(0);

        mProgress7.setMax(100);
        mProgress7.setProgress(0);

        mProgress8.setMax(100);
        mProgress8.setProgress(0);

        mProgress9.setMax(100);
        mProgress9.setProgress(0);

        mProgress10.setMax(100);
        mProgress10.setProgress(0);

        mProgress11.setMax(100);
        mProgress11.setProgress(0);

        mProgress12.setMax(100);
        mProgress12.setProgress(0);

        mProgress13.setMax(100);
        mProgress13.setProgress(0);

        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        RetryPolicy retryPolicy = new DefaultRetryPolicy();

        String filesDir = sygicSearch() + "/Sygic/Maps";
        final File filesDirFile = new File(filesDir);

        if(!filesDirFile.isDirectory()){
            filesDirFile.mkdir();
        }

        final File mapDir = new File(filesDir + "/" + MainActivity.country_chosen + "."+MainActivity.prefix+"." + year_usable + "." + month_usable);

        String mapDirCountry = mapDir.getPath() + "/" + MainActivity.country_chosen + ".";

        Uri downloadUri = Uri.parse(FILE1);
        Uri destinationUri = Uri.parse(mapDirCountry+"2dc");
        final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE2);
        destinationUri = Uri.parse(mapDirCountry+"2dt");
        final DownloadRequest downloadRequest2 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE3);
        destinationUri = Uri.parse(mapDirCountry+"cam");
        final DownloadRequest downloadRequest3 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE4);
        destinationUri = Uri.parse(mapDirCountry+"hmp");
        final DownloadRequest downloadRequest4 = new DownloadRequest(downloadUri)
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE5);
        destinationUri = Uri.parse(mapDirCountry+"lma");
        final DownloadRequest downloadRequest5 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE6);
        destinationUri = Uri.parse(mapDirCountry+"ne0");
        final DownloadRequest downloadRequest6 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE7);
        destinationUri = Uri.parse(mapDirCountry+"ne1");
        final DownloadRequest downloadRequest7 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE8);
        destinationUri = Uri.parse(mapDirCountry+"ne2");
        final DownloadRequest downloadRequest8 = new DownloadRequest(downloadUri)
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setDownloadListener(myDownloadStatusListener);


        downloadUri = Uri.parse(FILE9);
        destinationUri = Uri.parse(mapDirCountry+"nhs");
        final DownloadRequest downloadRequest9 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE10);
        destinationUri = Uri.parse(mapDirCountry+"pak");
        final DownloadRequest downloadRequest10 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE11);
        destinationUri = Uri.parse(mapDirCountry+"pnm");
        final DownloadRequest downloadRequest11 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setRetryPolicy(retryPolicy)
                .setDownloadListener(myDownloadStatusListener);

        downloadUri = Uri.parse(FILE12);
        destinationUri = Uri.parse(mapDirCountry+"poi");
        final DownloadRequest downloadRequest12 = new DownloadRequest(downloadUri)
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setDownloadListener(myDownloadStatusListener);

        mStartAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadManager.cancelAll();

                    for (File f : filesDirFile.listFiles()) {
                        if (f.getName().startsWith(MainActivity.country_chosen + "." + MainActivity.prefix + ".")) {
                            try {
                                f.setWritable(true);
                                boolean deleted = deleteDir(f);
                                Log.i("AncMap", f.getName() + " supprimé" + " ! " + deleted);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    mapDir.mkdir();

                    downloadId1 = downloadManager.add(downloadRequest1);
                    downloadId2 = downloadManager.add(downloadRequest2);
                    downloadId3 = downloadManager.add(downloadRequest3);
                    downloadId4 = downloadManager.add(downloadRequest4);
                    downloadId5 = downloadManager.add(downloadRequest5);
                    downloadId6 = downloadManager.add(downloadRequest6);
                    downloadId7 = downloadManager.add(downloadRequest7);
                    downloadId8 = downloadManager.add(downloadRequest8);
                    downloadId9 = downloadManager.add(downloadRequest9);
                    downloadId10 = downloadManager.add(downloadRequest10);
                    downloadId11 = downloadManager.add(downloadRequest11);
                    downloadId12 = downloadManager.add(downloadRequest12);

                    mProgress1Txt.setText("Début | " + MainActivity.country_chosen + ".2dc" + " : Initialisation ...");
                    mProgress2Txt.setText("Début | " + MainActivity.country_chosen + ".2dt" + " : Initialisation ...");
                    mProgress3Txt.setText("Début | " + MainActivity.country_chosen + ".cam" + " : Initialisation ...");
                    mProgress4Txt.setText("Début | " + MainActivity.country_chosen + ".hmp" + " : Initialisation ...");
                    mProgress5Txt.setText("Début | " + MainActivity.country_chosen + ".lma" + " : Initialisation ...");
                    mProgress6Txt.setText("Début | " + MainActivity.country_chosen + ".ne0" + " : Initialisation ...");
                    mProgress7Txt.setText("Début | " + MainActivity.country_chosen + ".ne1" + " : Initialisation ...");
                    mProgress8Txt.setText("Début | " + MainActivity.country_chosen + ".ne2" + " : Initialisation ...");
                    mProgress9Txt.setText("Début | " + MainActivity.country_chosen + ".nhs" + " : Initialisation ...");
                    mProgress10Txt.setText("Début | " + MainActivity.country_chosen + ".pak" + " : Initialisation ...");
                    mProgress11Txt.setText("Début | " + MainActivity.country_chosen + ".pnm" + " : Initialisation ...");
                    mProgress12Txt.setText("Début | " + MainActivity.country_chosen + ".poi" + " : Initialisation ...");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        mCancelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    downloadManager.cancelAll();
                }catch(Exception e){}
                String errorCode = "1001";
                String errorMessage = "Téléchargement annulé";
                    mProgress1Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".2dc" + " : " + errorCode + ", " + errorMessage);
                    mProgress1.setProgress(0);
                    mProgress1Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress2Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".2dt" + " : " + errorCode + ", " + errorMessage);
                    mProgress2.setProgress(0);
                    mProgress2Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress3Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".cam" + " : " + errorCode + ", " + errorMessage);
                    mProgress3.setProgress(0);
                    mProgress3Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress4Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".hmp" + " : " + errorCode + ", " + errorMessage);
                    mProgress4.setProgress(0);
                    mProgress4Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress5Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".lma" + " : " + errorCode + ", " + errorMessage);
                    mProgress5.setProgress(0);
                    mProgress5Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress6Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne0" + " : " + errorCode + ", " + errorMessage);
                    mProgress6.setProgress(0);
                    mProgress6Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress7Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne1" + " : " + errorCode + ", " + errorMessage);
                    mProgress7.setProgress(0);
                    mProgress7Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress8Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne2" + " : " + errorCode + ", " + errorMessage);
                    mProgress8.setProgress(0);
                    mProgress8Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress9Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".nhs" + " : " + errorCode + ", " + errorMessage);
                    mProgress9.setProgress(0);
                    mProgress9Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress10Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".pak" + " : " + errorCode + ", " + errorMessage);
                    mProgress10.setProgress(0);
                    mProgress10Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress11Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".pnm" + " : " + errorCode + ", " + errorMessage);
                    mProgress1.setProgress(0);
                    mProgress11Txt.setTextColor(getResources().getColor(R.color.Error));
                    mProgress12Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".poi" + " : " + errorCode + ", " + errorMessage);
                    mProgress12.setProgress(0);
                    mProgress12Txt.setTextColor(getResources().getColor(R.color.Error));
                mapDir.setWritable(true);
                Log.i("IfDeleted", "" + deleteDir(mapDir));
                Intent intent = new Intent(DownloaderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mProgress1Txt.setText("En attente | " + MainActivity.country_chosen + ".2dc" + " : ...");
        mProgress2Txt.setText("En attente | " + MainActivity.country_chosen + ".2dt" + " : ...");
        mProgress3Txt.setText("En attente | " + MainActivity.country_chosen + ".cam" + " : ...");
        mProgress4Txt.setText("En attente | " + MainActivity.country_chosen + ".hmp" + " : ...");
        mProgress5Txt.setText("En attente | " + MainActivity.country_chosen + ".lma" + " : ...");
        mProgress6Txt.setText("En attente | " + MainActivity.country_chosen + ".ne0" + " : ...");
        mProgress7Txt.setText("En attente | " + MainActivity.country_chosen + ".ne1" + " : ...");
        mProgress8Txt.setText("En attente | " + MainActivity.country_chosen + ".ne2" + " : ...");
        mProgress9Txt.setText("En attente | " + MainActivity.country_chosen + ".nhs" + " : ...");
        mProgress10Txt.setText("En attente | " + MainActivity.country_chosen + ".pak" + " : ...");
        mProgress11Txt.setText("En attente | " + MainActivity.country_chosen + ".pnm" + " : ...");
        mProgress12Txt.setText("En attente | " + MainActivity.country_chosen + ".poi" + " : ...");

        Log.i("RootPath", sygicSearch());
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    // return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadManager.release();
    }

    public static String sygicSearch(){
        File root_internal = new File(Environment.getExternalStorageDirectory().getPath());
        File root_internal_s = new File(Environment.getExternalStorageDirectory().getPath() + "/Sygic/Maps");
        File root_external_1 = new File("/mnt/external_sd");
        File root_external_2 = new File("/mnt/extSdCard");
        File root_external_1_s = new File("/mnt/external_sd" + "/Sygic/Maps");
        File root_external_2_s = new File("/mnt/extSdCard" + "/Sygic/Maps");

        if(root_internal_s.isDirectory()){
            return root_internal.getPath();
        }else{
            if(root_external_1_s.isDirectory()){
                return root_external_1.getPath();
            }else if(root_external_2_s.isDirectory()){
                return root_external_2.getPath();
            }else{
                return "Error";
            }
        }
    }

    class MyDownloadStatusListener implements DownloadStatusListener {

        @Override
        public void onDownloadComplete(int id) {

            if(id == downloadId1) {mProgress1Txt.setText(MainActivity.country_chosen + ".2dc" + " : Terminé !");mProgress1Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId2) {mProgress2Txt.setText(MainActivity.country_chosen + ".2dt" + " : Terminé !");mProgress2Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId3) {mProgress3Txt.setText(MainActivity.country_chosen + ".cam" + " : Terminé !");mProgress3Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId4) {mProgress4Txt.setText(MainActivity.country_chosen + ".hmp" + " : Terminé !");mProgress4Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId5) {mProgress5Txt.setText(MainActivity.country_chosen + ".lma" + " : Terminé !");mProgress5Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId6) {mProgress6Txt.setText(MainActivity.country_chosen + ".ne0" + " : Terminé !");mProgress6Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId7) {mProgress7Txt.setText(MainActivity.country_chosen + ".ne1" + " : Terminé !");mProgress7Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId8) {mProgress8Txt.setText(MainActivity.country_chosen + ".ne2" + " : Terminé !");mProgress8Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId9) {mProgress9Txt.setText(MainActivity.country_chosen + ".nhs" + " : Terminé !");mProgress9Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId10) {mProgress10Txt.setText(MainActivity.country_chosen + ".pak" + " : Terminé !");mProgress10Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId11) {mProgress11Txt.setText(MainActivity.country_chosen + ".pnm" + " : Terminé !");mProgress11Txt.setTextColor(getResources().getColor(R.color.Finished));
            }else if(id == downloadId12) {mProgress12Txt.setText(MainActivity.country_chosen + ".poi" + " : Terminé !");mProgress12Txt.setTextColor(getResources().getColor(R.color.Finished));
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DownloaderActivity.this);
                alertDialogBuilder.setTitle("Téléchargement réussi");
                alertDialogBuilder.setIcon(R.drawable.ic_finished);
                alertDialogBuilder
                        .setMessage("Le téléchargement s'est terminé avec succès.\nVoulez-vous quitter l'application ?")
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
                                dialog.dismiss();
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            if(id == downloadId1) {
                mProgress1Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".2dc");
                mProgress1.setProgress(0);
                mProgress1Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId2) {
                mProgress2Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".2dt");
                mProgress2.setProgress(0);
                mProgress2Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId3) {
                mProgress3Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".cam");
                mProgress3.setProgress(0);
                mProgress3Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId4) {
                mProgress4Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".hmp");
                mProgress4.setProgress(0);
                mProgress4Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId5) {
                mProgress5Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".lma");
                mProgress5.setProgress(0);
                mProgress5Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId6) {
                mProgress6Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne0");
                mProgress6.setProgress(0);
                mProgress6Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId7) {
                mProgress7Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne1");
                mProgress7.setProgress(0);
                mProgress7Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId8) {
                mProgress8Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".ne2");
                mProgress8.setProgress(0);
                mProgress8Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId9) {
                mProgress9Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".nhs");
                mProgress9.setProgress(0);
                mProgress9Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId10) {
                mProgress10Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".pak");
                mProgress10.setProgress(0);
                mProgress10Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId11) {
                mProgress11Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".pnm");
                mProgress1.setProgress(0);
                mProgress11Txt.setTextColor(getResources().getColor(R.color.Error));
            }else if(id == downloadId12) {
                mProgress12Txt.setText("Erreur Normale | " + MainActivity.country_chosen + ".poi");
                mProgress12.setProgress(0);
                mProgress12Txt.setTextColor(getResources().getColor(R.color.Error));
            }
        }

        @Override
        public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
            if(id == downloadId1){
                mProgress1Txt.setText(MainActivity.country_chosen + ".2dc" + " : " + progress + " % " + "" + getBytesDownloaded(progress,totalBytes));
                mProgress1.setProgress(progress);
            }else if(id == downloadId2){
                mProgress2Txt.setText(MainActivity.country_chosen + ".2dt" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress2.setProgress(progress);
            }else if (id == downloadId3){
                mProgress3Txt.setText(MainActivity.country_chosen + ".cam" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress3.setProgress(progress);
            }else if (id == downloadId4){
                mProgress4Txt.setText(MainActivity.country_chosen + ".hmp" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress4.setProgress(progress);
            }else if(id == downloadId5){
                mProgress5Txt.setText(MainActivity.country_chosen + ".lma" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress5.setProgress(progress);
            }else if (id == downloadId6){
                mProgress6Txt.setText(MainActivity.country_chosen + ".ne0" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress6.setProgress(progress);
            }else if (id == downloadId7){
                mProgress7Txt.setText(MainActivity.country_chosen + ".ne1" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress7.setProgress(progress);
            }else if (id == downloadId8){
                mProgress8Txt.setText(MainActivity.country_chosen + ".ne2" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress8.setProgress(progress);
            }else if(id == downloadId9){
                mProgress9Txt.setText(MainActivity.country_chosen + ".nhs" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress9.setProgress(progress);
            }else if (id == downloadId10){
                mProgress10Txt.setText(MainActivity.country_chosen + ".pak" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress10.setProgress(progress);
            }else if (id == downloadId11){
                mProgress11Txt.setText(MainActivity.country_chosen + ".pnm" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress11.setProgress(progress);
            }else if (id == downloadId12){
                mProgress12Txt.setText(MainActivity.country_chosen + ".poi" + " : " + progress + " % " + "  " + getBytesDownloaded(progress,totalBytes));
                mProgress12.setProgress(progress);
            }
        }
    }

    private String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted = (progress * totalBytes)/100;
        if (totalBytes >= 1000000) {
            return (""+(String.format("%.1f", (float)bytesCompleted/1000000))+ "/"+ ( String.format("%.1f", (float)totalBytes/1000000)) + " Mo");
        } if (totalBytes >= 1000) {
            return (""+(String.format("%.1f", (float)bytesCompleted/1000))+ "/"+ ( String.format("%.1f", (float)totalBytes/1000)) + " Ko");

        } else {
            return ( ""+bytesCompleted+"/"+totalBytes );
        }
    }
}