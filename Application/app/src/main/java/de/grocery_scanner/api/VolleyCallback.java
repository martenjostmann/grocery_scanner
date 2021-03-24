package de.grocery_scanner.api;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onSuccessResponse(String result);
    void onErrorResponse(VolleyError error);
}
