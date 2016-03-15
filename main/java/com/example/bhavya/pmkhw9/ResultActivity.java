package com.example.priyanka.pmkhw9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ResultActivity extends ActionBarActivity {

    String responseText = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle bundle = getIntent().getExtras();
        responseText = bundle.getString("JsonObject");
        String keywords = bundle.getString("keywords");
        JSONObject jsonObj = null;
        String title = new String();
        String shipping = new String();
        String itemString[] = new String[5];

        try {

            jsonObj = new JSONObject(responseText);

            for(int i=0; i<5; i++){

                String freeShipping = "";

                itemString[i] = new String();
                itemString[i] = jsonObj.getJSONObject("item"+i).getJSONObject("basicInfo").getString("title") + "\n";
                if(Float.parseFloat(jsonObj.getJSONObject("item"+i).getJSONObject("basicInfo").getString("shippingServiceCost")) > 0.0){
                    freeShipping = " (+$"+jsonObj.getJSONObject("item"+i).getJSONObject("basicInfo").getString("shippingServiceCost")+" for shipping)";
                }
                else{
                    freeShipping = " (FREE SHIPPING)";
                }
                itemString[i] += "Price: $"+jsonObj.getJSONObject("item"+i).getJSONObject("basicInfo").getString("convertedCurrentPrice")+""+freeShipping;
            }

            TextView tv = (TextView) findViewById(R.id.textView11);
            tv.setText(itemString[0]);
            tv = (TextView) findViewById(R.id.textView12);
            tv.setText(itemString[1]);
            tv = (TextView) findViewById(R.id.textView13);
            tv.setText(itemString[2]);
            tv = (TextView) findViewById(R.id.textView14);
            tv.setText(itemString[3]);
            tv = (TextView) findViewById(R.id.textView15);
            tv.setText(itemString[4]);
            tv = (TextView) findViewById(R.id.textView10);
            tv.setText("Results for '"+keywords+"'");


            AsyncClass async = new AsyncClass();
            String url1 = jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getString("galleryURL");
            String url2 = jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getString("galleryURL");
            String url3 = jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getString("galleryURL");
            String url4 = jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getString("galleryURL");
            String url5 = jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getString("galleryURL");
            async.execute(url1, url2, url3, url4, url5);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public void showDetails(){

        try {
            JSONObject jsonObj = new JSONObject(responseText);
            String itemDetails = jsonObj.getJSONObject("item0").toString();

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ItemDetails", itemDetails);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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



    private class AsyncClass extends AsyncTask<String, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(String...url) {

            Bitmap[] bm = new Bitmap[5];
            HttpClient client = new DefaultHttpClient();

            for(int i=0; i<5; i++){

                HttpUriRequest request = new HttpGet(url[i]);
                try {
                    HttpResponse response = client.execute(request);
                    InputStream is = null;
                    is = response.getEntity().getContent();

                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm[i] = BitmapFactory.decodeStream(bis);

                    return bm;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return bm;
        }


        @Override
        protected void onPostExecute(Bitmap[] bm) {
            ImageButton button[] = new ImageButton[5];
            button[0] = (ImageButton) findViewById(R.id.imageButton1);
            button[1] = (ImageButton) findViewById(R.id.imageButton2);
            button[2] = (ImageButton) findViewById(R.id.imageButton3);
            button[3] = (ImageButton) findViewById(R.id.imageButton4);
            button[4] = (ImageButton) findViewById(R.id.imageButton5);

            for(int i=0; i<5; i++){
                button[i].setImageBitmap(bm[i]);
            }

        }

    }

}
