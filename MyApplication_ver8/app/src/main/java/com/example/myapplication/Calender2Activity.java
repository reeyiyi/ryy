package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataClass.DataList;
import com.example.myapplication.DataClass.Gamsang;
import com.example.myapplication.DataClass.GamsangList;
import com.example.myapplication.DataClass.Meta;
import com.example.myapplication.DataClass.TestItem;
import com.example.myapplication.DataClass.UserDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Calender2Activity extends AppCompatActivity {

    TextView date,id;
    private ListView listView;
    private List<GamsangList> list, buf;
    private ListViewAdapter_GamsangList adapter;
    private RetrofitAPI RetrofitAPI;
    private Call<Gamsang> CallJsonList;
    String uid = UserDTO.superId;
    private String day;
    private String url ="http://holy97.cafe24.com/myphp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender2);

        date = findViewById(R.id.date_id);
        id = findViewById(R.id.user_id);
        listView = (ListView) findViewById(R.id.gs_list);

        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        date.setText(day);

        id.setText(uid);

        RetrofitAPI = setRetrofitInit();

        list = new ArrayList<>();
        callReadList(RetrofitAPI);
        adapter = new ListViewAdapter_GamsangList(this, list);
        adapter.setDate(day);
        //System.out.println(adapter.getCount());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 요소 클릭시 이동하는 부분
                String title = list.get(position).getTitle();
                String authors = list.get(position).getAuthors();
                String thumbnail = list.get(position).getThumbnail();
                Intent intent  = new Intent(Calender2Activity.this, Menu.class);
                intent.putExtra("day",day);
                intent.putExtra("btitle", title);
                intent.putExtra("bauthors", authors);
                intent.putExtra("thumbnail", thumbnail);
                startActivity(intent);
            }
        });
    }

    private RetrofitAPI setRetrofitInit(){
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
        return mRetrofitAPI;
    }

    private void callReadList(RetrofitAPI mRetrofitAPI) {
        CallJsonList = mRetrofitAPI.getReadInfo(uid, day);
        CallJsonList.enqueue(mRetrofitCallback);
    }


    private Callback<Gamsang> mRetrofitCallback = new Callback<Gamsang>() {
        @Override
        public void onResponse(Call<Gamsang> call, Response<Gamsang> response) {
            Gamsang result = response.body();
            buf = result.mDatalist;
            for (int i = 0; i < buf.size(); i++) {
                list.add(buf.get(i));
                System.out.println(list.get(i).getTitle());
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<Gamsang> call, Throwable t) {
            t.printStackTrace();
        }
    };
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
