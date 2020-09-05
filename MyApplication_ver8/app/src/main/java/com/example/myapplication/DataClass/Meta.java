package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

public class Meta{
    @SerializedName("total_count")
    private int total_count = 0;

    @SerializedName("pageable_count")
    private int pageable_count = 0;

    @SerializedName("is_end")
    private boolean is_end = false;

    public int getTotal_count(){return total_count;}
    public int getPageable_count(){return pageable_count;}
    public boolean getIsEnd(){return is_end;}
        }
