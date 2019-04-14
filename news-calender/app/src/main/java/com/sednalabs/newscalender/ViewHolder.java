package com.sednalabs.newscalender;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView sourceid;
    public TextView sourcedesc;


    public ViewHolder(View view) {
        super(view);
        setviewofNoteName((TextView) view.findViewById(R.id.sourceid));
        setviewofNoteDesc((TextView) view.findViewById(R.id.sourcedesc));
    }

    public TextView getviewofNoteName() {
        return sourceid;
    }

    public void setviewofNoteName(TextView noteName) {
        this.sourceid = noteName;
    }


    public TextView getviewofNoteDesc() {
        return sourcedesc;
    }

    public void setviewofNoteDesc(TextView noteDesc) {
        this.sourcedesc = noteDesc;
    }

}
