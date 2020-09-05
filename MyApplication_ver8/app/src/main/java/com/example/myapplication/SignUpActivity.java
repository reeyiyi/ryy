package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.request.RegisterRequest;
import com.example.myapplication.request.ValidateRequest;

import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private String userGender;
    private boolean validate = false;
    EditText idText ,passwordText , nameText, nickText , emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final RadioGroup selectGroup = (RadioGroup) findViewById(R.id.selectgroup);
        int selectGroupID = selectGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton) findViewById(selectGroupID)).getText().toString();

        selectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectButton = (RadioButton) findViewById(i);
                userGender = selectButton.getText().toString();
            }
        });

        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);
        nameText = findViewById(R.id.nameText);
        nickText = findViewById(R.id.nickText);
        emailText = findViewById(R.id.emailText);

        final Button validateButton = (Button) findViewById(R.id.validate_btn);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final String userID = idText.getText().toString();
                    if (validate) {
                        return;
                    }
                    if (userID.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                    builder.setMessage(userID + " 은/는 사용할 수 있는 아이디입니다. 사용하시겠습니까?")
                                            .setPositiveButton("확인",  new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    idText.setEnabled(false);
                                                    validateButton.setEnabled(false);
                                                    validate = true;
                                                    Toast.makeText(getApplicationContext(),"아이디는 " + userID + " 입니다",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(),"아이디를 다시 입력해주세요",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    builder.show();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                    dialog = builder.setMessage("이미 있는 아이디입니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    queue.add(validateRequest);
                }
        });

        Button sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    String userID = idText.getText().toString();
                    String userPassword = passwordText.getText().toString();
                    String userEmail = emailText.getText().toString();
                    String userNick = nickText.getText().toString();
                    String userName = nameText.getText().toString();



                    if (userID.equals("") || userPassword.equals("") || userName.equals("") || userNick.equals("") || userEmail.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        return;

                    }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    Toast.makeText(getApplicationContext(),"회원등록에 성공했습니다", Toast.LENGTH_SHORT).show();
                                    finish();


                                } else {
                                    Toast.makeText(getApplicationContext(),"회원등록에 실패했습니다", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPassword,userName, userNick, userGender, userEmail, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    queue.add(registerRequest);

                }
        });

    }
}