package com.example.priyanka.pmkhw9;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortBy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                editText.setText("");

                editText = (EditText)findViewById(R.id.editText2);
                editText.setText("");

                editText = (EditText)findViewById(R.id.editText3);
                editText.setText("");

                Spinner sp = (Spinner)findViewById(R.id.spinner);
                sp.setSelection(0);
            }

        });


        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String keywords = new String();
                float min, max;
                EditText editText = (EditText) findViewById(R.id.editText);
                if (editText.getText().equals("")) {
                    TextView tv6 = (TextView) findViewById(R.id.textView6);
                    tv6.setText("Please enter keywords");
                } else {
                    keywords = editText.getText().toString();
                }

                String regex = "[0-9]+";
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                if (!editText2.getText().toString().matches(regex)) {
                    TextView tv6 = (TextView) findViewById(R.id.textView6);
                    tv6.setText("Please enter integers for Price From");
                }

                EditText editText3 = (EditText) findViewById(R.id.editText3);
                if (!editText3.getText().toString().matches(regex)) {
                    TextView tv6 = (TextView) findViewById(R.id.textView6);
                    tv6.setText("Please enter integers for Price To");
                }

                if (!editText3.getText().equals("") && Integer.parseInt(editText3.getText().toString()) < Integer.parseInt(editText2.getText().toString())) {
                    TextView tv6 = (TextView) findViewById(R.id.textView6);
                    tv6.setText("Max Price cannot be smaller than min Price");
                }

                min = Float.parseFloat(editText2.getText().toString());
                max = Float.parseFloat(editText3.getText().toString());

                String url = "http://pmk571hw8-env.elasticbeanstalk.com/?key_words=" + keywords + "&order=BestMatch&result_per_page=5&min=" + min + "&max=" + max + "&condition[0]=false&condition[1]=false&condition[2]=false&condition[3]=false&condition[4]=false&format[0]=false&format[1]=false&format[2]=false&seller=false&free_shipping=false&expedited=false&max_days=1&pageNumber=1";

                AsyncClass async = new AsyncClass();
                async.execute(url);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    class AsyncClass extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String...url) {

            String responseText = null;
            InputStream is = null;
            HttpClient client = new DefaultHttpClient();

            HttpUriRequest request = new HttpGet(url[0]);
            try {
                HttpResponse response = client.execute(request);
                is = response.getEntity().getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                responseText = br.readLine();
                return responseText;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseText;
        }


        @Override
        protected void onPostExecute(String responseText) {

            try {
                JSONObject jsonObj = new JSONObject(responseText);

                EditText et = (EditText) findViewById(R.id.editText);

                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("JsonObject",responseText);
                bundle.putString("keywords",et.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}
