package com.shopping.gway_4u;

import android.content.Context;
import android.content.SharedPreferences;

public class BackStacks {

    public Context context;

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;


    public void getInstance(Context context)
    {
        editor = context.getSharedPreferences("activity",Context.MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences("activity",Context.MODE_PRIVATE);
    }


    public void setCurrentActivity(String TAG)
    {
        editor.putString("activity",TAG);
        editor.apply();
        editor.commit();
    }

    public String getCurrentActivity()
    {
        return preferences.getString("activity",null);
    }

    public void clearAll()
    {
        editor.clear();
    }

}
