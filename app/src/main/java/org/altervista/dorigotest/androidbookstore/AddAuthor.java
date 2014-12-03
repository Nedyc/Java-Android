package org.altervista.dorigotest.androidbookstore;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAuthor extends BaseMenu {
    public AlertDialog.Builder alertDialog;
    public AlertDialog.Builder deleteDialog;

    public TextView activityTitle;
    public EditText surname;
    public EditText name;
    public String wsUrl;
    public int id;

    public WebService ws;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_author);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Vars
        //Get the ws url from constant class
        wsUrl = Constants.getWsUrl();
        ws = new WebService();

        alertDialog = new AlertDialog.Builder(this);
        //Set dialog basics
        alertDialog.setTitle("Warning").setMessage("Fill empty fields!").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        }).setIcon(android.R.drawable.ic_dialog_alert);



        //Set dialog basics
        deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Warning").setMessage("Are you sure?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Call ws
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("action", "deleteAuthor");
                parameters.put("id", id+"");

                try {
                    JSONObject result = ws.execute(wsUrl, parameters);
                    JSONArray errorsValues = result.getJSONArray("errors");

                    if (errorsValues.length() > 0)
                        Log.e("Errors", errorsValues.toString());
                    else {
                        Intent intent = new Intent(getApplicationContext(), Authors.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.e("WS Error", e.toString());
                }
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        }).setIcon(android.R.drawable.ic_dialog_alert);


        activityTitle = (TextView)findViewById(R.id.textView5);
        Button saveBtn = (Button)findViewById(R.id.button);
        Button deleteBtn = (Button)findViewById(R.id.button2);
        Button backBtn = (Button)findViewById(R.id.button3);

        id = 0;
        surname = (EditText)findViewById(R.id.editText);
        name = (EditText)findViewById(R.id.editText2);



        //Get intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            String extrasSurname = extras.getString("surname");
            String extrasName = extras.getString("name");

            surname.setText(extrasSurname);
            name.setText(extrasName);

            activityTitle.setText("Edit Item");
        } else
            activityTitle.setText("Add Item");



        //Save
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surnameText = surname.getText().toString();
                String nameText = name.getText().toString();
                if(surnameText.equals("") || nameText.equals(""))
                    alertDialog.show();
                else{
                    //Call ws
                    Map<String, String> parameters = new HashMap<String, String>();
                    if(activityTitle.getText().equals("Add Item"))
                        parameters.put("action", "addAuthor");
                    else
                        parameters.put("action", "editAuthor");

                    String author = "{\"id\":\""+id+"\", \"surname\":\""+surnameText+"\", \"name\":\""+nameText+"\"}";
                    parameters.put("author", author);

                    try {
                        JSONObject result = ws.execute(wsUrl, parameters);
                        JSONArray errorsValues = result.getJSONArray("errors");

                        if (errorsValues.length() > 0)
                            Log.e("Errors", errorsValues.toString());
                        else {
                            Intent intent = new Intent(getApplicationContext(), Authors.class);
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        Log.e("WS Error", e.toString());
                    }
                }

            }
        });





        //Back
        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Authors.class);
                startActivity(intent);
            }
        });




        //Delete
        deleteBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
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
