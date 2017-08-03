package com.barrery.parkbuddy.Payment;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.barrery.parkbuddy.Comment.CommentActivity;
import com.barrery.parkbuddy.Payment.dialog.CommonDialog;
import com.barrery.parkbuddy.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements CustomerKeyboard.CustomerKeyboardClickListener,
        PasswordEditText.PasswordFullListener, View.OnClickListener {

    private List<PayType> payTypelist = new ArrayList<>();          //用于存放支付类型

    private Button pay;
    CustomerKeyboard mCustomerKeyboard;
    PasswordEditText mPasswordEditText;

    /*
    用于确定停车时间
     */
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }

        initPayType();
        PayAdaper adaper = new PayAdaper(PaymentActivity.this, R.layout.pay_type_item,payTypelist);
        ListView listView = (ListView) findViewById(R.id.list_view2);
        listView.setAdapter(adaper);

        pay = (Button) findViewById(R.id.pay_sure);
        pay.setOnClickListener(this);

        /*
        确定支付时间
         */
        Button submitTime = (Button) findViewById(R.id.submit_time);
        submitTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText parkTime = (EditText) findViewById(R.id.park_time);
                TextView showPrice = (TextView) findViewById(R.id.show_price);
                int intTime = Integer.parseInt( parkTime.getText().toString());
                showPrice.setText(String.valueOf(intTime*15)+"元");
            }
        });

    }

    /*
    确定支付类型
     */
    private void initPayType(){
        PayType payType_zhifubao = new PayType("支付宝支付",R.mipmap.zhifubao);
        payTypelist.add(payType_zhifubao);
        PayType payType_weixin = new PayType("微信支付",R.mipmap.weixin);
        payTypelist.add(payType_weixin);
    }

    @Override
    public void onClick(View v) {
        final CommonDialog.Builder builder = new CommonDialog.Builder(this).fullWidth().fromBottom()
                .setView(R.layout.dialog_customer_keyboard);
        builder.setOnClickListener(R.id.delete_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.create().show();
        mCustomerKeyboard = builder.getView(R.id.custom_key_board);
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this);
        mPasswordEditText =  builder.getView(R.id.password_edit_text);
        mPasswordEditText.setOnPasswordFullListener(this);
    }

    @Override
    public void click(String number) {
        mPasswordEditText.addPassword(number);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
    }

    @Override
    public void passwordFull(String password) {
        Intent intent = new Intent(PaymentActivity.this,FinishPayActivity.class);
        startActivity(intent);
        for(int i = 5; i >=0; i--){
            mPasswordEditText.deleteLastPassword();
        }
//        Toast.makeText(this, "密码填充完毕：" + password, Toast.LENGTH_SHORT).show();
    }
}
