package com.example.myapplication.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddBoardRequest extends StringRequest {

    final static private String URL ="http://holy97.cafe24.com/myphp/AddBoard.php";
    private Map<String, String> parameters;

    public AddBoardRequest(String bid, String superId , String boardType, String title , String content, Response.Listener<String> listener){

        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("bid",bid);
        parameters.put("superId",superId);
        parameters.put("boardType",boardType);
        parameters.put("title",title);
        parameters.put("content",content);

    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}