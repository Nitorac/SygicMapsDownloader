package com.nitorac.sygicdownloader.liststartitems;

import android.graphics.Bitmap;

/**
 * Created by Nitorac.
 */
public class Items {

    private String title;
    private String description;
    private Bitmap flag;

    public Items(String title, String description, Bitmap flag) {
        super();
        this.title = title;
        this.description = description;
        this.flag = flag;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public void setFlag(Bitmap flag){
        this.flag = flag;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public Bitmap getFlag(){
        return this.flag;
    }
}