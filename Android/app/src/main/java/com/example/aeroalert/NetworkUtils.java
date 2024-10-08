package com.example.aeroalert;

import android.util.Log;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class NetworkUtils {
    private static final String BASE_URL = "https://aeroalert.pythonanywhere.com";
    private static final String INVALID_VEHICLES_ENDPOINT = "/api/invalid_vehicles";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new GsonBuilder().create();

    public static void addInvalidVehicle(InvalidVehicle vehicle) {
        String url = BASE_URL + INVALID_VEHICLES_ENDPOINT;

        String json = gson.toJson(vehicle);
        Log.d("NetworkUtils", "addInvalidVehicle JSON: " + json); // Debugging
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("Response: " + responseBody);
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
            }
        });
    }

    public static void getInvalidVehicles(Callback callback) {
        String url = BASE_URL + INVALID_VEHICLES_ENDPOINT;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void updateInvalidVehicle1(String vehicleNo, InvalidVehicle updatedVehicle, Callback callback) {
        String url = BASE_URL + "/api/update_invalid_vehicle/" + vehicleNo;



        String json = gson.toJson(updatedVehicle);
        Log.d("url", "updateInvalidVehicle JSON: " + url); // Debugging
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void updateInvalidVehicle(String vehicleNo, InvalidVehicle updatedVehicle, Callback callback) {
        String url = BASE_URL + "/api/update_invalid_vehicle/" + vehicleNo;

        String json = gson.toJson(updatedVehicle);
        Log.d("NetworkUtils", "updateInvalidVehicle JSON: " + json); // Debugging
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void deleteInvalidVehicle(int id, Callback callback) {
        String url = BASE_URL + "/api/delete_invalid_vehicle/" + id;

        Log.e("url", url); // Debugging
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static List<InvalidVehicle> parseInvalidVehicles(String json) {
        Type listType = new TypeToken<List<InvalidVehicle>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
