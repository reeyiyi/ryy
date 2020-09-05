package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DataClass.Gamsang;
import com.example.myapplication.DataClass.UserDTO;
import com.example.myapplication.request.AddBoardRequest;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordActivity extends AppCompatActivity {

    Button btn;

    private RetrofitAPI mRetrofitAPI;
    private String url ="http://192.168.219.173:5000/";
    ProgressDialog dialog;
    String boardType;
    String bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //아름 추가: 감정분석한 결과도 DB에 저장
        mRetrofitAPI = setRetrofitInit();

        Intent intent = getIntent();
        bid = intent.getStringExtra("bid");
        final EditText title = findViewById(R.id.titleText);
        final EditText content = findViewById(R.id.contentText);
        btn = findViewById(R.id.success_btn);
        final RadioGroup selectGroup = (RadioGroup) findViewById(R.id.selectgroup);
        int selectGroupID = selectGroup.getCheckedRadioButtonId();
        boardType = ((RadioButton) findViewById(selectGroupID)).getText().toString();

        selectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectButton = (RadioButton) findViewById(i);
                boardType = selectButton.getText().toString();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String title1 = title.getText().toString();
                    String content1 = content.getText().toString();
                    String superId= UserDTO.superId;
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(),"작성 완료했습니다",Toast.LENGTH_SHORT).show();
                                    // showProgressDialog();
                                    //                                    //아름 추가: 감정분석한 결과도 DB에 저장
                                    //                                    Call<ResponseBody> call = mRetrofitAPI.emotionAnalysis(title1, content1, superId);
                                    //                                    call.enqueue(new Callback<ResponseBody>() {
                                    //                                        @Override
                                    //                                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    //                                            Toast.makeText(getApplicationContext(),"분석 완료했습니다",Toast.LENGTH_SHORT).show();
                                    //                                            dialog.dismiss();
                                                                               finish();
                                    //                                        }
                                    //
                                    //                                        @Override
                                    //                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    //                                            t.printStackTrace();
                                    //                                            Toast.makeText(getApplicationContext(),"분석 실패했습니다",Toast.LENGTH_SHORT).show();
                                    //                                            dialog.dismiss();
                                    //                                            finish();
                                    //                                        }
                                    //                                    });


                                } else {
                                    Toast.makeText(getApplicationContext(),"작성 실패했습니다",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AddBoardRequest addBoardRequest = new AddBoardRequest(bid ,superId, boardType,title1, content1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RecordActivity.this);
                    queue.add(addBoardRequest);
                }
        });
    }

    private RetrofitAPI setRetrofitInit(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .writeTimeout(20, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        RetrofitAPI mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
        return mRetrofitAPI;
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("감정 분석중");
        dialog.show();
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