package com.example.multinotes.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;




public class EditActivity extends AppCompatActivity {


    private EditText description ;
    private EditText name ;
    String datetime;
    int valueat;
    String oldname;
    String olddesc;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);
        description = (EditText) findViewById(R.id.prodDesc);
        name = (EditText)findViewById(R.id.prodName) ;
        description.setMovementMethod(new ScrollingMovementMethod());



        Intent intent = getIntent();
        if (intent.hasExtra("Notename")) {
            String sname = intent.getStringExtra("Notename");
            name.setText(sname);
            oldname = sname;

        }
        if (intent.hasExtra("NoteDesc")) {
            String desc = intent.getStringExtra("NoteDesc");
            description.setText(desc);
            olddesc=desc;
        }
         datetime = intent.getStringExtra("Note");
        valueat = intent.getIntExtra("position",0);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void saveNote() {

        if(name.getText().toString().isEmpty()||name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"Name is empty, Unable to save note",Toast.LENGTH_LONG).show();
        }
        else if(name.getText().toString().equals(oldname) && description.getText().toString().equals(olddesc)) {
            Toast.makeText(this,"No changes made exiting !",Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Intent data = new Intent();
            data.putExtra("Notename", name.getText().toString());
            data.putExtra("NoteDesc", description.getText().toString());
            data.putExtra("Note",datetime);
            data.putExtra("position",valueat);
            setResult(RESULT_OK, data);
            finish();
            }


    }

    @Override
    public void onBackPressed() {
        if(name.getText().toString().isEmpty() && description.getText().toString().isEmpty()) {
            super.onBackPressed();
        }
        else if(name.getText().toString().isEmpty()||name.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Name is empty, Unable to save note",Toast.LENGTH_LONG).show();
             }
        else if(name.getText().toString().equals(oldname) && description.getText().toString().equals(olddesc))
            super.onBackPressed();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.save);

            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    saveNote();
                    EditActivity.super.onBackPressed();
                    }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    EditActivity.super.onBackPressed();

                }
            });

            builder.setMessage("Do you wish to save your note '"+name.getText()+"'?");
            builder.setTitle("Save Note");

            AlertDialog dialog = builder.create();
            dialog.show();


            //finish();
        }
    }



}
