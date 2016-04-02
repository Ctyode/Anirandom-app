package com.flamie.anirandom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class RandomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button randomize = (Button) findViewById(R.id.randomize);
        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HttpRequest httpRequest = new HttpRequest(new URL("http://192.168.0.103:8080/anirandom.json"), new HttpRequest.HttpRequestCallback() {
                        @Override
                        public void success(String data) {
                            System.out.println(data);
                            System.out.println("ебни, просто ебни");
                        }

                        @Override
                        public void error(Exception e) {
                            Log.e(getClass().getName(), "все наебнулось", e);
                        }
                    });
                    httpRequest.start();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Spinner genres = (Spinner) findViewById(R.id.genre);
        ArrayAdapter<CharSequence> genresAdapter = ArrayAdapter.createFromResource(this,
                R.array.genre, android.R.layout.simple_spinner_item);
        genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genres.setAdapter(genresAdapter);

        Spinner years = (Spinner) findViewById(R.id.year);
        ArrayAdapter<CharSequence> yearsAdapter = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(yearsAdapter);

        Spinner rating = (Spinner) findViewById(R.id.rating);
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(this,
                R.array.rating, android.R.layout.simple_spinner_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(ratingAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_random, menu);
        final Activity self = this;
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(self, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
