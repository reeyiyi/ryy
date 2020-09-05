package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DataClass.GamsangList;
import com.example.myapplication.DataClass.Taste;
import com.example.myapplication.DataClass.TasteList;
import com.example.myapplication.DataClass.TestItem;
import com.example.myapplication.DataClass.UserDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TasteActivity extends AppCompatActivity {

    private RetrofitAPI mRetrofitAPI;
    private Call<Taste> mCallJsonList;
    ProgressDialog dialog;
    private static String url = "http://192.168.219.173:5000/";
    String day, btitle, bauthors, thumbnail;
    TextView title;
    private ListView listView;
    private List<TasteList> list, buf;
    private ListViewAdapter_Taste adapter;
    private static String uid = UserDTO.superId;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taste);
        activity = TasteActivity.this;

        Intent intent_sub = getIntent();
        day = intent_sub.getStringExtra("day");
        btitle = intent_sub.getStringExtra("btitle");
        bauthors = intent_sub.getStringExtra("bauthors");
        thumbnail = intent_sub.getStringExtra("thumbnail");

        listView = (ListView) findViewById(R.id.taste_list);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(btitle+"과 유사한 책");

        mRetrofitAPI = setRetrofitInit();

        list = new ArrayList<>();
        callSimilarityList(mRetrofitAPI);
        adapter = new ListViewAdapter_Taste(this, list);
        //System.out.println(adapter.getCount());
        listView.setAdapter(adapter);

        showProgressDialog();


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

    private void callSimilarityList(RetrofitAPI mRetrofitAPI) {
        mCallJsonList = mRetrofitAPI.similarityAnalysis(btitle, bauthors, thumbnail);
        mCallJsonList.enqueue(mRetrofitCallback);
        //System.out.println((tmp)+"페이지의 값 검색함");
    }


    private Callback<Taste> mRetrofitCallback = new Callback<Taste>() {
        @Override
        public void onResponse(Call<Taste> call, Response<Taste> response) {
            Taste result = response.body();
            try {
                buf = result.mDatalist;
                for (int i = 0; i < buf.size(); i++) {
                    list.add(buf.get(i));
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "분석 완료했습니다", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Intent intent = new Intent(getApplicationContext(), NoResult.class);
                startActivity(intent);
                e.printStackTrace();
            }

            dialog.dismiss();
        }

        @Override
        public void onFailure(Call<Taste> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(getApplicationContext(),"분석 실패했습니다",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("유사한 책 탐색중");
        dialog.show();
    }

    /**
     * Object type 변수가 비어있는지 체크
     *
     * @param obj
     * @return Boolean : true / false
     */
    public static Boolean empty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    /**
     * Object type 변수가 비어있지 않은지 체크
     *
     * @param obj
     * @return Boolean : true / false
     */
    public static Boolean notEmpty(Object obj) {
        return !empty(obj);
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