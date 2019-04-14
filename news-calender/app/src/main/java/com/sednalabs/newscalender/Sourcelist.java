package com.sednalabs.newscalender;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Sourcelist extends RecyclerView.Adapter<ViewHolder> {


    private MainActivity mainAct;
    private ArrayList<ArrayList<String>> sources;

    public Sourcelist(ArrayList<ArrayList<String>> sources, MainActivity ma) {
       this.sources = sources;
        this.mainAct = ma;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> source = sources.get(position);

        holder.getviewofNoteName().setText(source.get(1));
        holder.getviewofNoteDesc().setText(source.get(3));

    }

    @Override
    public int getItemCount() {
        return sources.size();
    }
}
