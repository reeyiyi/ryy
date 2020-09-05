package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private ListView boardListView;
    private BoardListAdapter adapter;
    private List<BoardInfo> boardList;
    String pub = "공개";
    @Override
    protected void onResume() {
        super.onResume();
        boardList = new ArrayList<BoardInfo>();
        new board().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        boardListView = (ListView)findViewById(R.id.board_list);
        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(BoardActivity.this , BoardDetailActivity.class);
                intent.putExtra("title" , boardList.get(position).title);
                intent.putExtra("content" , boardList.get(position).content);
                intent.putExtra("date" , boardList.get(position).date);
                intent.putExtra("userID" , boardList.get(position).userID);
                startActivity(intent);
            }
        });

    }
    class board extends AsyncTask<Void, Void, String> {

        String target;

        protected void onPreExecute() {

            target = "http://holy97.cafe24.com/myphp/BoardList.php?pub="+pub;

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
                String userID , title , content, date ;
                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    userID = object.getString("UID");
                    title =object.getString("EBTITLE");
                    content = object.getString("EBCONTENT");
                    date = object.getString("EBDATE");

                    BoardInfo boardInfo = new BoardInfo(userID, title, content ,date);
                    boardList.add(boardInfo);
                    count++;
                }

                adapter = new BoardListAdapter(getApplicationContext(),boardList);
                boardListView.setAdapter(adapter);
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

