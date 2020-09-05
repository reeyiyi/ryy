package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GamsangList{
    @SerializedName("thumbnail")
    private String thumbnail = "";

    @SerializedName("title")
    private String title = "";

    @SerializedName("authors")
    private String authors = "";

    public String getThumbnail() {return thumbnail;}
    public String getTitle() {return title;}
    public String getAuthors() {return authors;}
}
