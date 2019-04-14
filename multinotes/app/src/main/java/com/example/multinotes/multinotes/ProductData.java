package com.example.multinotes.multinotes;

import android.util.Log;

/**
 * Created by vamse on 2/23/2017.
 */
public class ProductData {


    private String name;
    private String main_data;

    public String getData() {
        return main_data;
    }

    public void setData(String description) {
        Log.d("ts1", "Inside Product getclass"+description);
        this.main_data = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

       /* public String toString() {
            return datetime + ": " + main_data;
        }*/

}