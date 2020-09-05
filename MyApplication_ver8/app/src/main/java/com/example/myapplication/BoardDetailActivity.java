package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataClass.UserDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BoardDetailActivity extends AppCompatActivity {

    TextView title_text,date_text,content_text,user_text;
    Button update,delete;
    String userID;
    String real_user,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarddetail);


        final Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        final String content = intent.getStringExtra("content");
        userID = intent.getStringExtra("userID");

        title_text = findViewById(R.id.titleText);
        title_text.setText(title);

        date_text = findViewById(R.id.dateText);
        date_text.setText(date);

        content_text = findViewById(R.id.contentText);
        content_text.setText(content);

        user_text = findViewById(R.id.userText);
        user_text.setText(userID);

        update = findViewById(R.id.update_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BoardDetailActivity.this,BoardUpdateActivity.class);
                intent1.putExtra("title",title);
                intent1.putExtra("content",content);
                intent1.putExtra("date",date);
                startActivity(intent1);
                finish();
            }
        });
        delete = findViewById(R.id.delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardDetailActivity.this);
                alert_confirm.setTitle("삭제").setMessage("정말 게시글을 삭제 하시겠어요?").setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteTask().execute();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });
        new validate().execute();
    }
    class DeleteTask extends AsyncTask<Void, Void, String>
    {

        String target;

        @Override
        protected void onPreExecute() {
            target = "http://holy97.cafe24.com/myphp/BoardDelete.php?superId="+userID +"&date="+date;
        }

        @Override
        protected String doInBackground(Void... voids){
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

            Toast.makeText(getApplicationContext() , "게시물을 삭제 했습니다 " , Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class validate extends AsyncTask<Void, Void, String> {

        String target;

        protected void onPreExecute() {

            target = "http://holy97.cafe24.com/myphp/UidValidate.php?superId="+ UserDTO.superId;

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
                    real_user = object.getString("UID");
                    Log.d("test",real_user);
                    count++;
                }
                if (userID.equals(real_user)){
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu1:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.menu2:
                finish();
                break;
            case R.id.menu3:
                finish();
                Toast myToast = Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT);
                myToast.show();
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
