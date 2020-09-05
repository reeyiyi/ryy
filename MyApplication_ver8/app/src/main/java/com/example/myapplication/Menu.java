package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Menu extends AppCompatActivity {

    Button record,taste,type;
    String day, btitle, bauthors, thumbnail;
    String bid ;
    @Override
    protected void onResume() {
        super.onResume();
        new getBook().execute();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //인텐트로 받아온 데이터
        Intent intent_sub = getIntent();
        day = intent_sub.getStringExtra("day");
        btitle = intent_sub.getStringExtra("btitle");

        bauthors = intent_sub.getStringExtra("bauthors");
        thumbnail = intent_sub.getStringExtra("thumbnail");

        record = findViewById(R.id.record_btn);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,RecordActivity.class);
                intent.putExtra("bid",bid);
                startActivity(intent);
            }
        });
        taste = findViewById(R.id.taste_btn);
        taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,TasteActivity.class);
                intent.putExtra("day",day);
                intent.putExtra("btitle", btitle);
                intent.putExtra("bauthors", bauthors);
                intent.putExtra("thumbnail", thumbnail);
                startActivity(intent);
            }
        });
        type = findViewById(R.id.type_btn);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,BoardActivity2.class);
                intent.putExtra("bid",bid);
                startActivity(intent);
            }
        });
    }
    class getBook extends AsyncTask<Void, Void, String> {

        String target;

        protected void onPreExecute() {

            target = "http://holy97.cafe24.com/myphp/SelectBid.php?btitle="+btitle;

        }
        @Override
        protected String doInBackground(Void... voids) {

            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    bid = object.getString("BID");
                    Log.d("test",bid);
                    count++;
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}