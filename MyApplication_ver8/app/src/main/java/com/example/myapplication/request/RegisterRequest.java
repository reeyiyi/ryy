package com.example.myapplication.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL ="http://holy97.cafe24.com/myphp/UserSignUp.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String userNick, String userGender, String userEmail, Response.Listener<String> listener){

        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPassword",userPassword);
        parameters.put("userName",userName);
        parameters.put("userNick",userNick);
        parameters.put("userGender",userGender);
        parameters.put("userEmail",userEmail);

    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
