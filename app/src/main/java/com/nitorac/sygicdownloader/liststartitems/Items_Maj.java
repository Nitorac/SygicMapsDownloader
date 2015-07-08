package com.nitorac.sygicdownloader.liststartitems;

import android.graphics.Bitmap;

/**
 * Created by Nitorac.
 */
public class Items_Maj {

    private String title;
    private boolean update;

    public Items_Maj(String title, boolean update) {
        super();
        this.title = title;
        this.update = update;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setUpdate(boolean update){
        this.update = update;
    }

    public String getTitle(){
        return this.title;
    }

    public boolean getUpdate(){
        return this.update;
    }
}