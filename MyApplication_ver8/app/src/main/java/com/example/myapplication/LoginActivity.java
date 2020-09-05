package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DataClass.UserDTO;
import com.example.myapplication.request.LoginRequest;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView registerButton ;
    EditText id_text ,pass_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        id_text = findViewById(R.id.idText);
        pass_text = findViewById(R.id.passwordText);


        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = id_text.getText().toString().trim();
                final String userPassword = pass_text.getText().toString().trim();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                String userID = jsonResponse.getString("userID");
                                String userPassword = jsonResponse.getString("userPassword");
                                String userName = jsonResponse.getString("userName");
                                String userNick = jsonResponse.getString("userNick");
                                String userEmail = jsonResponse.getString("userEmail");
                                String userGender = jsonResponse.getString("userGender");

                                UserDTO.superId = userID;
                                UserDTO.userName = userName;
                                UserDTO.userNick = userNick;
                                UserDTO.userEmail = userEmail;
                                UserDTO.userGender = userGender;


                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                                Toast.makeText(getApplicationContext(), userName + " 님으로 로그인 했습니다", Toast.LENGTH_LONG).show();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("아이디나 비밀번호를 확인하세요.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}