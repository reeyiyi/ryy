package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

public class DataList{
    @SerializedName("title")
    private String title = "";

    @SerializedName("contents")
    private String contents = "";

    @SerializedName("url")
    private String url = "";

    @SerializedName("isbn")
    private String isbn = "";

    @SerializedName("datetime")
    private String datetime = "";

    @SerializedName("publisher")
    private String publisher = "";

    @SerializedName("thumbnail")
    private String thumbnail = "";

    @SerializedName("status")
    private String status = "";

    @SerializedName("authors")
    private String[] authors = null;

    @SerializedName("translators")
    private String[] translators = null;

    @SerializedName("price")
    private int price = 0;

    @SerializedName("sale_price")
    private int sale_price = 0;

    public String getTitle(){return title;}
    public String getContents(){return contents;}
    public String getUrl(){return url;}
    public String getIsbn(){return isbn;}
    public String getPublisher(){return publisher;}
    public String getThumbnail(){return thumbnail;}
    public String getStatus(){return status;}

    public int getPrice(){return price;}
    public int getSale_price(){return sale_price;}

    public String getAuthors(){
        String text = "-";
        if(authors.length == 1) {
            text = authors[0];
        }else if(authors.length > 1){
            text = authors[0] + " 외 " + (authors.length - 1) + "명";
        }
        return text;
    }
    public String getTranslators(){
        String text = "-";
        if(translators.length == 1) {
            text = translators[0];
        }else if(translators.length > 1){
            text = translators[0] + " 외 " + (translators.length - 1) + "명";
        }
        return text;
    }

    public String getDatetime(){
        return datetime;}
}
