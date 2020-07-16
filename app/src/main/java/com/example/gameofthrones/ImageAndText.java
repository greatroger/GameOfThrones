package com.example.gameofthrones;

import android.graphics.Bitmap;

public class ImageAndText {
    private Bitmap bitmapImageUrl;
    private String text;
    private String imageUrl;

    public ImageAndText(){

    }

    public ImageAndText(String imageUrl, String text,Bitmap bitmapImageUrl) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.bitmapImageUrl=bitmapImageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmapImageUrl;
    }

    public void setBitmap(Bitmap bitmapImageUrl) {
        this.bitmapImageUrl = bitmapImageUrl;
    }
}
