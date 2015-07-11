package com.nitorac.sygicdownloader.downloader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nitorac.sygicdownloader.MainActivity;
import com.nitorac.sygicdownloader.R;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.ArrayList;

public class DownloaderActivity extends Activity {

    private ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 1;

    Button mStartAll, mCancelAll;

    ArrayList<ProgressBar> mProgress = new ArrayList<>();
    ArrayList<TextView> mProgressTxt = new ArrayList<>();
    public static String base_url;

    MyDownloadStatusListener myDownloadStatusListener = new MyDownloadStatusListener();

    int lastDownloadId;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    private String month_usable;
    private String year_usable;

    public String str(int id){
        return getResources().getString(id);
    }

    public static int[] txtviewids = {R.id.progressTxt1,R.id.progressTxt2,R.id.progressTxt3,R.id.progressTxt4,R.id.progressTxt5,R.id.progressTxt6,R.id.progressTxt7,R.id.progressTxt8,R.id.progressTxt9,R.id.progressTxt10,R.id.progressTxt11,R.id.progressTxt12,R.id.progressTxt13};
    public static int[] progressbarids = {R.id.progress1,R.id.progress2,R.id.progress3,R.id.progress4,R.id.progress5,R.id.progress6,R.id.progress7,R.id.progress8,R.id.progress9,R.id.progress10,R.id.progress11,R.id.progress12,R.id.progress13};

    public static String[] s = {"2dc","2dt","cam","hmp","lma","ne0","ne1","ne2","nhs","pak","pnm","poi","dbp"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            month_usable = b.getString("month");
            year_usable = b.getString("year");
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        boolean autoDownload = prefs.getBoolean("startAutoDownload", false);
        base_url = "http://cdn.sygic.com/in-app-data/maps/" + MainActivity.continent_chosen + "/" + MainActivity.prefix + "." + year_usable + "." + month_usable + "/" + MainActivity.country_chosen + "." + MainActivity.prefix + "." + year_usable + "." + month_usable + "/" + MainActivity.country_chosen + ".";

        mStartAll = (Button) findViewById(R.id.button5);
        mCancelAll = (Button) findViewById(R.id.button6);


        for(int i = 0;i<txtviewids.length;i++){
            mProgressTxt.add(i, (TextView) findViewById(txtviewids[i]));
            mProgress.add(i, (ProgressBar) findViewById(progressbarids[i]));
        }

        for(int i = 0;i<mProgress.size();i++) {
            mProgress.get(i).setMax(100);
            mProgress.get(i).setProgress(0);
        }

        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);

        String filesDir = sygicSearch() + "/Sygic/Maps";
        final File filesDirFile = new File(filesDir);

        if (!filesDirFile.isDirectory()) {
            filesDirFile.mkdir();
        }

        final File mapDir = new File(filesDir + "/" + MainActivity.country_chosen + "." + MainActivity.prefix + "." + year_usable + "." + month_usable);

        for(int i = 0; i<mProgressTxt.size();i++) {
            mProgressTxt.get(i).setText(str(R.string.waiting) + " | " + MainActivity.country_chosen + "." + s[i] + " : ...");
        }

        Log.i("RootPath", sygicSearch());

