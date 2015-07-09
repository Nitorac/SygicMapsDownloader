package com.nitorac.sygicdownloader;

import android.app.ListActivity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.nitorac.sygicdownloader.liststartitems.Items;
import com.nitorac.sygicdownloader.liststartitems.ListStartAdapter;
import com.nitorac.sygicdownloader.liststartitems.Arrays;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Nitorac.
 */
public class ListStartActivity extends ListActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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

    }

    public ArrayList<Items> generateData(){
        ArrayList<Items> items = new ArrayList<Items>();
        for(int i = 0; i< Arrays.country_eu.length;i++) {
            items.add(new Items(Arrays.country_eu[i], Arrays.country_code_eu[i], BitmapFactory.decodeResource(getResources(), Arrays.flag_eu[i])));
        }
        return items;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
        Toast.makeText(this, "Numero : " + position + "; Code : " + Arrays.country_code_eu[(int)id], Toast.LENGTH_SHORT).show();
    }
}
