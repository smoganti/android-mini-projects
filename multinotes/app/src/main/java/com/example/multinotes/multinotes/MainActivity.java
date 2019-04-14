package com.example.multinotes.multinotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {


    private static final int NEW_REQ = 1;
    private static final int UPDATE_REQ = 2;

    public List<MainNotes> notesList = new ArrayList<>();  // Main content is here

    private NoteFragment mainFragment;
    private RecyclerView recyclerView; // Layout's recyclerview
    private static final String  F_ID= "My_Note_IDFrag";


    private MainNotesAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "inside onCreate method");
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new MainNotesAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAsyncJob();

    }



    @Override
    protected void onPause() {
        super.onPause();
        try {

            writeJsonStream(notesList);
        }
        catch (IOException e){
            e.getStackTrace();
        }

        Log.d("MainActivity", "inside onPause method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "inside onResume method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "inside onStop method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "inside onDestroy method");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.newnote:
                newNote();
                return true;
            case R.id.help:
                aboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newNote() {


        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,MainActivity.class.getSimpleName());
        intent.putExtra("Note","New");
        intent.putExtra("position",0);
        intent.putExtra("NoteName","");
        intent.putExtra("NoteDesc","");

        startActivityForResult(intent, NEW_REQ);

    }

    private void aboutActivity() {
        Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void editActivity(int value,MainNotes m) {




        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        intent.putExtra("Notename", m.getNotename());
        intent.putExtra("NoteDesc", m.getNotedesc());
        intent.putExtra("position", value);
        intent.putExtra("Note", getString(R.string.oldNoteIdentifierConst));
        startActivityForResult(intent, NEW_REQ);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = null;
        String desc = null;
        String noteIdentifier = null;
        if (requestCode == NEW_REQ) {
            if (resultCode == RESULT_OK) {

                noteIdentifier = data.getStringExtra("Note");
                desc = data.getStringExtra("NoteDesc");
                name = data.getStringExtra("Notename");
                int position = data.getIntExtra("position", 0);

                MainNotes notesresult = new MainNotes();
                notesresult.setNotedesc(desc);

                Calendar p = Calendar.getInstance();
                String sDate = String.valueOf(p.getTime());


                notesresult.setDatetime(sDate);
                notesresult.setNotename(name);

                if (noteIdentifier.compareTo(getString(R.string.oldNoteIdentifierConst)) == 0) {
                    notesList.remove(position);
                    position = 0;
                }
                notesList.add(position, notesresult);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        MainNotes m = notesList.get(pos);
        editActivity(pos,m);


    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks

        final int pos = recyclerView.getChildLayoutPosition(v);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.delete);

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(pos);
                mAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });


        builder.setMessage("Do you Want to Delete Note '"+notesList.get(pos).getNotename()+"'?");
        builder.setTitle("Delete Note");

        AlertDialog dialog = builder.create();
        dialog.show();





        return false;
    }

    //Requesting system to start Async
    private void myAsyncJob() {
        FragmentManager manager = getFragmentManager();
        mainFragment = (NoteFragment) manager.findFragmentByTag(F_ID);
        if (mainFragment != null) {
            notesList = mainFragment.getNoteList();
            mAdapter = new MainNotesAdapter(notesList, MainActivity.this);
            recyclerView.setAdapter(mAdapter);
        } else {
            MyAsyncTask mytask = new MyAsyncTask();
            mytask.execute();
        }
    }



    //ReaderJSON

    public List<MainNotes> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<MainNotes> readMessagesArray(JsonReader reader) throws IOException {

        List<MainNotes> readnoteList = new ArrayList<MainNotes>();
        reader.beginArray();
        while (reader.hasNext()) {
            readnoteList.add(readMessage(reader));
        }
        reader.endArray();
        return readnoteList;
    }

    public MainNotes readMessage(JsonReader reader) throws IOException {

        MainNotes allnote = new MainNotes();


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Notename")) {
                allnote.setNotename(reader.nextString());
            } else if (name.equals("Note")) {
                allnote.setDatetime(reader.nextString());
            } else if (name.equals("NoteDesc")) {
                allnote.setNotedesc(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();


        return allnote;
    }

        //This is a fragment inner class
        public static class NoteFragment extends Fragment {
            private List<MainNotes> fragNotesList;

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setRetainInstance(true);
            }

            public void setData(List<MainNotes> thisnoteList) {
                this.fragNotesList = thisnoteList;
            }

            public List<MainNotes> getNoteList() {
                return fragNotesList;
            }
        }

    //Writer JSON

    public void writeJsonStream( List<MainNotes> notewrite) throws IOException {
        FileOutputStream stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(stream, getString(R.string.encoding)));
        writer.setIndent("  ");
        writeMessagesArray(writer, notewrite);
        writer.close();
    }

    public void writeMessagesArray(JsonWriter writer, List<MainNotes> notewrite) throws IOException {
        writer.beginArray();
        for (MainNotes note : notewrite) {
            writer.beginObject();
            writer.name("Notename").value(note.getNotename());
            writer.name("Note").value(note.getDatetime());
            writer.name("NoteDesc").value(note.getNotedesc());

            writer.endObject();
        }
        writer.endArray();
    }


   /////////////
    class MyAsyncTask extends AsyncTask<Long, Integer, List<MainNotes>> { //  <Parameter, Progress, Result>

        @Override
        protected List<MainNotes> doInBackground(Long... params) {

            List<MainNotes> noteList1 = null;
            try{

                InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));

                noteList1 =  readJsonStream(is);
            }  catch (IOException e) {
                e.printStackTrace();
            }

            return noteList1;
        }

       @Override
       protected void onPostExecute(List<MainNotes>  mynotesList) {
           if (mynotesList != null) {
               notesList = mynotesList;
           }

           mAdapter = new MainNotesAdapter(notesList, MainActivity.this);
           recyclerView.setAdapter(mAdapter);

           FragmentManager manager = getFragmentManager();
           mainFragment = (NoteFragment) manager.findFragmentByTag(F_ID);
           if (mainFragment == null) {
               mainFragment = new NoteFragment();
               manager.beginTransaction().add(mainFragment, F_ID).commit();
               mainFragment.setData(notesList);
           }
       }


    }





}



