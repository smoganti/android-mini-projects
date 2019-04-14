package com.sednalabs.newscalender;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;



public class NewsService extends Service {

    private boolean isRunning = true;
    private static final String TAG = "News_Service";
    private static final String SOURCE_ID = "sourceid";
    private static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    ServiceReceiver serviceReceiver;
    private static final String ACTION_NEWS_STORY = "news_story";
    private static final String STORY = "storydata";
    ArrayList<Articles> articlelist = new ArrayList<>();


    @Override
    public void onDestroy(){
        //unregistering
        //the receiver
        unregisterReceiver(serviceReceiver);
        isRunning = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Intent intentmain = new Intent(NewsService.this,Activity_frag.class);
        startService(intentmain);

        serviceReceiver = new ServiceReceiver();

        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR

        new Thread(new Runnable() {
            @Override
            public void run() {


                while (isRunning) {

                    if (isRunning) {
                            while(articlelist.size()!=0){
                                Intent intent = new Intent();
                                intent.setAction(ACTION_NEWS_STORY);
                                Bundle bdl = new Bundle();
                                bdl.putSerializable(STORY,new DataWrapper(articlelist));
                                intent.putExtras(bdl);
                                sendBroadcast(intent);
                                articlelist.clear();
                            }

                    }
                    try {
                        Thread.sleep(250);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        return Service.START_STICKY;

    }

    public void setarticles(ArrayList<Articles> sourcedata) {
        articlelist.clear();

        for(int i= 0; i<sourcedata.size();i++){
           /* articlelist.add(i,sourcedata.get(i));*/
            articlelist.add(new Articles(sourcedata.get(i).getTitle(),sourcedata.get(i).getAuthor(),sourcedata.get(i).getDate(),sourcedata.get(i).getDesc(),sourcedata.get(i).getImgURL(),sourcedata.get(i).getUrl()));
        }

    }


    class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_MSG_TO_SERVICE:
                   if (intent.hasExtra(SOURCE_ID)) {
                       NewsArticleDownloader nsd = new NewsArticleDownloader(NewsService.this, intent.getStringExtra(SOURCE_ID));
                       nsd.execute();
                   }
                    break;

            }
        }
    }

}
