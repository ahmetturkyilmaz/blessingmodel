package com.example.blessing_model.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    /*    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(prayers);
        editor.putString("prayers", json);
        editor.apply();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("prayers", null);

        Type type = new TypeToken<ArrayList<Prayer>>() {
        }.getType();
        prayers = gson.fromJson(json, type);

        if (prayers == null) {
            HashMap<String, String> prayerList = new HashMap<>();
            prayers = new ArrayList<>();

        }
    }*/
}