        if(!autoDownload) {
            editor.putBoolean("startAutoDownload", false);
            editor.commit();
            mStartAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStartAll.setEnabled(false);
                    startDownload(mapDir, filesDirFile);
                }
            });
        }else{
            mStartAll.setEnabled(false);
            startDownload(mapDir, filesDirFile);
            Toast.makeText(DownloaderActivity.this, str(R.string.downloadAuto), Toast.LENGTH_LONG).show();
        }
        mCancelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadManager.cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String errorMessage = str(R.string.downloadCanceled);

                for (int i = 0; i < mProgressTxt.size(); i++) {
                    mProgressTxt.get(i).setText(str(R.string.canceled) + " | " + MainActivity.country_chosen + "." + s[i] + " : " + errorMessage);
                    mProgressTxt.get(i).setTextColor(getResources().getColor(R.color.ErrorNormale));
                }
                for (int i = 0; i < mProgress.size(); i++) {
                    mProgress.get(i).setProgress(0);
                }

                mapDir.setWritable(true);
                Log.i("IfDeleted", "" + deleteDir(mapDir));
                Intent intent = new Intent(DownloaderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void startDownload(File mapDir, File filesDirFile){
        try {
            downloadManager.cancelAll();

            for (File f : filesDirFile.listFiles()) {
                if (f.getName().startsWith(MainActivity.country_chosen + "." + MainActivity.prefix + ".")) {
                    try {
                        f.setWritable(true);
                        boolean deleted = deleteDir(f);
                        Log.i("AncMap", f.getName() + " deleted" + " ! " + deleted);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            mapDir.mkdir();
            for (int i = 0; i < s.length; i++) {
                lastDownloadId = downloadManager.add(getDownloadRequest(i, filesDirFile));
            }
            for (int i = 0; i < mProgressTxt.size(); i++) {
                mProgressTxt.get(i).setText(str(R.string.downloadStarting) + " | " + MainActivity.country_chosen + "." + s[i] + " : Initialisation ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DownloadRequest getDownloadRequest(int boucle, File filesDirFile){
        RetryPolicy retryPolicy = new DefaultRetryPolicy();

        final File mapDir = new File(filesDirFile.getPath() + "/" + MainActivity.country_chosen + "." + MainActivity.prefix + "." + year_usable + "." + month_usable);

        String mapDirCountry = mapDir.getPath() + "/" + MainActivity.country_chosen + ".";

            Uri downloadUri = Uri.parse(base_url + s[boucle]);
            Uri destinationUri = Uri.parse(mapDirCountry + s[boucle]);

        return new DownloadRequest(downloadUri)
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                .setDownloadListener(myDownloadStatusListener);
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
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

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    public static String sygicSearch() {
        File root_internal = new File(Environment.getExternalStorageDirectory().getPath());
        File root_internal_s = new File(Environment.getExternalStorageDirectory().getPath() + "/Sygic/Maps");
        File root_external_1 = new File("/mnt/external_sd");
        File root_external_2 = new File("/mnt/extSdCard");
        File root_external_1_s = new File("/mnt/external_sd" + "/Sygic/Maps");
        File root_external_2_s = new File("/mnt/extSdCard" + "/Sygic/Maps");

        if (root_internal_s.isDirectory()) {
            return root_internal.getPath();
        } else {
            if (root_external_1_s.isDirectory()) {
                return root_external_1.getPath();
            } else if (root_external_2_s.isDirectory()) {
                return root_external_2.getPath();
            } else {
                return root_internal.getPath();
            }
        }
    }

    public void showAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(str(R.string.downloadSuccess));
        alertDialogBuilder.setIcon(R.drawable.ic_finished);
        alertDialogBuilder
                .setMessage(str(R.string.downloadSuccessMessage))
                .setCancelable(false)
                .setPositiveButton(str(R.string.startSygic),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startNewActivity(DownloaderActivity.this, "com.sygic.aura");
                                finish();
                            }
                        })

                .setNegativeButton(str(R.string.quitTitle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /******************************Changement de class !! **********************/

    class MyDownloadStatusListener implements DownloadStatusListener {

        @Override
        public void onDownloadComplete(int id) {
            mProgressTxt.get(id - 1).setText(MainActivity.country_chosen + "." + s[id - 1] + " : " + str(R.string.finished));
            mProgressTxt.get(id - 1).setTextColor(getResources().getColor(R.color.Finished));

            if (id == lastDownloadId) {
                DownloaderActivity.this.showAlert();
            }
        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            if(errorCode == DownloadManager.ERROR_UNHANDLED_HTTP_CODE) {
                mProgressTxt.get(id - 1).setText(str(R.string.expectedError) + " | " + MainActivity.country_chosen + "." + s[id - 1] + " (" + errorMessage + str(R.string.commonCountries) +")");
                mProgressTxt.get(id - 1).setTextColor(getResources().getColor(R.color.ErrorNormale));
                mProgress.get(id - 1).setProgress(100);
            }else{
                mProgressTxt.get(id - 1).setText(str(R.string.unexpectedError) + " | " + MainActivity.country_chosen + "." + s[id - 1] + " : " + errorCode + " ; " + errorMessage);
                mProgressTxt.get(id - 1).setTextColor(getResources().getColor(R.color.Error));
                mProgress.get(id - 1).setProgress(0);
            }

            if (id == lastDownloadId) {
                DownloaderActivity.this.showAlert();
            }
        }

        @Override
        public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
                mProgressTxt.get(id-1).setText(MainActivity.country_chosen + "." + s[id-1] + " : " + progress + " % " + "" + getBytesDownloaded(progress, totalBytes));
                mProgress.get(id-1).setProgress(progress);
        }

        private String getBytesDownloaded(int progress, long totalBytes) {
            //Greater than 1 MB
            long bytesCompleted = (progress * totalBytes) / 100;
            if (totalBytes >= 1000000) {
                return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + " Mo");
            }
            if (totalBytes >= 1000) {
                return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + " Ko");

            } else {
                return ("" + bytesCompleted + "/" + totalBytes);
            }
        }
    }
}