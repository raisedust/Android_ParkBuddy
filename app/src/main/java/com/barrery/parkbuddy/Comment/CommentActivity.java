package com.barrery.parkbuddy.Comment;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.barrery.parkbuddy.R;

import org.litepal.LitePal;

public class CommentActivity extends AppCompatActivity {
    /*
    用于设置评价星星的值
     */
    RatingBar ratingBar;
    String ratingVal;
    /*
    用于设置显示的名字
     */
    private String usersName;
    /*
    用于显示评价的信息
     */
    private EditText commentEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentEdit = (EditText) findViewById(R.id.comment_edit);       //得到编辑评价的id

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {                          //隐藏系统自带的标题栏
            actionbar.hide();
        }
        /*
        监听匿名选择
         */
        Switch anonyMity = (Switch) findViewById(R.id.switch2);
        anonyMity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // 开启switch，设置提示信息
                    usersName = "匿     名";
                } else {
                    // 关闭swtich，设置提示信息
                    usersName = "Barrery";
                }
            }
        });
        /*
        将五颗星星的值得到，并显示出来
         */
        ratingBar=(RatingBar)findViewById(R.id.ratingBar3);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
//                Toast.makeText(getApplication(), "rating:"+String.valueOf(rating),Toast.LENGTH_SHORT).show();
                ratingVal = String.valueOf(rating);
            }
        });

        Button createDatabase = (Button) findViewById(R.id.show_more);
        createDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LitePal.getDatabase();
                Intent intent = new Intent(CommentActivity.this,MoreCommentActivity.class);
                startActivity(intent);
            }
        });
        Button submitComment = (Button) findViewById(R.id.submit_comment);
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CommentActivity.this,"You clicked submit button",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplication(), "rating:"+ratingVal,Toast.LENGTH_SHORT).show();
                String comment_edit = commentEdit.getText().toString();
                Comment comment = new Comment();
                comment.setId(1);
                comment.setUser_name(usersName);
                comment.setRating_value(ratingVal);
                comment.setUser_image(1);
                comment.setUser_comment(comment_edit);
                comment.save();

            }
        });

    }
}
