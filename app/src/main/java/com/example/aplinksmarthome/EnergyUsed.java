package com.example.aplinksmarthome;

import org.litepal.crud.LitePalSupport;

public class EnergyUsed extends LitePalSupport {
    private int id;

    private float energy_used;

    private String Electrical_appliances_name,date;

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
    public String getElectrical_appliances_name(){
        return Electrical_appliances_name;
    }
    public void setElectrical_appliances_name(String Electrical_appliances_name){
        this.Electrical_appliances_name = Electrical_appliances_name;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

}
