package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataClass.DataList;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private List<DataList> list;
    private int REQUEST;
    private int REQUEST_SEARCH = 1111;
    private int REQUEST_ADD = 2222;


    public ListViewAdapter(Context context, List<DataList> list){
        // MainActivity 에서 데이터 리스트를 넘겨 받는다.
        this.list = list;
        this.context = context;
    }

    public void setREQUEST(int code){
        this.REQUEST = code;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.row_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.book_click = (LinearLayout) convertView.findViewById(R.id.book_click);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.book_cover);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 각 셀에 넘겨받은 텍스트 데이터를 넣는다.
        viewHolder.title.setText((CharSequence) list.get(position).getTitle());
        viewHolder.price.setText("정가 "+ Integer.toString(list.get(position).getPrice())+"원");
        viewHolder.info.setText((CharSequence) (list.get(position).getAuthors()+" | "+list.get(position).getPublisher()));
        String url = list.get(position).getThumbnail();

        Glide.with(convertView)
                .load(url)
                .thumbnail(0.1f)
                .override(140, 210)
                .into(viewHolder.cover);
        return convertView;
    }

    class ViewHolder{
        public LinearLayout book_click;
        public TextView title;
        public TextView price;
        public TextView info;
        public ImageView cover;
    }


}

