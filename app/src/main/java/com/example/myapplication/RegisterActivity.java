package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.register_button);
        EditText usernameView = findViewById(R.id.username);
        EditText emailView = findViewById(R.id.email);
        EditText passwordView = findViewById(R.id.password);
        EditText confirmPasswordView = findViewById(R.id.confirm_password);
        TextView loginView = findViewById(R.id.login_option);


        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameView.getText().toString();
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
                String confirmPassword = confirmPasswordView.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else {
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    } else {
                        registerUser(username, email, password);
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser(String username, String email, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");

            client.post(RegisterActivity.this, "http://192.168.43.151/register", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("RegisterActivity", "Error: " + throwable.getMessage());
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(RegisterActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            Log.e("RegisterActivity", "JSON error: " + e.getMessage());
        }
    }
}
