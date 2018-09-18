package com.theomnipotent.guruthor.testproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PACKAGE_NAME;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private ArrayList<BaseBean> genList = new ArrayList<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        PACKAGE_NAME = getApplicationContext().getPackageName();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout[] drawer = {findViewById(R.id.drawer_layout)};
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer[0], toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer[0].addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final RecyclerView listRecyclerView = findViewById(R.id.list_recycler_view);
        listRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager listLayoutManager = new LinearLayoutManager(this);
        listRecyclerView.setLayoutManager(listLayoutManager);

        final MyAdapter myAdapter = new MyAdapter(genList);
        listRecyclerView.setAdapter(myAdapter);

        @SuppressLint("StaticFieldLeak")
        class BackgroundTask extends
                AsyncTask<Integer, String, Integer> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Integer doInBackground(Integer... positions) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int x = new Random().nextInt(4);
                switch (x) {
                    case 0:
                        AudioBean aBean = new AudioBean();
                        aBean.setType(Constants.TYPE_AUDIO);
                        aBean.setFilePath("raw/audio_file");
                        genList.add(aBean);
                        break;
                    case 1:
                        VideoBean vBean = new VideoBean();
                        vBean.setType(Constants.TYPE_VIDEO);
                        vBean.setFilePath("raw/video_file");
                        genList.add(vBean);
                        break;
                    case 2:
                        ImageBean iBean = new ImageBean();
                        iBean.setType(Constants.TYPE_IMAGE);
                        iBean.setFilePath("drawable/ic_menu_gallery");
                        genList.add(iBean);
                        break;
                    default:
                        TextBean tBean = new TextBean();
                        tBean.setType(Constants.TYPE_TEXT);
                        tBean.setText("This is a test String");
                        genList.add(tBean);
                }
                return positions[0];
            }

            @Override
            protected void onPostExecute(Integer position) {
                super.onPostExecute(position);
                myAdapter.notifyItemInserted(position);
            }
        }

        for (int i = 0; i < 15; i++) {
            new BackgroundTask().execute(i);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Background Task started!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}