package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.myapplication.CalendarClass.EventDecorator;
import com.example.myapplication.CalendarClass.OneDayDecorator;
import com.example.myapplication.CalendarClass.SaturdayDecorator;
import com.example.myapplication.CalendarClass.SundayDecorator;
import com.example.myapplication.DataClass.ReadDays;
import com.example.myapplication.DataClass.Taste;
import com.example.myapplication.DataClass.TasteList;
import com.example.myapplication.DataClass.UserDTO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarActivity extends AppCompatActivity {
    MaterialCalendarView materialCalendarView;
    private RetrofitAPI mRetrofitAPI;
    private Call<ReadDays> mCallJsonList;
    private static String url = "http://192.168.219.173:5000/";
    private List<String> list, buf;
    private static String uid = UserDTO.superId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mRetrofitAPI = setRetrofitInit();

        list = new ArrayList<>();
        callDaysList(mRetrofitAPI);

        materialCalendarView = findViewById(R.id.calendarView);

        materialCalendarView.setTileHeightDp((int) (standardSize_Y / 10));

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator()
        );

        List<String> result;

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                //Log.i("Year test", Year + "");
                //Log.i("Month test", Month + "");
                //Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;

                //Log.i("shot_Day test", shot_Day + "");
                //materialCalendarView.clearSelection();

                Intent intent  = new Intent(CalendarActivity.this,SelectActivity.class);
                intent.putExtra("day",shot_Day);
                startActivity(intent);
            }
        });
    }

    public void decoCal(List<String> result) {
        Observable.just(result)
                .map(new Function<List<String>, ArrayList<CalendarDay>>(){
                    @Override
                    public ArrayList<CalendarDay> apply(List<String> Time_Result) throws Throwable {
                        Calendar calendar = Calendar.getInstance();
                        ArrayList<CalendarDay> dates = new ArrayList<>();

                        //현재 날짜 표시
                        //dates.add(CalendarDay.from(calendar));

                        //result에 있는 날짜 표시
                        for(int i = 0; i < Time_Result.size() ; i++){
                            String tmp = Time_Result.get(i);
                            String[] time = tmp.split("-");
                            int year = Integer.parseInt(time[0]);
                            int month = Integer.parseInt(time[1]);
                            int dayy = Integer.parseInt(time[2]);

                            //System.out.println(year+" "+month+" "+dayy);

                            calendar.set(year,month-1,dayy);
                            CalendarDay day = CalendarDay.from(calendar);
                            dates.add(day);
                        }
                        System.out.println(dates);
                        return dates;
                    }
                })
                .subscribe(new Consumer<ArrayList<CalendarDay>>(){
                    @Override
                    public void accept(ArrayList<CalendarDay> calendarDays) throws Throwable {
                        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#ff7761"), calendarDays, CalendarActivity.this, materialCalendarView));
                    }
                });
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return  size;
    }

    int standardSize_X, standardSize_Y;
    float density;

    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
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

    private void callDaysList(RetrofitAPI mRetrofitAPI) {
        mCallJsonList = mRetrofitAPI.getReadDays(uid);
        mCallJsonList.enqueue(mRetrofitCallback);
        //System.out.println((tmp)+"페이지의 값 검색함");
    }


    private Callback<ReadDays> mRetrofitCallback = new Callback<ReadDays>() {
        @Override
        public void onResponse(Call<ReadDays> call, Response<ReadDays> response) {
            ReadDays result = response.body();
            buf = result.days;
                for (int i = 0; i < buf.size(); i++) {
                    list.add(buf.get(i));
                }
                decoCal(list);
            }

        @Override
        public void onFailure(Call<ReadDays> call, Throwable t) {
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
