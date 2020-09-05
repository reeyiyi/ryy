package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DataClass.UserDTO;
import com.example.myapplication.request.UpdateBoardRequest;

import org.json.JSONObject;

public class BoardUpdateActivity extends AppCompatActivity {

    Button btn;
    String title_text,content_text,date_text;
    EditText title,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardupdate);

         title = findViewById(R.id.titleText);
         content = findViewById(R.id.contentText);
        Intent intent = getIntent();
        title_text = intent.getStringExtra("title");
        content_text = intent.getStringExtra("content");
        date_text = intent.getStringExtra("date");

        title.setText(title_text);
        content.setText(content_text);
        btn = findViewById(R.id.success_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String title1 = title.getText().toString();
                    String content1 = content.getText().toString();
                    String date1 = date_text;
                    String superId= UserDTO.superId;
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    Toast.makeText(getApplicationContext(),"수정 완료했습니다",Toast.LENGTH_SHORT).show();
                                    finish();


                                } else {
                                    Toast.makeText(getApplicationContext(),"수정 실패했습니다",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest(superId, title1, content1, date1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(BoardUpdateActivity.this);
                    queue.add(updateBoardRequest);
                }
        });
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