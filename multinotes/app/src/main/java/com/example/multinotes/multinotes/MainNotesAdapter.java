package com.example.multinotes.multinotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vamse on 2/22/2017.
 */

 class MainNotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "MainNotesAdapter";
    private List<MainNotes> notesList;
    private MainActivity mainAct;

     MainNotesAdapter(List<MainNotes> note, MainActivity ma) {
        notesList = note;
        mainAct = ma;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new ViewHolder(itemView);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        MainNotes notes = notesList.get(position);
        holder.getviewofNoteName().setText(notes.getNotename());
        holder.getviewofUpdateTime().setText(notes.getDatetime());

        String text = trimString(notes);
        holder.getviewofNoteDesc().setText(text);


    }

    private String trimString(MainNotes note) {
        String recycledString;
        if (note.getNotedesc().length() > 80) {
             recycledString = note.getNotedesc().substring(0, 79) + "...";
        } else {
            recycledString = note.getNotedesc();
        }
        return recycledString;
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


}
