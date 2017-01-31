package com.anthonydunk.deputychallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartShift(View v)
    {
        final Activity finalThis = this;
        //Thread thread = new Thread(new Runnable() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //@Override
            //public void run() {
                try  {
                    //Toast.makeText(finalThis, "Start shift", Toast.LENGTH_LONG).show();
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
                            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            while ((line = rd.readLine()) != null) {
                                result.append(line);
                                Toast.makeText(finalThis, line, Toast.LENGTH_LONG).show();
                            }
                            rd.close();

                        }
                        else{
                            Toast.makeText(finalThis, "Failed", Toast.LENGTH_LONG).show();
                        }
          /*
            URL url = new URL("https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/business");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Deputy 1aae64f96017f0387614fe85a9312a1ca804cae3");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                Toast.makeText(this,output,Toast.LENGTH_LONG);
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();
*/
                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(finalThis, e.toString(), Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        //});

        //thread.start();

    }

    public void onEndShift(View v)
    {
        try {
            String url = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc/shift/end";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject json = new JSONObject();
            json.put("time","2017-01-17T06:35:57+00:00");
            json.put("latitude","-33.0");
            json.put("longitude","151.0");



            //add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Deputy 1aae64f96017f0387614fe85a9312a1ca804cae3");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

        }
        catch (Exception e)
        {

        }
    }

    public void onShiftDetails(View v)
    {
        // Switch activity!
        Intent myIntent = new Intent(v.getContext(),
                ShiftListActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
