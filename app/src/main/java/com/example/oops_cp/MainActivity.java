package com.example.oops_cp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText et1;
    TextView tv1,tv2;
    //Button tosearch;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;
    public String cityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = findViewById(R.id.et1);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        //tosearch = findViewById(R.id.tosearch);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//comment
        String url1="https://api.weatherapi.com/v1/current.json?key=8728aaad07a8409c90b155708231704&q=mumbai&aqi=no";
        RequestQueue queue1=Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest  request1=new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj=response.getJSONObject("current");
                    JSONObject obj2=response.getJSONObject("location");
                    String cityDisplay=obj2.getString("name");
                    String temperature=obj.getString("temp_c");
                    String pressure=obj.getString("pressure_in");
                    tv2.setText(cityDisplay.toUpperCase()+"\nTemp:"+temperature+" °C\nPressure: "+pressure);

                } catch (JSONException e) {
                    tv2.setText(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv2.setText("something went wrong");
            }
        });
        queue1.add(request1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();//comment else
        }
//        tosearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, Search.class));
//            }
//        });
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Search.class));
            }
        });
    }

    private void startLocationUpdates() {
        // Define a location listener
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Get the latitude and longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (!addresses.isEmpty()) {
                        String cityName = addresses.get(0).getLocality();
                        cityMain=cityName;
                        // Display the city name
                        String url="https://api.weatherapi.com/v1/current.json?key=8728aaad07a8409c90b155708231704&q="+cityMain+"&aqi=no";
                        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject obj=response.getJSONObject("current");
                                    JSONObject obj2=response.getJSONObject("location");
                                    String cityDisplay=obj2.getString("name");
                                    String temperature=obj.getString("temp_c");
                                    String pressure=obj.getString("pressure_in");
                                    tv1.setText(cityDisplay.toUpperCase()+"\nTemp:"+temperature+" °C\nPressure: "+pressure);

                                } catch (JSONException e) {
                                    tv1.setText(e.toString());
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tv1.setText("Something went wrong");
                            }
                        });
                        queue.add(request);
                    }
                    else {
                        tv1.setText("wait a moment");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    tv1.setText(e.toString());

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start listening for location updates
                startLocationUpdates();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

//    public void get(View v){
//        String apiKey="8728aaad07a8409c90b155708231704";
//        String city=et.getText().toString();
//
//        String url="https://api.weatherapi.com/v1/current.json?key=8728aaad07a8409c90b155708231704&q="+cityMain+"&aqi=no";
//        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
//        JsonObjectRequest  request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject obj=response.getJSONObject("current");
//                    JSONObject obj2=response.getJSONObject("location");
//                    String cityDisplay=obj2.getString("name");
//                    String temperature=obj.getString("temp_c");
//                    String pressure=obj.getString("pressure_in");
//                    tv.setText(cityDisplay.toUpperCase()+"\nTemp:"+temperature+" °C\nPressure: "+pressure);
//
//                } catch (JSONException e) {
//                    tv.setText(e.toString());
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv.setText("PLEASE ENTER CITY AGAIN");
//
//            }
//        });
//        queue.add(request);
//    }
}