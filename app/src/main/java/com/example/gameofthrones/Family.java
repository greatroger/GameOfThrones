package com.example.gameofthrones;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Family {
    boolean haveLoaded=false;
    public  String familyName;
    public  ArrayList<Characters> top10Person = new ArrayList<Characters>();
    public  ArrayList<ImageAndText> pages = new ArrayList<ImageAndText>();

    public static Map<String,Family> instance=new HashMap<>();

    public static Family getInstance(String name){
        if(instance.get(name)==null){
            instance.put(name,new Family(name));
        }
        return instance.get(name);
    }

    private Family(String name){
        familyName=name;
        haveLoaded=false;
    }
}
