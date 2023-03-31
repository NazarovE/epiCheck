package com.example.appfond;

public class ImageForCol {
    public String img_path,id_post;

    public ImageForCol() {

    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }


    public ImageForCol(String img_path, String id_post) {
        this.img_path = img_path;
        this.id_post = id_post;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getId_post() {
        return id_post;
    }



}

