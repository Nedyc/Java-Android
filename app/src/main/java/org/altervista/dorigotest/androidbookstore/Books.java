package org.altervista.dorigotest.androidbookstore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Books extends BaseMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);


        //Get the ws url from constant class
        String wsUrl = Constants.getWsUrl();

        //Call ws
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableView);

                //Set header
                List<String> headerLabels = Arrays.asList("Title", "Description");
                addRow(0, headerLabels, tableLayout, true);


                //Set fields
                for (int i = 0; i < resultValues.length(); i++) {
                    int id = resultValues.getJSONObject(i).getInt("id");
                    String title = resultValues.getJSONObject(i).getString("title");
                    String description = resultValues.getJSONObject(i).getString("description");

                    List<String> recordLabels = Arrays.asList(title, description);
                    addRow(id, recordLabels, tableLayout, false);
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
                Intent intent = new Intent(getApplicationContext(), AddBook.class);
                startActivity(intent);
            }
        });
    }



    //Dinamically add rows
    public void addRow(int id, List<String> labels, TableLayout tableLayout, Boolean isHeader){
        TableRow tableRow = new TableRow(getApplicationContext());

        for (int i = 0; i < labels.size(); i++) {
            TextView textView;

            textView = new TextView(getApplicationContext());
            textView.setPadding(20, 20, 20, 20);
            textView.setText(labels.get(i));
            if(isHeader)
                textView.setTextColor(Color.RED);
            else {
                textView.setTextColor(Color.BLACK);

                tableRow.setId(id);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Integer id = v.getId();
                            TableRow tappedTr = (TableRow) findViewById(v.getId());
                            TextView tappedTitle = (TextView) tappedTr.getChildAt(0);
                            TextView tappedDescription = (TextView) tappedTr.getChildAt(1);

                            Intent intent = new Intent(getApplicationContext(), AddBook.class);
                            intent.putExtra("id", id);
                            intent.putExtra("title", tappedTitle.getText().toString());
                            intent.putExtra("description", tappedDescription.getText().toString());
                            startActivity(intent);
                        } catch (Exception e){Log.e("HeaderClick", e.toString());}

                    }
                });
            }

            tableRow.addView(textView);


        }


        tableLayout.addView(tableRow);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

}
