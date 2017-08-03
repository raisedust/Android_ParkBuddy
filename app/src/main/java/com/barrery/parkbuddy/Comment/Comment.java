package com.barrery.parkbuddy.Comment;

import org.litepal.crud.DataSupport;

/**
 * Created by Barrery on 2017/7/6.
 */

public class Comment extends DataSupport {
    public int id;
    private String user_name;
    private String rating_value;
    private int user_image;
    private String user_comment;


    public String toString(){
        return  user_name + ':'+' '+' '+ ' '+' '+' '+' '+' '+' '+' '+' '+' '+' '+' '+rating_value+ ' ' + ' ' +  ' ' + ' '+ ' '+ ' '+ ' '
                + ' ' + ' ' + user_comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRating_value(){
        return rating_value;
    }
    public void setRating_value(String rating_value){
        this.rating_value = rating_value;
    }

    public int getUser_image() {
        return user_image;
    }

    public void setUser_image(int user_image) {
        this.user_image = user_image;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }
}
