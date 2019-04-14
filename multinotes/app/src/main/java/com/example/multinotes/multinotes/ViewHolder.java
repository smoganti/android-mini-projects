package com.example.multinotes.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vamse on 2/22/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView datetime;
    public TextView description;

    public ViewHolder(View view) {
        super(view);
        setviewofNoteName((TextView) view.findViewById(R.id.nameCons));
        setviewofNoteDesc((TextView) view.findViewById(R.id.descCons));
        setviewofUpdateTime ((TextView) view.findViewById(R.id.dttimeCons));
    }

    public TextView getviewofNoteName() {
        return name;
    }

    public void setviewofNoteName(TextView noteName) {
        this.name = noteName;
    }

    public TextView getviewofUpdateTime() {
        return datetime;
    }

    public void setviewofUpdateTime(TextView UpdateTime) {
        this.datetime = UpdateTime;
    }

    public TextView getviewofNoteDesc() {
        return description;
    }

    public void setviewofNoteDesc(TextView noteDesc) {
        this.description = noteDesc;
    }

}
