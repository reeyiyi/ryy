package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.DataClass.DataList;
import com.example.myapplication.DataClass.Meta;
import com.example.myapplication.DataClass.TestItem;
import com.example.myapplication.DataClass.UserDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_BookSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_BookSearch extends Fragment implements AbsListView.OnScrollListener {

    private ListView listView;
    private List<DataList> list;
    private ListViewAdapter adapter;
    private EditText editSearch;
    private TextView tv;

    private boolean lastItemVisibleFlag = false;
    private final int OFFSET = 10;
    private ProgressBar progressBar;
    private boolean mLockListView = false;

    private RetrofitAPI KAKAO_RetrofitAPI;
    private RetrofitAPI DB_RetrofitAPI;
    private Call<TestItem> KAKAO_CallJsonList;
    List<DataList> buf;
    Meta meta;

    private String text = "";
    InputMethodManager imm;
    private int last_flag;
    int tmp = 0;

    private int REQUEST_SEARCH = 1111;
    private int REQUEST_ADD = 2222;
    private int REQUEST;
    private String day;
    String uid = UserDTO.superId;
    private FragmentManager fragmentManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String KAKAO_SEARCH = "https://dapi.kakao.com";
    private static final String DB_CONNECT = "http://holy97.cafe24.com/myphp/";

    private String btitle, bauthor, bimage, byear;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_BookSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_BookSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_BookSearch newInstance(String param1, String param2) {
        Fragment_BookSearch fragment = new Fragment_BookSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_book_search, container, false);

        Bundle bundle = getArguments();
        fragmentManager = getActivity().getSupportFragmentManager();

        if (bundle != null) {
            REQUEST = bundle.getInt("REQUEST");
            day = bundle.getString("day");
        }
        //System.out.println(REQUEST);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        KAKAO_RetrofitAPI = setRetrofitInit(KAKAO_SEARCH);

        tv = (TextView) layout.findViewById(R.id.total_count);
        editSearch = (EditText) layout.findViewById(R.id.editSearch);
        listView = (ListView) layout.findViewById(R.id.listView);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressbar);

        list = new ArrayList<>();
        buf = new ArrayList<>();
        adapter = new ListViewAdapter(getContext(), list);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text = editSearch.getText().toString();
                search(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(REQUEST == REQUEST_SEARCH){
                    Intent intent = new Intent(getContext(),BoardActivity3.class);
                    intent.putExtra("btitle",list.get(position).getTitle());
                    startActivity(intent);
                }else if(REQUEST == REQUEST_ADD){
                    btitle = list.get(position).getTitle();
                    bauthor = list.get(position).getAuthors();
                    byear = list.get(position).getDatetime();
                    bimage = list.get(position).getThumbnail();
                    DB_RetrofitAPI = setRetrofitInit(DB_CONNECT);
                    Call<ResponseBody> call = DB_RetrofitAPI.insertBookInfo(btitle, bauthor, byear, bimage, uid, day);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast myToast = Toast.makeText(getContext(), "추가되었습니다.", Toast.LENGTH_SHORT);
                            myToast.show();
                            fragmentManager.beginTransaction().remove(Fragment_BookSearch.this).commit();
                            fragmentManager.popBackStack();
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast myToast = Toast.makeText(getContext(),"추가하지 못했습니다.", Toast.LENGTH_SHORT);
                            myToast.show();
                            fragmentManager.beginTransaction().remove(Fragment_BookSearch.this).commit();
                            fragmentManager.popBackStack();
                            getActivity().finish();
                        }
                    });
                }
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }

    private void search(String text) {
        tmp = 0;
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (text.length() == 0) {
            System.out.println("입력값 없음");
        }
        // 문자 입력을 할때..
        else
        {
            getItem(text);
            tmp++;
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private RetrofitAPI setRetrofitInit(String url){
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
        return mRetrofitAPI;
    }

    private void callBookList(RetrofitAPI mRetrofitAPI) {
        String token = "KakaoAK 5367a7332db7a550d96967c4bed6712f";
        KAKAO_CallJsonList = mRetrofitAPI.getBookInfo(token, text, tmp + 1, OFFSET);
        KAKAO_CallJsonList.enqueue(mRetrofitCallback);
        //System.out.println((tmp)+"페이지의 값 검색함");
    }


    private Callback<TestItem> mRetrofitCallback = new Callback<TestItem>() {
        @Override
        public void onResponse(Call<TestItem> call, Response<TestItem> response) {
            TestItem result = response.body();
            buf = result.mDatalist;
            meta = result.mMeta;
            last_flag = (meta.getPageable_count()-1) / OFFSET;
            tv.setText(Integer.toString(meta.getPageable_count())+"건의 검색 결과");
            //System.out.println(meta.getPageable_count());
        }

        @Override
        public void onFailure(Call<TestItem> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false && last_flag > 0 && last_flag+1 >= tmp) {

            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem(text);
            tmp++;
            //System.out.println("last : " + last_flag);
            //System.out.println("tmp : " + tmp);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem(String title){

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 다음 데이터를 불러와서 리스트에 저장한다.
        callBookList(KAKAO_RetrofitAPI);
        if (tmp == 1) {
            list.clear();
            callBookList(KAKAO_RetrofitAPI);
        }

        if(buf != null && buf.size() > 0) {
            for (int i = 0; i < buf.size(); i++) {
                list.add(buf.get(i));
                //System.out.println(buf.get(i).getTitle());
            }
            //list = buf;
        }else{ }
        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        }, 1000);
    }

}