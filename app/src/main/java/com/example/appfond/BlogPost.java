package com.example.appfond;

import java.sql.Date;
import java.sql.Timestamp;

public class BlogPost {
    public String id, title, text, date_post_txt, image;
    public Date date_post;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate_post_txt(String date_post_txt) {
        this.date_post_txt = date_post_txt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate_post(Date date_post) {
        this.date_post = date_post;
    }

    public BlogPost(String id, String title, String text, String date_post_txt, String image) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date_post_txt = date_post_txt;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate_post_txt() {
        return date_post_txt;
    }

    public String getImage() {
        return image;
    }



}
