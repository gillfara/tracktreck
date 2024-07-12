    package com.example.myapplication;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;

    import org.json.JSONException;
    import org.json.JSONObject;

    import cz.msebera.android.httpclient.Header;
    import cz.msebera.android.httpclient.entity.StringEntity;

    public class LoginActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            Button loginBtn = findViewById(R.id.login);
            EditText usernameView = findViewById(R.id.username);
            EditText passwordView = findViewById(R.id.password);
            TextView registerView = findViewById(R.id.create_account);

            registerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    String username = usernameView.getText().toString();
                    String password = passwordView.getText().toString();

                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "You must fill all fields", Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username", username);
                            params.put("password", password);
                            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
                            client.post(LoginActivity.this, "http://192.168.43.151/login", entity, "application/json", new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    if (statusCode == 200) {

                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
