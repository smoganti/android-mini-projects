package com.sednalabs.newscalender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


public class activity_calender extends AppCompatActivity {


    CalendarView cView;
    Button button1;
    int day;int mon; int yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

         cView = (CalendarView)(findViewById(R.id.calendarView));
        cView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                day =date;
                mon=month;
                yy=year;
            }
        });

             button1 = (Button)findViewById(R.id.submit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity_calender.this,day+" "+mon+" "+yy,Toast.LENGTH_LONG).show();

            }
        });


    }



}
