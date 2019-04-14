package com.sednalabs.newscalender;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerView;
    private Sourcelist mAdapter;
    ArrayList<ArrayList<String>> sources;
    protected static String categoryselection="";
    protected static String country="us";
    public MainActivity _context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        _context = MainActivity.this;
        GlobalClass gc = (GlobalClass) getApplicationContext();
        gc.setMainActivity(_context);


        Intent nsintent = new Intent(MainActivity.this, NewsService.class);
        startService(nsintent);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        NewsSourceDownloader nsd = new NewsSourceDownloader(MainActivity.this);
        nsd.execute(country,categoryselection);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_topstories) {
            categoryselection="";
            NewsSourceDownloader nsd = new NewsSourceDownloader(MainActivity.this);
            nsd.execute(country,categoryselection);

        }else if (id== R.id.nav_categories){
            final CharSequence[][] sArray = new CharSequence[2][9];
                    sArray[0][0]="Science and Nature";sArray[1][0]="science-and-nature";
                    sArray[0][1]="Gaming";sArray[1][1]="gaming";
                    sArray[0][2]="Music";sArray[1][2]="music";
                    sArray[0][3]="General";sArray[1][3]="general";
                    sArray[0][4]="Politics";sArray[1][4]="politics";
                    sArray[0][5]="Technology";sArray[1][5]="technology";
                    sArray[0][6]="Sports";sArray[1][6]="sport";
                    sArray[0][7]="Entertainment";sArray[1][7]="entertainment";
                    sArray[0][8]="Business";sArray[1][8]="business";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");

            builder.setItems(sArray[0], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    categoryselection=String.valueOf(sArray[1][which]);
                    NewsSourceDownloader nsd = new NewsSourceDownloader(MainActivity.this);
                    nsd.execute(country,categoryselection);
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        else if (id == R.id.nav_calender) {
            Intent ic = new Intent(this,activity_calender.class);
            startActivity(ic);

        }  else if (id == R.id.nav_manage) {
            final CharSequence[][] sArray2 = new CharSequence[2][5];
            sArray2[0][0]="Australia";sArray2[1][0]="au";
            sArray2[0][1]="Great Britain";sArray2[1][1]="gb";
            sArray2[0][2]="India";sArray2[1][2]="in";
            sArray2[0][3]="Italy";sArray2[1][3]="it";
            sArray2[0][4]="USA";sArray2[1][4]="us";


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");

            builder.setItems(sArray2[0], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    country=String.valueOf(sArray2[1][which]);
                    NewsSourceDownloader nsd = new NewsSourceDownloader(MainActivity.this);
                    nsd.execute(country,categoryselection);
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();

        }else if(id==R.id.about){
           Intent abt=new Intent(this,AboutActivity.class);
            startActivity(abt);
        }else if(id==R.id.feedback){
            Intent feedback = new Intent(this,FeedbackActivity.class);
            startActivity(feedback);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setsources(ArrayList<ArrayList<String>> sources, ArrayList<String> categoryList) {

        this.sources = sources;
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new Sourcelist(sources, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }




    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);


        Intent intent = new Intent(this,Activity_frag.class);

        ArrayList<String> s = sources.get(pos);
        intent.putStringArrayListExtra("sourceval",s);
        startActivity(intent);

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



}
