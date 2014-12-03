package org.altervista.dorigotest.androidbookstore;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.altervista.dorigotest.androidbookstore.R;

abstract class BaseMenu extends Activity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Class destinationClass = null;
        String title = item.getTitle().toString();

        if(title.equals("Home"))
            destinationClass = Home.class;
        else if(title.equals("Books"))
            destinationClass = Books.class;
       else if(title.equals("Authors"))
            destinationClass = Authors.class;

        try {
            Intent intent = new Intent(getApplicationContext(), destinationClass);
            startActivity(intent);
        } catch(Exception e){}

        return super.onOptionsItemSelected(item);
    }

}