package com.example.aeroalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllUserRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<AlertLog> userRequestsList;
    private AlertLogAdapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_requests);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRequestsList = new ArrayList<>();
        adapter = new AlertLogAdapter(userRequestsList,this);
        recyclerView.setAdapter(adapter);
        progressDialog.show();

        fetchUserRequests();


        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                fetchUserRequests(); // Call the method to fetch user requests
                sendEmptyMessageDelayed(0, 3000); // Schedule the next execution in 3 seconds
            }
        };

        handler.sendEmptyMessage(0); // Start the first fetch
    }

    private void fetchUserRequests() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://aeroalert.pythonanywhere.com/api/alert_logs")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AllUserRequestsActivity.this, "Failed to fetch requests", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseData = response.body().string();
                Gson gson = new Gson();
                Type type = new TypeToken<List<AlertLog>>() {
                }.getType();
                final List<AlertLog> requests = gson.fromJson(responseData, type);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userRequestsList.clear();
                        userRequestsList.addAll(requests);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
