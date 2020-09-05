package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadDays {
    @SerializedName("documents")
    public List<String> days;

    public List<String> getDays(){return days;}
}
