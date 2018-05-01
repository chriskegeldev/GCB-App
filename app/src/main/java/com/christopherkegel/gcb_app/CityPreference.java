package com.christopherkegel.gcb_app;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by christopherkegel on 24.12.14.
 */
public class CityPreference {
    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    String getCity(){
        return prefs.getString("city", "Zimmerhof");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
