package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchActivity extends AppCompatActivity {
    private int REQUEST_SEARCH = 1111;
    private int REQUEST_ADD = 2222;

    private FragmentManager fragmentManager;
    private Bundle bundle;

    private Fragment search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        int REQUEST = intent.getExtras().getInt("REQUEST");
        String day = intent.getStringExtra("day");

        System.out.println("SearchActivity : "+REQUEST);
        fragmentManager = getSupportFragmentManager();
        search = new Fragment_BookSearch();

        bundle = new Bundle(1);
        if(REQUEST == REQUEST_SEARCH) {
            bundle.putInt("REQUEST", REQUEST_SEARCH);
        }else if(REQUEST == REQUEST_ADD){
            bundle.putInt("REQUEST", REQUEST_ADD);
            bundle.putString("day", day);
        }
        search.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, search).commit();
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
