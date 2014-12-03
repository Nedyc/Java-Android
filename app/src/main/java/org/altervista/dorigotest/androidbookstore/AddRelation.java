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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import org.altervista.dorigotest.androidbookstore.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRelation extends BaseMenu {
    public WebService ws;
    public String wsUrl;

    public Spinner author;
    public Spinner book;
    public HashMap<String, Integer> bookSpinnerMap;
    public HashMap<String, Integer> authorSpinnerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relation);

        String[] spinnerArray;
        author = (Spinner)findViewById(R.id.spinner);
        book = (Spinner)findViewById(R.id.spinner2);

        //Get the ws url from constant class
        wsUrl = Constants.getWsUrl();

        //Call ws
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Get books
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("action", "getBooks");


        try {
            WebService ws = new WebService();
            JSONObject result = ws.execute(wsUrl, parameters);
            JSONArray resultValues = result.getJSONArray("values");
            JSONArray errorsValues = result.getJSONArray("errors");

            if(errorsValues.length() > 0)
                Log.e("Errors", errorsValues.toString());
            else {
                //Set fields
                spinnerArray = new String[resultValues.length()];
                bookSpinnerMap = new HashMap<String, Integer>();
                for (int i = 0; i < resultValues.length(); i++) {
                    int id = resultValues.getJSONObject(i).getInt("id");
                    String title = resultValues.getJSONObject(i).getString("title");

                    bookSpinnerMap.put(title, id);
                    spinnerArray[i] = title;
                }

                ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, spinnerArray);
                adapter.setDropDownViewResource(R.layout.spinner_item2);
                book.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("WS Error", e.toString());
        }





        //Get authors
        parameters = new HashMap<String, String>();
        parameters.put("action", "getAuthors");


        try {
            WebService ws = new WebService();
            JSONObject result = ws.execute(wsUrl, parameters);
            JSONArray resultValues = result.getJSONArray("values");
            JSONArray errorsValues = result.getJSONArray("errors");

            if(errorsValues.length() > 0)
                Log.e("Errors", errorsValues.toString());
            else {
                //Set fields
                spinnerArray = new String[resultValues.length()];
                authorSpinnerMap = new HashMap<String, Integer>();
                for (int i = 0; i < resultValues.length(); i++) {
                    int id = resultValues.getJSONObject(i).getInt("id");
                    String surname = resultValues.getJSONObject(i).getString("surname");
                    String name = resultValues.getJSONObject(i).getString("name");

                    authorSpinnerMap.put(surname+" "+name, id);
                    spinnerArray[i] = surname+" "+name;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, spinnerArray);
                adapter.setDropDownViewResource(R.layout.spinner_item2);
                author.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("WS Error", e.toString());
        }



        //Save
        Button saveBtn = (Button)findViewById(R.id.button);
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = book.getSelectedItem().toString();
                int bookId = bookSpinnerMap.get(title);

                String name = author.getSelectedItem().toString();
                int authorId = authorSpinnerMap.get(name);


                ///Call ws
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("action", "add_author_book");
                parameters.put("author_id", authorId+"");
                parameters.put("book_id", bookId+"");

                try {
                    ws = new WebService();
                    JSONObject result = ws.execute(wsUrl, parameters);
                    JSONArray errorsValues = result.getJSONArray("errors");

                    if (errorsValues.length() > 0)
                        Log.e("Errors", errorsValues.toString());
                    else {
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.e("WS Error", e.toString());
                }
            }


        });

        //Back
        Button backBtn = (Button)findViewById(R.id.button3);
        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

}
