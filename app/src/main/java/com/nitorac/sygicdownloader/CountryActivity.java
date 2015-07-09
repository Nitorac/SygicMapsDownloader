package com.nitorac.sygicdownloader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
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
public class CountryActivity extends ListActivity {

    private String[] country;
    private String[] country_code;
    private Integer[] flag;
    public Arrays array;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        array = new Arrays(getApplicationContext());
        otherCode();
        ListStartAdapter adapter = new ListStartAdapter(this, generateData());
        adapter.sort(new Comparator<Items>() {
            @Override
            public int compare(Items lhs, Items rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        setListAdapter(adapter);
    }

    public void otherCode(){
        switch(MainActivity.continent_chosen){
            case "Africa":
                country = array.country_af;
                country_code = array.country_code_af;
                flag = array.flag_af;
                break;
            case "Asia":
                country = array.country_as;
                country_code = array.country_code_as;
                flag = array.flag_as;
                break;
            case "Australia":
                country = array.country_aus;
                country_code = array.country_code_aus;
                flag = array.flag_aus;
                break;

            case "Europe":
                country = array.country_eu;
                country_code = array.country_code_eu;
                flag = array.flag_eu;
                break;
            case "MiddleEast":
                country = array.country_mo;
                country_code = array.country_code_mo;
                flag = array.flag_mo;
                break;
            case "NorthAmerica":
                country = array.country_adn;
                country_code = array.country_code_adn;
                flag = array.flag_adn;
                break;
            case "SouthAmerica":
                country = array.country_ads;
                country_code = array.country_code_ads;
                flag = array.flag_ads;
                break;
            default:
                country = array.error_country;
                country_code = array.error_country_code;
                flag = array.error_flag;
        }
    }

    public ArrayList<Items> generateData(){
        ArrayList<Items> items = new ArrayList<Items>();
        for(int i = 0; i< country.length;i++) {
            items.add(new Items(country[i], country_code[i], BitmapFactory.decodeResource(getResources(), flag[i])));
        }
        return items;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id){

        TextView root_code = (TextView)v.findViewById(R.id.value);
        String code = root_code.getText().toString();
        String tempArray[] = code.split(" : ");

        TextView title = (TextView)v.findViewById(R.id.label);
        String label = title.getText().toString();

        ImageView flag = (ImageView)v.findViewById(R.id.flag);

        Intent intent=new Intent();
        intent.putExtra("COUNTRY", tempArray[1]);
        intent.putExtra("FLAG", ((BitmapDrawable)flag.getDrawable()).getBitmap());
        intent.putExtra("NAME", label);
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
