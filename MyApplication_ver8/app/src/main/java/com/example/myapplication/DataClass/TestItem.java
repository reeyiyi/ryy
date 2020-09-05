package com.example.myapplication.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestItem {

    @SerializedName("meta")
    public Meta mMeta;

    @SerializedName("documents")
    public List<DataList> mDatalist;

}

