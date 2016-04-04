package com.flamie.anirandom.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flamie.anirandom.HttpRequest;
import com.flamie.anirandom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RandomActivity extends AppCompatActivity {

    private Handler handler;
    private Bitmap imageBitmap;
    private String titleText;
    private String synopsisText;
    private Double ratingText;

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
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(imageBitmap);
                TextView titleTextView = (TextView) findViewById(R.id.title_text);
                titleTextView.setText(titleText);
                TextView synopsisTextView = (TextView) findViewById(R.id.synopsis_text);
                synopsisTextView.setText(synopsisText);
                TextView ratingTextView = (TextView) findViewById(R.id.rating_text);
                ratingTextView.setText(ratingText.toString());
                return false;
            }
        });

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String genre = ((Spinner) findViewById(R.id.genre)).getSelectedItem().toString();
                    if("Genre".equals(genre)) {
                        genre = "undefined";
                    }
                    String year = ((Spinner) findViewById(R.id.year)).getSelectedItem().toString();
                    if("Year".equals(year)) {
                        year = "undefined";
                    }
                    String rating = ((Spinner) findViewById(R.id.rating)).getSelectedItem().toString();
                    if("Rating".equals(rating)) {
                        rating = "undefined";
                    }
                    String httpRequestString = String.format("http://192.168.0.103:8080/anirandom.json?genre=%s&year=%s&rating=%s",
                                                             URLEncoder.encode(genre, "UTF-8"),
                                                             URLEncoder.encode(year, "UTF-8"),
                                                             URLEncoder.encode(rating, "UTF-8"));
                    HttpRequest httpRequest = new HttpRequest(new URL(httpRequestString), new HttpRequest.HttpRequestCallback() {
                        @Override
                        public void success(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String image = jsonObject.getString("image");
                                String title = jsonObject.getString("title");
                                String synopsis = jsonObject.getString("synopsis");
                                Double rating = jsonObject.getDouble("rating");
                                URL url = new URL(image);
                                titleText = title;
                                synopsisText = synopsis;
                                ratingText = rating;
                                imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                handler.sendMessage(new Message());
                            } catch (IOException | JSONException e) {
                                Log.e(getClass().getName(), "наебнулось не все", e);
                            }
                        }

                        @Override
                        public void error(Exception e) {
                            Log.e(getClass().getName(), "все наебнулось", e);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), R.string.no_connection_error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    httpRequest.start();
                } catch (MalformedURLException | UnsupportedEncodingException e) {
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
