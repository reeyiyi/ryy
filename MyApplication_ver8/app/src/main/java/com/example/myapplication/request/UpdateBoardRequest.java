package com.example.myapplication.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateBoardRequest extends StringRequest {

    final static private String URL ="http://holy97.cafe24.com/myphp/BoardUpdate.php";
    private Map<String, String> parameters;

    public UpdateBoardRequest(String superId , String title , String content, String date, Response.Listener<String> listener){

        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("superId",superId);
        parameters.put("title",title);
        parameters.put("content",content);
        parameters.put("date",date);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}