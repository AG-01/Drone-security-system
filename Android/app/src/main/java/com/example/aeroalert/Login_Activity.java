package com.example.aeroalert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aeroalert.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login_Activity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private OkHttpClient client;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        emailEditText = findViewById(R.id.id_login_email);
        passwordEditText = findViewById(R.id.id_login_password);
        loginButton = findViewById(R.id.id_login_btnLogin);

        client = new OkHttpClient();
        gson = new Gson();


        UserSession userSession = new UserSession(getApplicationContext());
        if (userSession.getFirstName().equalsIgnoreCase("gaurd")) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
//
//        findViewById(R.id.tv_goToregister).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), GenerateFormActivity.class));
//
//            }
//        });


        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();


            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("password is required");
                return;
            }
            if (password.length() < 6) {
                passwordEditText.setError("Password must have at least 6 characters");
                return;
            }


            login(email, password);
        });
    }

    private void login(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);
        String jsonString = gson.toJson(json);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url("https://aeroalert.pythonanywhere.com/gaurdLogin") // Replace with your server's IP address
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);
                    runOnUiThread(() -> Toast.makeText(Login_Activity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show());


                    UserSession userSession = new UserSession(getApplicationContext());
                    userSession.setFirstName("gaurd");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));


                } else {
                    runOnUiThread(() -> Toast.makeText(Login_Activity.this, "Login failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    public class LoginResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        // Setter
    }
}
