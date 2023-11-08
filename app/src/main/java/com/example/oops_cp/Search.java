package com.example.oops_cp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;

public class Search extends AppCompatActivity {
    EditText et;
    TextView tv;
    ImageView tolocation;
    public String cityMain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        tolocation = findViewById(R.id.tolocation);
        et.setSelection(et.getText().length());
        if (et.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        tolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Search.this, MainActivity.class));
            }
        });

    }
    public void get(View v){
        String apiKey="8728aaad07a8409c90b155708231704";
        String city=et.getText().toString();

        String url="https://api.weatherapi.com/v1/current.json?key=8728aaad07a8409c90b155708231704&q="+city+"&aqi=no";
        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest  request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj=response.getJSONObject("current");
                    JSONObject obj2=response.getJSONObject("location");
                    String cityDisplay=obj2.getString("name");
                    String temperature=obj.getString("temp_c");
                    String pressure=obj.getString("pressure_in");
                    String humidity=obj.getString("humidity");
                    tv.setText(cityDisplay.toUpperCase()+"\nTemp:"+temperature+" °C\nPressure: "+pressure+"\nHumidity:"+humidity);

                } catch (JSONException e) {
                    tv.setText(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("PLEASE ENTER CITY AGAIN");

            }
        });
        queue.add(request);
    }
}