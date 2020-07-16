package com.example.gameofthrones;

import android.widget.TextView;

import java.util.ArrayList;

public class SlideMenuUtil {
    // 家族菜单
    public static ArrayList<TextView> familyMenu;
    public static String ITEM_Stark = "史塔克";
    public static String ITEM_Lannister = "兰尼斯特";
    public static String ITEM_Baratheon = "拜拉席恩";
    public static String ITEM_Targaryen = "坦格利安";
    public static String ITEM_Tully = "徒利";
    public static String ITEM_Arryn = "艾林";
    public static String ITEM_Tyrell = "提利尔";
    public static String ITEM_Martell = "马泰尔";
    public static String ITEM_Greyjoy = "葛雷乔伊";

    // 音乐菜单
    public static ArrayList<TextView> albumMenu;
    public static String ITEM_One = "第一季";
    public static String ITEM_Two = "第二季";
    public static String ITEM_Three = "第三季";
    public static String ITEM_Four = "第四季";
    public static String ITEM_Five = "第五季";
    public static String ITEM_Six = "第六季";
    public static String ITEM_Seven = "第七季";
    public static String ITEM_Eight = "第八季";

    public static void initFamily(){
        familyMenu=new ArrayList<TextView>();
    }

    public static void initMusic(){
        albumMenu=new ArrayList<TextView>();
    }
}
