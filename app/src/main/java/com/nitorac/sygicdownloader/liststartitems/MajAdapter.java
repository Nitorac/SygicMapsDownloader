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
public class MajAdapter extends ArrayAdapter<Items_Maj> {

    private final Context context;
    private final ArrayList<Items_Maj> itemsArrayList;

    public MajAdapter(Context context, ArrayList<Items_Maj> itemsArrayList) {
        super(context, R.layout.item_maj, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_maj, parent, false);

        // 3. Get the two text view from the rowView
        TextView maj_label = (TextView) rowView.findViewById(R.id.maj_label);
        ImageView maj_image = (ImageView) rowView.findViewById(R.id.maj_image);

        // 4. Set the text for textView
        maj_label.setText(itemsArrayList.get(position).getTitle());

        if(itemsArrayList.get(position).getUpdate()) {
            maj_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_update));
            maj_label.setTextColor(context.getResources().getColor(R.color.Finished));
        }

        // 5. retrn rowView
        return rowView;
    }
}