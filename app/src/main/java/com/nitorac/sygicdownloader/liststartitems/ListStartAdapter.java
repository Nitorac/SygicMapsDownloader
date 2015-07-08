package com.nitorac.sygicdownloader.liststartitems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitorac.sygicdownloader.R;

import java.util.ArrayList;

/**
 * Created by Nitorac.
 */
public class ListStartAdapter extends ArrayAdapter<Items> {

    private final Context context;
    private final ArrayList<Items> itemsArrayList;

    public ListStartAdapter(Context context, ArrayList<Items> itemsArrayList) {

        super(context, R.layout.item_start, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_start, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);
        ImageView flagView = (ImageView) rowView.findViewById(R.id.imageView);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText("Code : " + itemsArrayList.get(position).getDescription());
        flagView.setImageBitmap(itemsArrayList.get(position).getFlag());

        // 5. retrn rowView
        return rowView;
    }
}