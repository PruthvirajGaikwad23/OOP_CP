package com.example.oops_cp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    //private static final int REQUEST_LOCATION_PERMISSION = 1;

    //private LocationManager locationManager;
    public String cityMain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        tolocation = findViewById(R.id.tolocation);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//comment
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        } else {
//           startLocationUpdates();//comment else
//             }
        tolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Search.this, MainActivity.class));
            }
        });

    }

//    private void startLocationUpdates() {
//        // Define a location listener
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                // Get the latitude and longitude
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                String url="http://api.openweathermap.org/geo/1.0/reverse?lat=51.5098&lon=-0.1180&limit=5&appid={API key}";
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                    if (!addresses.isEmpty()) {
//                        String cityName = addresses.get(0).getLocality();
//                        cityMain=cityName;
//                        // Display the city name
//                        Toast.makeText(getApplicationContext(),  cityMain, Toast.LENGTH_SHORT).show();
//
//                    }
//                    else {
//                        tv.setText("wait a moment");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    tv.setText(e.toString());
//
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_LOCATION_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, start listening for location updates
//                startLocationUpdates();
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message to the user)
//            }
//        }
//    }

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
                    tv.setText(cityDisplay.toUpperCase()+"\nTemp:"+temperature+" °C\nPressure: "+pressure);

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