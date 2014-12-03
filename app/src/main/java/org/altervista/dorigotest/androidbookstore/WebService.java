package org.altervista.dorigotest.androidbookstore;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea.dorigo on 25/07/14.
 */
public class WebService {

    public JSONObject execute(String url, Map parameters) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry innerMap = (Map.Entry)it.next();

            pairs.add(new BasicNameValuePair(innerMap.getKey().toString(), innerMap.getValue().toString()));
            it.remove(); // avoids a ConcurrentModificationException
        }


        post.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = client.execute(post);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line);
        }

        return new JSONObject(builder.toString());

    }
}
