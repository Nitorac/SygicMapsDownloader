package com.nitorac.sygicdownloader.liststartitems;

import android.content.Context;

import com.nitorac.sygicdownloader.R;

/**
 * Created by Nitorac.
 */
public class Arrays {

    public Context c;

    public Arrays(Context c){
        this.c = c;
        country_eu = c.getResources().getStringArray(R.array.country_eu);
        continent = c.getResources().getStringArray(R.array.continent);
    }

    //Doit respecter le meme ordre !!!!
    public String[] country_af = {};
    public String[] country_code_af = {};
    public Integer[] flag_af = {};

    public String[] country_as = {};
    public String[] country_code_as = {};
    public Integer[] flag_as = {};

    public String[] country_aus = {"Error"};
    public String[] country_code_aus = {"err"};
    public Integer[] flag_aus = {R.drawable.ic_action_name};

    public String[] country_eu;
    public String[] country_code_eu = {"alb","and","aut","blr","bel","bih","bgr","hrv","cyp","cze","dnk","est","fin","fra","deu","gib","grc","hun","irl","ita","lva","lie","ltu","lux","mlt","mco","mne","nld","nor","pol","prt","mda","rou","rus","smr","srb","svk","svn","esp","swe","che","mkd","tur","ukr","gbr","vat"};
    public Integer[] flag_eu = {R.mipmap.albania, R.mipmap.andorra, R.mipmap.austria, R.mipmap.belarus, R.mipmap.belgium, R.mipmap.bosniaherze, R.mipmap.bulgaria,R.mipmap.croatia,R.mipmap.cyprus,R.mipmap.czechrepublic,R.mipmap.denmark,R.mipmap.estonia,R.mipmap.finland,R.mipmap.france,R.mipmap.germany,R.mipmap.gibraltar,R.mipmap.greece,R.mipmap.hungary,R.mipmap.ireland,R.mipmap.italy,R.mipmap.latvia,R.mipmap.liechtenshein,R.mipmap.lithuania,R.mipmap.luxembourg,R.mipmap.malta,R.mipmap.monaco,R.mipmap.montenegro,R.mipmap.netherlands,R.mipmap.norway,R.mipmap.poland,R.mipmap.portugal,R.mipmap.moldova,R.mipmap.romania,R.mipmap.russia,R.mipmap.sanmarino,R.mipmap.serbia,R.mipmap.slovakia,R.mipmap.slovenia,R.mipmap.spain,R.mipmap.sweden,R.mipmap.switzerland,R.mipmap.macedonia,R.mipmap.turkey,R.mipmap.ukraine,R.mipmap.uk,R.mipmap.vatican};

    public String[] country_mo = {};
    public String[] country_code_mo = {};
    public Integer[] flag_mo = {};

    public String[] country_adn = {};
    public String[] country_code_adn = {};
    public Integer[] flag_adn = {};

    public String[] country_ads = {};
    public String[] country_code_ads = {};
    public Integer[] flag_ads = {};

    public String[] continent;
    public String[] continent_code = {"Africa","Asia","Australia","Europe","MiddleEast","NorthAmerica","SouthAmerica"};

    public String[] error_country = {"Error"};
    public String[] error_country_code = {"err"};
    public Integer[] error_flag = {R.drawable.ic_action_name};
}
