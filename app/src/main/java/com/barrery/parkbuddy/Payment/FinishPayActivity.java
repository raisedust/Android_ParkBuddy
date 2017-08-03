package com.barrery.parkbuddy.Payment;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.barrery.parkbuddy.Comment.CommentActivity;
import com.barrery.parkbuddy.PoiSearch.PoiAroundSearchActivity;
import com.barrery.parkbuddy.R;

public class FinishPayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_finish);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }

        Button finishPay = (Button) findViewById(R.id.finish_pay);
        finishPay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FinishPayActivity.this,PoiAroundSearchActivity.class);
                startActivity(intent);
            }
        });
        Button goNavi = (Button) findViewById(R.id.go_navi);
        goNavi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FinishPayActivity.this,CommentActivity.class);
                startActivity(intent);
            }
        });
    }
}
