package org.altervista.dorigotest.androidbookstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Home extends BaseMenu {
    public String wsUrl;
    public WebService ws;

    public LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get the ws url from constant class
        ws = new WebService();
        wsUrl = Constants.getWsUrl();

        layout = (LinearLayout) findViewById(R.id.mainLayout);

        //Call ws
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("action", "get_author_books");


        try {
            JSONObject result = ws.execute(wsUrl, parameters);
            JSONArray resultValues = result.getJSONArray("values");
            JSONArray errorsValues = result.getJSONArray("errors");

            if (errorsValues.length() > 0)
                Log.e("Errors", errorsValues.toString());
            else {
                //Set fields
                for (int i = 0; i < resultValues.length(); i++) {
                    String fullname = resultValues.getJSONObject(i).getString("fullname");
                    String books = resultValues.getJSONObject(i).getString("books");
                    JSONArray booksArray = new JSONArray(books);

                    setRow(fullname, booksArray);

                }
            }




        } catch (Exception e) {
            Log.e("WS Error", e.toString());
        }



        //On new button
        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRelation.class);
                startActivity(intent);
            }
        });
    }


    //Set author/book rows
    public void setRow(String fullname, JSONArray booksArray){

        TextView textView = new TextView(getApplicationContext());
        textView.setText(fullname);
        textView.setTextColor(Color.BLACK);
        textView.setTextAppearance(this, android.R.style.TextAppearance_Large);
        textView.setPadding(0,20,0,0);

        layout.addView(textView);

        for (int i = 0; i < booksArray.length(); i++) {
            try {
                int id = booksArray.getJSONObject(i).getInt("id");
                String title = booksArray.getJSONObject(i).getString("title");

                LinearLayout innerLayout = new LinearLayout(getApplicationContext());
                TextView innerTextView = new TextView(getApplicationContext());
                innerTextView.setText(title);
                innerTextView.setTextColor(Color.BLACK);


                Button deleteBtn = new Button(getApplicationContext());
                deleteBtn.setText("Delete");
                deleteBtn.setId(id);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    try {
                        Integer id = v.getId();
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("action", "delete_author_book");
                        parameters.put("id", id+"");

                        JSONObject result = ws.execute(wsUrl, parameters);
                        JSONArray errorsValues = result.getJSONArray("errors");

                        if (errorsValues.length() > 0)
                            Log.e("Errors", errorsValues.toString());
                        else {
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                        }


                    } catch (Exception e){Log.e("ButtonClick", e.toString());}

                    }
                });

                innerLayout.addView(deleteBtn);
                innerLayout.addView(innerTextView);

                layout.addView(innerLayout);
            } catch (
                    Exception e){Log.e("JSON", e.toString());
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


}
