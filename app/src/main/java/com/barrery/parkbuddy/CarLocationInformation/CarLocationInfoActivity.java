package com.barrery.parkbuddy.CarLocationInformation;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.barrery.parkbuddy.Comment.MoreCommentActivity;
import com.barrery.parkbuddy.Payment.PaymentActivity;
import com.barrery.parkbuddy.R;

import java.util.ArrayList;
import java.util.List;

public class CarLocationInfoActivity extends AppCompatActivity {

    private List<Car> carlist = new ArrayList<>();
    /*
    用于得到车场名称
     */
    String TCtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_location_info);
        /*
        接收PoiAroundSearchActivity的信息,用于提取当前车场名称
         */
        Intent intent=getIntent();
        TCtitle=intent.getStringExtra("mCPoi");
        /*
        用于隐藏系统自带的标题栏
         */
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }

        initCars();
        CarAdaper adaper = new CarAdaper(CarLocationInfoActivity.this,R.layout.car_item,carlist);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adaper);

        Button pay = (Button) findViewById(R.id.pay);
        //pay.setBackgroundColor(Color.rgb(65,69,69));
        pay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent( CarLocationInfoActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
        Button usersComment = (Button) findViewById(R.id.users_comment);
        usersComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CarLocationInfoActivity.this, MoreCommentActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initCars(){
        Car car_name = new Car("车场名称：" + TCtitle.toString(),R.drawable.depot_name);
        carlist.add(car_name);
        Car car_price = new Car("车位单价：15元/小时",R.drawable.money);
        carlist.add(car_price);
        Car car_remainder = new Car("车位剩余：3位",R.drawable.chewei1);
        carlist.add(car_remainder);
    }
}
