package edu.cmu.myipinfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding detail of IP, it
         * can callback to this object with the resulting ip detail String.
         */
        final MainActivity ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                System.out.println("searchTerm = " + searchTerm);
                MyIP ipinfo = new MyIP();

                if(!searchTerm.equals("")) {
                    ipinfo.search(searchTerm, ma);
                }
                else {
                    TextView feedback = (TextView) findViewById(R.id.feedback);
                    feedback.setText("Please enter an IP");
                }
            }
        });
    }

    /*
     * This is called by the MyIP object when the ipDetail is ready.  This allows for passing back the String Ip detail for updating the TextView
     */
    public void ipReady(String ipinfo) {
        TextView feedback = (TextView) findViewById(R.id.feedback);

        try {
            String ip = ipinfo.split("@")[0];
            String city = ipinfo.split("@")[1];
            String region = ipinfo.split("@")[2];
            String country = ipinfo.split("@")[3];
            String loc = ipinfo.split("@")[4];
            String postal = ipinfo.split("@")[5];
            String timezone = ipinfo.split("@")[6];
            System.out.println(ipinfo);

            TextView searchView = (EditText)findViewById(R.id.searchTerm);
            TextView ipAddress =  (TextView)findViewById(R.id.ipAddress);
            TextView cityText =  (TextView)findViewById(R.id.city);
            TextView regionText =  (TextView)findViewById(R.id.region);
            TextView countryText =  (TextView)findViewById(R.id.country);
            TextView locText =  (TextView)findViewById(R.id.loc);
            TextView postalText =  (TextView)findViewById(R.id.postal);
            TextView timezoneText =  (TextView)findViewById(R.id.timezone);
            if (ipinfo != null) {
                System.out.println(ipinfo);
                ipAddress.setText(" You IP: " + ip);
                cityText.setText(" You city: " + city);
                regionText.setText(" You region: " +region);
                countryText.setText(" You country: " +country);
                locText.setText(" You loc: " +loc);
                postalText.setText(" You postal: " +postal);
                timezoneText.setText(" Your timezone: " + timezone);
                feedback.setText(" Found your IP information!\n");


            }
            searchView.setText("");
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            TextView searchView = (EditText)findViewById(R.id.searchTerm);
            TextView ipAddress =  (TextView)findViewById(R.id.ipAddress);
            TextView cityText =  (TextView)findViewById(R.id.city);
            TextView regionText =  (TextView)findViewById(R.id.region);
            TextView countryText =  (TextView)findViewById(R.id.country);
            TextView locText =  (TextView)findViewById(R.id.loc);
            TextView postalText =  (TextView)findViewById(R.id.postal);
            TextView timezoneText =  (TextView)findViewById(R.id.timezone);
            ipAddress.setText("");
            cityText.setText("");
            regionText.setText("");
            countryText.setText("");
            locText.setText("");
            postalText.setText("");
            timezoneText.setText("");
            feedback.setText("Sorry, I could not find a the info about "+ ((EditText)findViewById(R.id.searchTerm)).getText().toString());

        }




    }


}