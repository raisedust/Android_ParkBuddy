package com.barrery.parkbuddy.Comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.barrery.parkbuddy.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MoreCommentActivity extends AppCompatActivity {
    private ListView Lv;
    List<Comment> commentList;              //创建一个List集合，将数据库中的每一个数据都加到此集合中
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_comment);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }

//        commentList = DataSupport.findAll(Comment.class);
//
//        Lv = (ListView) findViewById(R.id.more_comment);
//        MyAdapter myAdapter = new MyAdapter(this);
//        Lv.setAdapter(myAdapter);
        show_listview();


        Button deleteData = (Button) findViewById(R.id.delete_all);
        deleteData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DataSupport.deleteAll(Comment.class);
                show_listview();
            }
        });
    }
    public void show_listview(){
        commentList = DataSupport.findAll(Comment.class);

        Lv = (ListView) findViewById(R.id.more_comment);
        MyAdapter myAdapter = new MyAdapter(this);
        Lv.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context){
            this.context = context;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return commentList.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(MoreCommentActivity.this);
            Comment comments = commentList.get(position);
            tv.setText(comments.toString());
            return tv;
        }
    }
}

