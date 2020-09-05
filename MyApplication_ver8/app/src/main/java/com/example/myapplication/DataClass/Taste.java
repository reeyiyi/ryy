package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Taste {
    @SerializedName("documents")
    public List<TasteList> mDatalist;
}
