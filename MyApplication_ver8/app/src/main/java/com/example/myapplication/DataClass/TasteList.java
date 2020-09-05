package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

public class TasteList {
    @SerializedName("bid")
    private int bid = 0;

    @SerializedName("title")
    private String title = "";

    @SerializedName("authors")
    private String authors = "";

    @SerializedName("thumbnail")
    private String thumbnail = "";

    @SerializedName("similarity")
    private float similarity = 0;

    public String getThumbnail() {return thumbnail;}
    public String getTitle() {return title;}
    public String getAuthors() {return authors;}
    public float getSimilarity() {return similarity;}
}
