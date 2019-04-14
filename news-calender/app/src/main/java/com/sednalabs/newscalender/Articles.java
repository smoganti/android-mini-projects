package com.sednalabs.newscalender;


import java.io.Serializable;

/**
 * Created by vamse on 4/20/2017.
 */

public class Articles implements Serializable {

    private String title;
    private String author;
    private String date;
    private String desc;
    private String  imgURL;
    private String url;

    public Articles(String title, String author, String date, String desc, String imgURL,String url) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.desc = desc;
        this.imgURL = imgURL;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString(){
        return title+author+date+desc+imgURL;
    }
}
