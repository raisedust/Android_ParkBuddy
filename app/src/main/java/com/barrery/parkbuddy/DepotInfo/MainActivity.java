package com.barrery.parkbuddy.DepotInfo;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.barrery.parkbuddy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Depot[] depots = {
            new Depot("停车场1  5星",R.drawable.depot1),new Depot("停车场2  5星",R.drawable.depot2),
            new Depot("停车场3  5星",R.drawable.depot3),new Depot("停车场4  5星",R.drawable.depot4),
            new Depot("停车场5  5星",R.drawable.depot5),new Depot("停车场6  5星",R.drawable.depot6)};
    private List<Depot> depotList = new ArrayList<>();
    private DepotAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);          //设置first_layout为MainActivity中的布局
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }

        initDepots();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DepotAdapter(depotList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                refreshDepots();
            }
        });

    }
    private void initDepots(){
        depotList.clear();
        for (int i=0; i<8;i++){
            Random random = new Random();
            int index = random.nextInt(depots.length);
            depotList.add(depots[index]);
        }
    }
    private void refreshDepots(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initDepots();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


}
