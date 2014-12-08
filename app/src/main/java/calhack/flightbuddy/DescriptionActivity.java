package calhack.flightbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Karan Chitnis on 12/7/2014.
 */
public class DescriptionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Button searchButton = (Button) findViewById(R.id.search);
        Button savedButton = (Button) findViewById(R.id.saved);
        Button homeButton = (Button) findViewById(R.id.home);

        searchButton.setOnClickListener(searchButtonListener);
        savedButton.setOnClickListener(savedButtonListener);
        homeButton.setOnClickListener(homeButtonListener);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("searchInfoJson")) {
            String searchData = b.getString("searchInfoJson");
            Toast.makeText(getApplicationContext(), "Your flight info has been saved!",
                    Toast.LENGTH_LONG).show();

            SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            String str = myPrefs.getString("flightData", "");
            System.out.println(str);

            if (myPrefs.getString("flightData", "").equals("")) {
                editor.putString("flightData", searchData);
            } else {
                String appendedFlight = myPrefs.getString("flightData", "");
                appendedFlight += "---" + searchData;
                editor.putString("flightData", appendedFlight);
                //editor.remove("flightData");
            }
            editor.commit();

            str = myPrefs.getString("flightData", "");
            System.out.println(str);

            JSONObject json = null;
            try {
                json = new JSONObject(searchData);
                String status = json.getString("status");
                String flightNumber = json.getString("flightNumber");
                String airline = json.getString("airline");
                String flightDate = json.getString("flightDate");

                JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
                String departureTemperature = departureInfo.getString("temperature");
                String departureConditions = departureInfo.getString("conditions");
                String departureAirport = departureInfo.getString("airport");
                String departureEstimateTime = departureInfo.getString("estimateTime");
                String departureTerminal = departureInfo.getString("terminal");
                String departureScheduleTime = departureInfo.getString("scheduleTime");
                String departureGate = departureInfo.getString("gate");
                String departureCity = departureInfo.getString("city");

                JSONObject arrivalInfo = json.getJSONArray("arrivalInfo").getJSONObject(0);
                String arrivalTemperature = arrivalInfo.getString("temperature");
                String arrivalConditions = arrivalInfo.getString("conditions");
                String arrivalAirport = arrivalInfo.getString("airport");
                String arrivalEstimateTime = arrivalInfo.getString("estimateTime");
                String arrivalTerminal = arrivalInfo.getString("terminal");
                String arrivalScheduleTime = arrivalInfo.getString("scheduleTime");
                String arrivalGate = arrivalInfo.getString("gate");
                String arrivalCity = arrivalInfo.getString("city");

                System.out.println(status);
                System.out.println(departureTemperature);
                System.out.println(arrivalAirport);
                System.out.println(flightDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SearchActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener savedButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SavedActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener homeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HomeActivity.class);
            startActivity(intent);
        }
    };
}
