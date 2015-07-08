package com.nitorac.sygicdownloader;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import com.nitorac.sygicdownloader.liststartitems.Arrays;
import com.nitorac.sygicdownloader.liststartitems.Items;

import java.util.ArrayList;

/**
 * Created by Nitorac.
 */
public class CountryActivity extends ListStartActivity {

    private String[] country;
    private String[] country_code;
    private Integer[] flag;

    @Override
    public void otherCode(){
        switch(MainActivity.continent_chosen){
            case "Africa":
                country = Arrays.country_af;
                country_code = Arrays.country_code_af;
                flag = Arrays.flag_af;
                break;
            case "Asia":
                country = Arrays.country_as;
                country_code = Arrays.country_code_as;
                flag = Arrays.flag_as;
                break;
            case "Europe":
                country = Arrays.country_eu;
                country_code = Arrays.country_code_eu;
                flag = Arrays.flag_eu;
                break;
            case "MiddleEast":
                country = Arrays.country_mo;
                country_code = Arrays.country_code_mo;
                flag = Arrays.flag_mo;
                break;
            case "NorthAmerica":
                country = Arrays.country_adn;
                country_code = Arrays.country_code_adn;
                flag = Arrays.flag_adn;
                break;
            case "SouthAmerica":
                country = Arrays.country_ads;
                country_code = Arrays.country_code_ads;
                flag = Arrays.flag_ads;
                break;
        }
    }

    @Override
    public ArrayList<Items> generateData(){
        ArrayList<Items> items = new ArrayList<Items>();
        for(int i = 0; i< country.length;i++) {
            items.add(new Items(country[i], country_code[i], BitmapFactory.decodeResource(getResources(), flag[i])));
        }
        return items;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
        Intent intent=new Intent();
        intent.putExtra("COUNTRY", country_code[position]);
        intent.putExtra("FLAG", flag[position]);
        intent.putExtra("NAME", country[position]);
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
