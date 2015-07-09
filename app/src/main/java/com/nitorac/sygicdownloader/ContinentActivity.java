package com.nitorac.sygicdownloader;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitorac.sygicdownloader.ListStartActivity;
import com.nitorac.sygicdownloader.MainActivity;
import com.nitorac.sygicdownloader.liststartitems.Arrays;
import com.nitorac.sygicdownloader.liststartitems.Items;

import java.util.ArrayList;

/**
 * Created by Nitorac.
 */
public class ContinentActivity extends ListStartActivity{

        public static Bitmap fake = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        @Override
        public ArrayList<Items> generateData(){
            ArrayList<Items> items = new ArrayList<Items>();
            for(int i = 0; i< Arrays.continent.length;i++) {
                items.add(new Items(Arrays.continent[i], Arrays.continent_code[i], fake));
            }
            return items;
        }

        @Override
        public void onListItemClick (ListView l, View v, int position, long id){
            TextView root_code = (TextView)v.findViewById(R.id.value);
            String code = root_code.getText().toString();
            String tempArray[] = code.split(" : ");
            Intent intent=new Intent();
            intent.putExtra("CONTINENT", tempArray[1]);
            setResult(2, intent);
            finish();
        }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Quitter l'application ?");
        alertDialogBuilder.setIcon(R.drawable.ic_action_name);
        alertDialogBuilder
                .setMessage("Voulez-vous vraiment quitter l'application ?")
                .setCancelable(true)
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
}
