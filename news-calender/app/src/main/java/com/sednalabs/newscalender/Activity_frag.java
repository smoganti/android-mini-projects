package com.sednalabs.newscalender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity_frag extends AppCompatActivity{


    ArrayList<String> selectedsources;
    private static final String SOURCE_ID = "sourceid";
    private static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    public static final String ACTION_NEWS_STORY = "news_story";
    public static final String STORY = "storydata";

    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    NewsReceiver newsreceiver;
    int currentpos;
    MainActivity ma;


    private ArrayList<Articles> clickArticles;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        if(i.hasExtra("sourceval")) {
            selectedsources = i.getStringArrayListExtra("sourceval");
            setTitle(selectedsources.get(1));
        }

        GlobalClass gc = (GlobalClass) getApplicationContext();
            ma = gc.getMa();
        new MyFragment().setaf(ma);

        Intent intent = new Intent();
        intent.setAction(ACTION_MSG_TO_SERVICE);
        intent.putExtra(SOURCE_ID,selectedsources.get(0));
        sendBroadcast(intent);

        setContentView(R.layout.frag_activity);

        fragments = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);

        pager.setAdapter(pageAdapter);



        newsreceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);

        registerReceiver(newsreceiver, filter1);


    }



@Override
    protected void onDestroy(){

    unregisterReceiver(newsreceiver);
    fragments.clear();
    pager.destroyDrawingCache();
    if(clickArticles!=null)
    clickArticles.clear();
    super.onDestroy();

}

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }



    final class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    ArrayList<Articles> a;

                    Bundle bdl=  intent.getExtras();
                    DataWrapper dw = (DataWrapper)bdl.getSerializable(STORY);
                    a= dw.getParliaments();
                    reDoFragments(a);
                    //Toast.makeText(MainActivity.this, "Redo Fragments", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
    public void buttonClicked(View v) {

        currentpos = pager.getCurrentItem();
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse(clickArticles.get(currentpos).getUrl()));
        startActivity(viewIntent);


    }

    private void reDoFragments(ArrayList<Articles> storylist) {

        if(clickArticles!=null)
            clickArticles.clear();

        clickArticles=storylist;
        for (int i = 0; i < storylist.size(); i++) {
            pageAdapter.notifyChangeInPosition(i);

        }


        fragments.clear();



        for (int i = 0; i < storylist.size(); i++) {
            int p=storylist.size();
            int q = i+1;

            if((storylist.get(i).getAuthor()==null&&storylist.get(i).getDate()==null&&storylist.get(i).getDesc()==null&&storylist.get(i).getTitle()==null&&storylist.get(i).getImgURL()==null))
                p=p-1;

            if(!(storylist.get(i).getAuthor()==null&&storylist.get(i).getDate()==null&&storylist.get(i).getDesc()==null&&storylist.get(i).getTitle()==null&&storylist.get(i).getImgURL()==null))
            {

                fragments.add(MyFragment.newInstance(storylist.get(i), ma, q, p));
            }
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                    currentpos=pager.getCurrentItem();

               Toast.makeText(this,clickArticles.get(currentpos).getUrl(),Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,clickArticles.get(currentpos).getUrl() );
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;
            case R.id.addtoDate:
                if(item.isChecked()){
                    item.setIcon(R.drawable.removefavorites);
                    item.setChecked(false);
                    Toast.makeText(this,"Added this set to Today's Date",Toast.LENGTH_LONG).show();


                }else{
                    item.setIcon(R.drawable.addtofavorites);
                    item.setChecked(true);
                    Toast.makeText(this,"Removed this set from Today's Date",Toast.LENGTH_LONG).show();
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        private MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        private void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }


    }







}
