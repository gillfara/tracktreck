package com.example.myapplication;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        Button btn = findViewById(R.id.reg_btn);
        EditText username_vw = findViewById(R.id.username);
        EditText email_vw = findViewById(R.id.email);
        EditText password_vw = findViewById(R.id.password);
        EditText confirm_password_vw = findViewById(R.id.confirm_password);
        TextView login_vw = findViewById(R.id.login);

        login_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                String username = username_vw.getText().toString();
                String email = email_vw.getText().toString();
                String password = password_vw.getText().toString();
                String confirm_password = confirm_password_vw.getText().toString();
                if (username.isEmpty()||email.isEmpty()||password.isEmpty()||confirm_password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"you must fill all ",Toast.LENGTH_LONG).show();
                }else {
                    if (!password.equals(confirm_password)){
                        Toast.makeText(RegisterActivity.this,"password must be equal to confirmpassword",Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username",username);
                            params.put("email",email);
                            params.put("password",password);
                            StringEntity entit = new StringEntity(params.toString(),"UTF-8");
                            client.post(RegisterActivity.this,"http://192.168.43.151/register",entit,"application/json",new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                    super.onSuccess(statusCode, headers, response);
                                    if (statusCode == 200){
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }
                                }


                            });
                        }catch (JSONException e){
                            Toast.makeText(RegisterActivity.this,"an error occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
//                client.get("http://192.168.43.151/users", new JsonHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
////
////                        String out = null;
////                        for (int i = 0; i < response.length(); i++) {
////                            try {
////                                JSONObject item = response.getJSONObject(i);
////                                Toast.makeText(RegisterActivity.this, item.toString(), Toast.LENGTH_LONG).show();
////
////                            } catch (JSONException e) {
//////                                e.printStackTrace();
////                                Toast.makeText(RegisterActivity.this, "error parsing json", Toast.LENGTH_LONG).show();
////                            }
////
//////
////                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                        Log.e("users", "fail" + e.toString());
//                        String users = response.toString();
//                        Toast.makeText(RegisterActivity.this, users, Toast.LENGTH_LONG).show();
//                        ;
//                    }
//
//                });
//                JSONObject params = new JSONObject();
//                try {
//                    params.put("username","gilbert");
//                    params.put("password","123");
//
//                }catch (JSONException e){};
//                StringEntity entity = new StringEntity(params.toString(),"UTF-8");
//
//                client.post(RegisterActivity.this,"http://192.168.43.151/login", entity,"application/json",new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] header, JSONObject response) {
//                        try {
//                            String token = response.getString("token");
//                            String type = response.getString("type");
//
//                            Toast.makeText(RegisterActivity.this,type,Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_LONG).show();
//                            Log.d("registeractivity",e.toString());
//
//                        }
//                    }
//
//                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}