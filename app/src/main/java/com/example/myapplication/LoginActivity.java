package com.example.myapplication;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        AccountManager am = AccountManager.get(this);
//        get login views
        TextView username_vw = findViewById(R.id.username);
        TextView passwors_vw = findViewById(R.id.password);
        Button login_btn = findViewById(R.id.login);

//      set on click listener on the button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_vw.getText().toString();
                String password = passwors_vw.getText().toString();
                if (username.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"fill all the fields",Toast.LENGTH_SHORT).show();
                }else {
                    AsyncHttpClient client = new AsyncHttpClient();
//                    String headertoken;
                    JSONObject params = new JSONObject();
                    try {
                        params.put("username",username);
                        params.put("password",password);
                        StringEntity payload = new  StringEntity(params.toString(),"UTF-8");
                        client.post(LoginActivity.this,"http://192.168.43.151/login",payload,"application/json",new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                super.onSuccess(statusCode, headers, response);
                                try {
                                    String token = response.getString("token");
                                    String type = response.getString("type");
                                    String headertoken = type+" "+token;
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }catch (JSONException e){}
                                ;

                            }
                            @Override
                            public void onFailure(int statusCode,Header[] header,Throwable e,JSONObject response){
                                Toast.makeText(LoginActivity.this,"user not found try again",Toast.LENGTH_SHORT).show();
                            }

                        });
                    }catch (JSONException e){}
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}