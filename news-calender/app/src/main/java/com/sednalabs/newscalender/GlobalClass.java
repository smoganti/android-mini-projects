package com.sednalabs.newscalender;


import android.app.Application;

public class GlobalClass extends Application{

    public MainActivity ma;
    @Override
    public void onCreate(){
        super.onCreate();
    }

    public void setMainActivity(MainActivity ma){
        this.ma = ma;
    }

    public MainActivity getMa(){
        return ma;
    }

}
