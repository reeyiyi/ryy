package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataClass.GamsangList;
import com.example.myapplication.DataClass.TasteList;

import java.util.List;

public class ListViewAdapter_Taste extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private List<TasteList> list;

    public ListViewAdapter_Taste(Context context, List<TasteList> list){
        // MainActivity 에서 데이터 리스트를 넘겨 받는다.
        this.list = list;
        this.context = context;
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
            convertView = View.inflate(context, R.layout.taste_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.similarity = (TextView) convertView.findViewById(R.id.similarity);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.book_cover);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 각 셀에 넘겨받은 텍스트 데이터를 넣는다.
        viewHolder.title.setText((CharSequence) list.get(position).getTitle());
        viewHolder.similarity.setText(Float.toString(list.get(position).getSimilarity()));
        viewHolder.author.setText(list.get(position).getAuthors());
        String url = list.get(position).getThumbnail();

        Glide.with(convertView)
                .load(url)
                .thumbnail(0.1f)
                .override(140, 210)
                .into(viewHolder.cover);
        return convertView;
    }

    class ViewHolder{
        public TextView title;
        public TextView similarity;
        public TextView author;
        public ImageView cover;
    }


}

