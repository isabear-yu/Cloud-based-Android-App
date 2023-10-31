package edu.cmu.myipinfo;
import android.os.AsyncTask;
import android.os.Build;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.annotation.RequiresApi;

public class MyIP {
    MainActivity ip = null;

    /*
     * search is the public MyIP method.  Its arguments are the search term, and the MainActivity object that called it.  This provides a callback
     * path such that the ipReady method in that object is called when the IP data is available from the search.
     */
    public void search(String searchTerm, MainActivity ip) {
        this.ip = ip;
        new AsyncIPSearch().execute(searchTerm);
    }

    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncIPSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return search(urls[0]);
        }

        protected void onPostExecute(String ipinfo) {
            ip.ipReady(ipinfo);
        }

        /*
         * Search my webservice for the searchTerm argument, and return a String that can be put in an textView
         */
        private String search(String searchTerm) {
            String ipURL = null;
            ipURL = "https://protected-scrubland-77046.herokuapp.com/result?searchTerm="+ searchTerm;

            System.out.println(ipURL);
            try {
                return getRemoteIP(ipURL);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error");
                return null; // so compiler does not complain
            }

        }


        /*
         * Given a URL referring to an ip details, return a String of that ip info
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private String getRemoteIP(final String url) {
            String ipAdr;
            String city;
            String region;
            String country;
            String loc;
            String postal;
            String timezone;
            try {
                HttpURLConnection conn ;
                URL myIP = new URL (url);
                conn = (HttpURLConnection)myIP.openConnection();
                conn.setRequestMethod("GET");


                InputStream stream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder responce = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)
                {
                    responce.append(line);
                    responce.append("\r");
                }
                reader.close();
                String result = responce.toString();
                System.out.print(result);
                JSONObject objectIp = new JSONObject(result);
                System.out.println(objectIp);

                if (objectIp.has("ip")) {
                    ipAdr = objectIp.getString("ip");
                } else {
                    ipAdr = "N/A";
                }

                if (objectIp.has("city")) {
                    city = objectIp.getString("city");
                } else {
                    city = "N/A";
                }

                if (objectIp.has("region")) {
                    region = objectIp.getString("region");
                } else {
                    region = "N/A";
                }

                if (objectIp.has("country")) {
                    country = objectIp.getString("country");
                } else {
                    country = "N/A";
                }

                if (objectIp.has("loc")) {
                    loc = objectIp.getString("loc");
                } else {
                    loc = "N/A";
                }

                if (objectIp.has("postal")) {
                    postal = objectIp.getString("postal");
                } else {
                    postal = "N/A";
                }


                if (objectIp.has("timezone")) {
                    timezone = objectIp.getString("timezone");
                } else {
                    timezone = "N/A";
                }


            } catch (IOException | JSONException  e) {
                e.printStackTrace();
                return "Please enter the valid IP";
            }
            System.out.println(ipAdr+ "@" + city +"@"+ region +"@"+country+ "@"+loc+ "@"+ postal +"@"+ timezone);
            return ipAdr+ "@" + city +"@"+ region +"@"+country+ "@"+loc+ "@" + postal +"@"+ timezone;
        }
    }
}