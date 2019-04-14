package com.example.multinotes.multinotes;

import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by vamse on 2/22/2017.
 */

public class MainNotes {
    private String datetime;
    private String notedesc;
    private String notename;
    



    public MainNotes(){
        datetime= null;
        notedesc = null;
        notename= null;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String newdatetime) {
        this.datetime = newdatetime;
    }

    public String getNotedesc() {
        return notedesc;
    }

    public void setNotedesc(String notedesc) {
        this.notedesc = notedesc;
    }

    public String getNotename() {
        return notename;
    }

    public void setNotename(String notename) {
        this.notename = notename;
    }

}
