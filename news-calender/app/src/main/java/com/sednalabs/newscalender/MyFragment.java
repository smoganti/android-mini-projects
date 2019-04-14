package com.sednalabs.newscalender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyFragment extends Fragment {


    public static Date da;
    private String imgurl;
     MainActivity af;
    static MyFragment f;

    public void setaf(MainActivity context) {
        this.af=context;
    }
    public MainActivity getMa(){
        return this.af;
    }
    public static final MyFragment newInstance(Articles article,MainActivity context,int count,int total)
    {


        f = new MyFragment();

        f.setaf(context);
        Bundle bdl = new Bundle(1);
        bdl.putString("title", article.getTitle());
        bdl.putString("author", article.getAuthor());
        bdl.putString("date", article.getDate());
        bdl.putString("desc", article.getDesc());
        bdl.putString("imgURL", article.getImgURL());
        bdl.putString("url",article.getUrl());
        bdl.putString("urlcount",Integer.toString(count-1));
        bdl.putString("count", count+" of "+total);

        f.setArguments(bdl);
        return f;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String title = getArguments().getString("title");
        String author = getArguments().getString("author");
        String date = getArguments().getString("date");
        String desc = getArguments().getString("desc");
        String count = getArguments().getString("count");
        imgurl = getArguments().getString("imgURL");


        View v = inflater.inflate(R.layout.fragmentactivity_main, container, false);


        if(!title.equals("null"))
            ((TextView) v.findViewById(R.id.frag_head)).setText(title);

        if(!author.equals("null")&&!author.contains("http://")&&!author.contains("https://")) {
            author=author.replace("\t","").trim();
            ((TextView) v.findViewById(R.id.author)).setText(trimString(author.replace("\n", "")));
        }
        if(!date.equals("null")) {

           DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault());


            try {
                da = df.parse(date);
                df = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                ((TextView) v.findViewById(R.id.datestmp)).setText(df.format(da));

            }catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(!desc.equals("null")) {
            TextView tv = (TextView)v.findViewById(R.id.desc);
            //tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(desc);
        }
        if(!count.equals("null"))
        ((TextView)v.findViewById(R.id.count)).setText(count);

        final ImageView officialImage = (ImageView)v.findViewById(R.id.articleimg);
            officialImage.destroyDrawingCache();

        if (!(imgurl.equals("No Data Provided")) && !(imgurl.equals("null"))) {
            Picasso picasso = new Picasso.Builder(f.getMa()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed
                    final String changedUrl = imgurl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(officialImage);
                }
            }).build();
            picasso.load(imgurl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(officialImage);
        } else {
            Picasso.with(f.getMa()).load(imgurl)
                    .error(R.drawable.brokenimage)
                    //.placeholder(R.drawable.missingimage)
                    //.error(R.drawable.brokenimage)
                    .into(officialImage);
        }


        return v;
    }
    private String trimString(String note) {
        String recycledString;
        if (note.length() > 50) {
            recycledString = note.substring(0, 49) + "...\n multiple authors";
        } else {
            recycledString = note;
        }
        return recycledString;
    }



}
