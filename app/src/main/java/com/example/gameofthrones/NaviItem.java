package com.example.gameofthrones;

public class NaviItem {
    private int iconId;
    private String iconName;

    public NaviItem() {
    }

    public NaviItem(int iconId, String iconName) {
        this.iconId = iconId;
        this.iconName = iconName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getIconName() {
        return iconName;
    }

}
