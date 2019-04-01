package com.example.aplinksmarthome.UI;

public class CardBottom {
    private String name;
    private int imageId;
    public CardBottom(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
