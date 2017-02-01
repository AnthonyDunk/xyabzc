package com.anthonydunk.deputychallenge;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static java.net.Proxy.Type.HTTP;

import com.google.gson.*;

public class MainActivity extends AppCompatActivity {

    ShiftsDatabase mDB;

    LocationManager m_locationProvider;
    LocationListener m_locationListener;

    boolean mHaveLocation = false;
    double mCurrentLatitude = 0.0;
    double mCurrentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate local cache database from server
        mDB = new ShiftsDatabase(this);
        retrieveShiftData();

        // Request location updates
        m_locationProvider = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (m_locationProvider!=null) {
            m_locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    mHaveLocation = true;
                    mCurrentLatitude = location.getLatitude();
                    mCurrentLongitude = location.getLongitude();
                    System.out.println(Double.toString(mCurrentLatitude)+","+Double.toString(mCurrentLongitude));
                }


                public void onProviderDisabled(String arg0) {
                    // TODO Auto-generated method stub

                }

                public void onProviderEnabled(String arg0) {
                    // TODO Auto-generated method stub

                }

                public void onStatusChanged(String arg0, int arg1,
                                            Bundle arg2) {
                    // TODO Auto-generated method stub

                }
            };

            // Minimum GPS location update: 5 seconds or 10 metres
            m_locationProvider.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10.0f, m_locationListener);
        }
        else
        {
            System.out.println("No permission to use location !");

        }
    }

    public void startEndShift(boolean startShift, Date time, String lat, String lon)
    {
        //
        // Send a request to start or end a shift
        //

        final Activity finalThis = this;
        final String finalLat = lat;
        final String finalLon = lon;
        final boolean finalStart = startShift;
        final Date finalTime = time;

        Thread thread = new Thread(new Runnable() {

            public void run() {

                try {
                    String url;
                    if (finalStart)
                        url = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/shift/start";
                    else
                        url = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/shift/end";

                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestMethod("POST");

                    ShiftStartEndData shiftStart = new ShiftStartEndData();
                    shiftStart.time = Utility.timeToTimeString(finalTime);
                    shiftStart.latitude = finalLat;
                    shiftStart.longitude = finalLon;

                    JSONObject json = new JSONObject();
                    json.put("time", shiftStart.time);
                    json.put("latitude", shiftStart.latitude);
                    json.put("longitude", shiftStart.longitude);

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Authorization", "Deputy 1aae64f96017f0387614fe85a9312a1ca804cae3");

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(json.toString());
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        requestShiftData();
                    }
                    else {
                        System.out.println("POST request failed!");
                    }
                } catch (Exception e) {

                }
            }
        });

        thread.start();

    }

    public void requestShiftData()
    {
        //
        // Get shift data from web API and cache in local database
        //
        try  {

            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL("https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/shifts");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Deputy 1aae64f96017f0387614fe85a9312a1ca804cae3");
                int responseCode = conn.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success

                    mDB.DeleteAllShifts();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    while ((line = rd.readLine()) != null) {
                        result.append(line);

                        JSONArray json = new JSONArray(line);
                        for (int n=0; n<json.length(); n++) {
                            String s = json.get(n).toString();
                            ShiftDetails shift = new Gson().fromJson(s, ShiftDetails.class);
                            System.out.println(shift.start+" "+shift.end);
                            mDB.AddShift(shift);
                        }

                        int [] shiftIDs = mDB.GetShiftIDs();
                        System.out.println(Integer.toString(shiftIDs.length)+" shifts");
                    }

                    rd.close();

                }
                else{
                    System.out.println("GET request failed!");
                }

            } catch (Exception e) {

                e.printStackTrace();
                System.out.println(e.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retrieveShiftData() {

        final Handler handler = new Handler();


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                requestShiftData();

                handler.post(new Runnable(){
                    public void run() {
                        TextView tvStatus = (TextView)findViewById(R.id.textStatus);
                        tvStatus.setVisibility(View.INVISIBLE);

                        Button butStartShift = (Button)findViewById(R.id.buttonStartShift);
                        butStartShift.setEnabled(true);

                        Button butEndShift = (Button)findViewById(R.id.buttonEndShift);
                        butEndShift.setEnabled(true);

                        Button butShiftDetails = (Button)findViewById(R.id.buttonShiftDetails);
                        butShiftDetails.setEnabled(true);
                    }
                });
            }

        });

        thread.start();

    }

    public void onStartShift(View v)
    {
        Date now = new Date();
        DecimalFormat df = new DecimalFormat("#.###");
        startEndShift(true,now,df.format(mCurrentLatitude),df.format(mCurrentLongitude));
    }

    public void onEndShift(View v)
    {
        Date now = new Date();
        DecimalFormat df = new DecimalFormat("#.###");
        startEndShift(false,now,df.format(mCurrentLatitude),df.format(mCurrentLongitude));
    }

    public void onShiftDetails(View v)
    {
        // Switch activity!
        Intent myIntent = new Intent(v.getContext(),
                ShiftListActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
