package com.sednalabs.newscalender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    EditText feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedbackTxt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback(view, String.valueOf(feedback.getText()));

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendFeedback(View view,String feedbackTxt) {
        Snackbar.make(view, "Replace with your own action"+feedbackTxt, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Intent ifeedback = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("mailto:?subject=" + "Feedback for NewCalender" + "&body=" + feedbackTxt);
        ifeedback.setData(data);
        startActivity(ifeedback);
        feedback.setText("");
    }

}
