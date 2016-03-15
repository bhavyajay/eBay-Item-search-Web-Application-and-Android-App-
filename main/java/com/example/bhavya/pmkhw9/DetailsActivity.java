package com.example.priyanka.pmkhw9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class DetailsActivity extends ActionBarActivity {

    String itemDetails = "";
    String url = "";
    JSONObject jsonObj = null;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        itemDetails = bundle.getString("ItemDetails");

        try {
            jsonObj = new JSONObject(itemDetails);
            if(jsonObj.getJSONObject("basicInfo").getString("pictureURLSuperSize") != null){
                url = jsonObj.getJSONObject("basicInfo").getString("pictureURLSuperSize");
            }
            else{
                url = jsonObj.getJSONObject("basicInfo").getString("galleryURL");
            }

            String URL = java.net.URLDecoder.decode(jsonObj.getJSONObject("basicInfo").getString("pictureURLSuperSize"), "UTF-8");

            if(URL == null)
                URL = java.net.URLDecoder.decode(jsonObj.getJSONObject("basicInfo").getString("galleryURL"), "UTF-8");

            AsyncClass async = new AsyncClass();
            async.execute(URL);

            TextView title = (TextView) findViewById(R.id.textView8);

            String shipping = "";

            Float zero = 0f;
            if(!jsonObj.getJSONObject("basicInfo").getString("shippingServiceCost").equals("")) {
                if (Float.parseFloat(jsonObj.getJSONObject("basicInfo").getString("shippingServiceCost")) > zero) {
                    shipping = " (+$" + jsonObj.getJSONObject("basicInfo").getString("shippingServiceCost") + " for shipping)";
                }
            }
            else{
                shipping = " (FREE SHIPPING)";
            }
            String total_price= "Price: $"+jsonObj.getJSONObject("basicInfo").getString("convertedCurrentPrice")+""+shipping;

            String str = java.net.URLDecoder.decode(jsonObj.getJSONObject("basicInfo").getString("title").toString());
            title.setText(str);

            title = (TextView) findViewById(R.id.textView9);
            title.setText(total_price);
            title.setTextColor(Color.DKGRAY);

            title = (TextView) findViewById(R.id.textView7);
            title.setText(java.net.URLDecoder.decode(jsonObj.getJSONObject("basicInfo").getString("location").toString()));
            title.setTextColor(Color.GRAY);


            ImageView image_top_rated = (ImageView) findViewById(R.id.iv3);
            image_top_rated.setImageResource(R.drawable.fb);




        }catch(JSONException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        TableLayout basic = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.VISIBLE);

        TableLayout seller = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        TableLayout shipping = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        try {

            TextView tv = (TextView) findViewById(R.id.textView21);
            tv.setText("Category Name");
            tv = (TextView) findViewById(R.id.textView22);
            tv.setText(jsonObj.getJSONObject("basicInfo").getString("categoryName"));

            tv = (TextView) findViewById(R.id.textView23);
            tv.setText("Condition");
            tv = (TextView) findViewById(R.id.textView24);
            tv.setText(jsonObj.getJSONObject("basicInfo").getString("conditionDisplayName"));

            String format = null;
            if(jsonObj.getJSONObject("basicInfo").getString("listingType").equals("FixedPrice") || jsonObj.getJSONObject("basicInfo").getString("listingType").equals("StoreInventory")){
                format = "Buy It Now";
            }
            else if(jsonObj.getJSONObject("basicInfo").getString("Auction").equals("FixedPrice")){
                format = "Auction";
            }
            else if(jsonObj.getJSONObject("basicInfo").getString("listingType").equals("Classified")){
                format = "Classified Ad";
            }
            else{
                format = jsonObj.getJSONObject("basicInfo").getString("Auction");
            }
            tv = (TextView) findViewById(R.id.textView25);
            tv.setText("Buying Format");
            tv = (TextView) findViewById(R.id.textView26);
            tv.setText(format);

        }catch(JSONException e){
            e.printStackTrace();
        }


        AsyncClass async = new AsyncClass();
        async.execute(url);

        basicInfo(view);
    }

    public void basicInfo(View view) {

       TableLayout basic = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.VISIBLE);

        TableLayout seller = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        TableLayout shipping = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        try {

            TextView tv = (TextView) findViewById(R.id.textView21);
            tv.setText("Category Name");
            tv = (TextView) findViewById(R.id.textView22);
            tv.setText(jsonObj.getJSONObject("basicInfo").getString("categoryName"));

            tv = (TextView) findViewById(R.id.textView23);
            tv.setText("Condition");
            tv = (TextView) findViewById(R.id.textView24);
            tv.setText(jsonObj.getJSONObject("basicInfo").getString("conditionDisplayName"));

            String format = null;
            if(jsonObj.getJSONObject("basicInfo").getString("listingType").equals("FixedPrice") || jsonObj.getJSONObject("basicInfo").getString("listingType").equals("StoreInventory")){
                format = "Buy It Now";
            }
            else if(jsonObj.getJSONObject("basicInfo").getString("listingType").equals("Auction")){
                format = "Auction";
            }
            else if(jsonObj.getJSONObject("basicInfo").getString("listingType").equals("Classified")){
                format = "Classified Ad";
            }
            else{
                format = jsonObj.getJSONObject("basicInfo").getString("listingType");
            }
            tv = (TextView) findViewById(R.id.textView25);
            tv.setText("Buying Format");
            tv = (TextView) findViewById(R.id.textView26);
            tv.setText(format);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    public void sellerInfo(View view) {

        TableLayout basic = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        TableLayout seller = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.VISIBLE);

        TableLayout shipping = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        try {

            TextView tv;
            ImageView iv;

            String username = "N/A";
            if(jsonObj.getJSONObject("sellerInfo").getString("sellerUserName") != null  && !jsonObj.getJSONObject("sellerInfo").getString("sellerUserName").equals("")){
                username = jsonObj.getJSONObject("sellerInfo").getString("sellerUserName");
            }
            tv = (TextView) findViewById(R.id.textView31);
            tv.setText("User Name");
            tv = (TextView) findViewById(R.id.textView32);
            tv.setText(username);

            String feedbackScore = "N/A";
            if(jsonObj.getJSONObject("sellerInfo").getString("feedbackScore") != null && !jsonObj.getJSONObject("sellerInfo").getString("feedbackScore").equals("")){
                feedbackScore = jsonObj.getJSONObject("sellerInfo").getString("feedbackScore");
            }
            tv = (TextView) findViewById(R.id.textView33);
            tv.setText("Feedback Score");
            tv = (TextView) findViewById(R.id.textView34);
            tv.setText(feedbackScore);

            String posFeedback = "N/A";
            if(jsonObj.getJSONObject("sellerInfo").getString("feedbackPositivePercent") != null && !jsonObj.getJSONObject("sellerInfo").getString("feedbackPositivePercent").equals("")){
                posFeedback = jsonObj.getJSONObject("sellerInfo").getString("feedbackPositivePercent");
            }
            tv = (TextView) findViewById(R.id.textView35);
            tv.setText("Positive Feedback");
            tv = (TextView) findViewById(R.id.textView36);
            tv.setText(posFeedback);

            String rating = "N/A";
            if(jsonObj.getJSONObject("sellerInfo").getString("feedbackRatingStar") != null && !jsonObj.getJSONObject("sellerInfo").getString("feedbackRatingStar").equals("")){
                rating = jsonObj.getJSONObject("sellerInfo").getString("feedbackRatingStar");
            }
            tv = (TextView) findViewById(R.id.textView37);
            tv.setText("Feedback Rating");
            tv = (TextView) findViewById(R.id.textView38);
            tv.setText(rating);

            if(jsonObj.getJSONObject("sellerInfo").getString("topRatedSeller") == "true"){
                iv = (ImageView) findViewById(R.id.imageView310);
                iv.setImageResource(R.drawable.top);
            }
            tv = (TextView) findViewById(R.id.textView39);
            tv.setText("Top Rated");



            String store = "N/A";
            if(jsonObj.getJSONObject("sellerInfo").getString("sellerStoreName") != null && !jsonObj.getJSONObject("sellerInfo").getString("sellerStoreName").equals("")){
                store = jsonObj.getJSONObject("sellerInfo").getString("sellerStoreName");
            }
            tv = (TextView) findViewById(R.id.textView311);
            tv.setText("Store");
            tv = (TextView) findViewById(R.id.textView312);
            tv.setText(store);



        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    public void shippingInfo(View view) {

        TableLayout basic = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        TableLayout seller = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.INVISIBLE);

        TableLayout shipping = (TableLayout) findViewById(R.id.basic);
        basic.setVisibility(View.VISIBLE);

        try {

            TextView tv;
            ImageView iv;

            String type = "N/A";
            if(jsonObj.getJSONObject("shippingInfo").getString("shippingType") != null && !jsonObj.getJSONObject("shippingInfo").getString("shippingType").equals("")){
                type = jsonObj.getJSONObject("shippingInfo").getString("shippingType");
            }
            tv = (TextView) findViewById(R.id.textView41);
            tv.setText("Shipping Type");
            tv = (TextView) findViewById(R.id.textView42);
            tv.setText(type);

            String time = "N/A";
            if(jsonObj.getJSONObject("shippingInfo").getString("handlingTime") != null && !jsonObj.getJSONObject("shippingInfo").getString("handlingTime").equals("")){
                time = jsonObj.getJSONObject("shippingInfo").getString("handlingTime");
            }
            tv = (TextView) findViewById(R.id.textView43);
            tv.setText("Handling Time");
            tv = (TextView) findViewById(R.id.textView44);
            tv.setText(time);

            String loc = "N/A";
            if(jsonObj.getJSONObject("shippingInfo").getString("shipToLocations") != null && !jsonObj.getJSONObject("shippingInfo").getString("shipToLocations").equals("")){
                loc = jsonObj.getJSONObject("shippingInfo").getString("shipToLocations");
            }
            tv = (TextView) findViewById(R.id.textView45);
            tv.setText("Shipping Locations");
            tv = (TextView) findViewById(R.id.textView46);
            tv.setText(loc);

            tv = (TextView) findViewById(R.id.textView47);
            tv.setText("Expedited Shipping");
            if(jsonObj.getJSONObject("shippingInfo").getString("expeditedShipping") == "true"){
                iv = (ImageView) findViewById(R.id.imageView48);
                iv.setImageResource(R.drawable.tick);
            }
            else{
                iv = (ImageView) findViewById(R.id.imageView48);
                iv.setImageResource(R.drawable.cross);
            }

            tv = (TextView) findViewById(R.id.textView49);
            tv.setText("One Day Shipping");
            if(jsonObj.getJSONObject("shippingInfo").getString("oneDayShippingAvailable") == "true"){
                iv = (ImageView) findViewById(R.id.imageView410);
                iv.setImageResource(R.drawable.tick);
            }
            else{
                iv = (ImageView) findViewById(R.id.imageView410);
                iv.setImageResource(R.drawable.cross);
            }

            tv = (TextView) findViewById(R.id.textView411);
            tv.setText("Returns Accepted");
            if(jsonObj.getJSONObject("shippingInfo").getString("returnsAccepted") == "true"){
                iv = (ImageView) findViewById(R.id.imageView412);
                iv.setImageResource(R.drawable.tick);
            }
            else{
                iv = (ImageView) findViewById(R.id.imageView412);
                iv.setImageResource(R.drawable.cross);
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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


    private class AsyncClass extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {

            Bitmap bm = null;
            Log.d("Object is:", "In B");
            HttpClient client = new DefaultHttpClient();

            HttpUriRequest request = new HttpGet(url[0]);
            try {
                Log.d("Object is:", "In try");
                HttpResponse response = client.execute(request);
                InputStream is = null;
                is = response.getEntity().getContent();

                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);

                return bm;

            } catch (IOException e) {
                Log.d("Object is:", "In catch");
                e.printStackTrace();
            }


            return bm;
        }


        @Override
        protected void onPostExecute(Bitmap bm) {
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(bm);
        }

    }


    }
