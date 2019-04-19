package com.example.aplinksmarthome.DateBase;

import org.litepal.crud.LitePalSupport;

public class EnergyUsed extends LitePalSupport {
    private int id;

    private float energy_used;

    private String date;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public float getEnergy_used(){
        return energy_used;
    }
    public void setEnergy_used(float energy_used){
        this.energy_used = energy_used;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

}
