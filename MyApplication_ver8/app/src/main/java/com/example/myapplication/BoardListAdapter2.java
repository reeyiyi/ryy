package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class BoardListAdapter2 extends BaseAdapter {

    private Context context;
    private List<BoardInfo> boardList;

    public BoardListAdapter2(Context context, List<BoardInfo> boardList) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int i) {
        return boardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.board, null);

        TextView title = (TextView)v.findViewById(R.id.title_text);
        TextView content = (TextView)v.findViewById(R.id.content_text);
        TextView user = (TextView)v.findViewById(R.id.user_text);
        TextView date = (TextView)v.findViewById(R.id.date_text);

        title.setText(boardList.get(i).getTitle());
        content.setText(boardList.get(i).getContent());
        user.setText(boardList.get(i).getUserID());
        date.setText(boardList.get(i).getDate());

        v.setTag(boardList.get(i).getTitle());
        return v;
    }
}
