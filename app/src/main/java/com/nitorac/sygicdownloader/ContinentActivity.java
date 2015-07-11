package com.nitorac.sygicdownloader;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nitorac.sygicdownloader.liststartitems.Arrays;
import com.nitorac.sygicdownloader.liststartitems.Items;
import com.nitorac.sygicdownloader.liststartitems.ListStartAdapter;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Nitorac.
 */
public class ContinentActivity extends ListActivity {

    public static Bitmap fake = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    public Context c;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        c = getApplicationContext();
        ListStartAdapter adapter = new ListStartAdapter(this, generateData());
        adapter.sort(new Comparator<Items>() {
            @Override
            public int compare(Items lhs, Items rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        setListAdapter(adapter);
    }

        public ArrayList<Items> generateData(){

            Arrays array = new Arrays(c);
            ArrayList<Items> items = new ArrayList<Items>();
            for(int i = 0; i< array.continent.length;i++) {
                items.add(new Items(array.continent[i], array.continent_code[i], fake, 0));
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
}
