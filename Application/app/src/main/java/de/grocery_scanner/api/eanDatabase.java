package de.grocery_scanner.api;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class eanDatabase {

    private String ean;
    private String queryId = "400000000";
    private String URL;
    private Context context;
    private String result;


    public eanDatabase(String ean, String URL, Context context) {
        this.ean = ean;
        this.URL = URL;
        this.context = context;
    }

    public String getEan() {
        return ean;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setEan(String barCode) {
        this.ean = barCode;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getResult() {
        return result;
    }


    public void getProduct(final VolleyCallback callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest req = new StringRequest(Request.Method.GET, this.URL + "?ean=" + this.ean + "&cmd=query&queryid=" + this.queryId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result = transformText(response);
                callback.onSuccessResponse(result);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);

            }
        });

        requestQueue.add(req);
    }

    private String transformText(String text){

        String[] lines  = text.split(System.getProperty("line.separator"));

        for(String x : lines){
            if (x.contains("name=")){
                return x.substring(5);
            }
        }
        return null;
    }
}
