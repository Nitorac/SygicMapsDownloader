package com.nitorac.sygicdownloader.liststartitems;

import android.graphics.Bitmap;

/**
 * Created by Nitorac.
 */
public class Items {

    private String title;
    private String description;
    private Bitmap flag;
    private int flag_drawable;

    public Items(String title, String description, Bitmap flag, int flag_drawable) {
        super();
        this.title = title;
        this.description = description;
        this.flag = flag;
        this.flag_drawable = flag_drawable;
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

    public void setFlag_drawable(int flag_drawable){ this.flag_drawable = flag_drawable;}

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public Bitmap getFlag(){
        return this.flag;
    }

    public int getFlag_drawable(){return this.flag_drawable;}
}