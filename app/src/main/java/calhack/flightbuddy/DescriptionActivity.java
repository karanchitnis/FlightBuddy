package calhack.flightbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Karan Chitnis on 12/7/2014.
 */
public class DescriptionActivity extends Activity {
    private String status, flightNumber, airline, flightDate, departureTemperature, departureConditions,
            departureAirport, departureEstimateTime, departureTerminal, departureScheduleTime, departureGate, departureCity,
            arrivalTemperature, arrivalConditions, arrivalAirport, arrivalEstimateTime, arrivalTerminal,
            arrivalScheduleTime, arrivalGate, arrivalCity;

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
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

        if (b != null) { // When the user inputs something in the search and gets redirected here.
            if (b.containsKey("searchInfoJson")) {
                String searchData = b.getString("searchInfoJson");
                Toast.makeText(getApplicationContext(), "Your flight info has been saved!",
                        Toast.LENGTH_LONG).show();

                if (myPrefs.getString("flightData", "").equals("")) {
                    editor.putString("flightData", searchData);
                    editor.putString("flightDataIndex", "0");
                } else {
                    String appendedFlight = myPrefs.getString("flightData", "");
                    appendedFlight += "---" + searchData;
                    editor.putString("flightData", appendedFlight);
                    editor.putString("flightDataIndex", (appendedFlight.split("---").length) + "");
                }
                editor.commit();
                updateAttributes(searchData);
                //TODO: CAN ACCESS ANY OF THE ATTRIBUTES ON TOP FROM ABOVE OVER HERE
            }
        }
        else { // When the user clicks the Description button.
            String flightDataIndex = myPrefs.getString("flightDataIndex", "0");
            String flightData = myPrefs.getString("flightData", "");
            if (flightData.length() > 0) { // There is already a saved result (use attributes above)
                if (!flightData.contains("---")) {
                    updateAttributes(flightData);
                }
                else {
                    int index = Integer.parseInt(flightDataIndex);
                    String displayFlight = flightData.split("---")[index];
                    updateAttributes(displayFlight);
                    //TODO: CAN ACCESS ANY OF THE ATTRIBUTES ON TOP FROM ABOVE OVER HERE
                }
            }
            else { // The user does NOT have any saved flights (cannot use attributes above)
                TextView title = (TextView) findViewById(R.id.title);
                title.setText("NO FLIGHT DETAILS");
            }
        }
    }

    public void updateAttributes(String data) {
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            status = json.getString("status");
            flightNumber = json.getString("flightNumber");
            airline = json.getString("airline");
            flightDate = json.getString("flightDate");

            JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
            departureTemperature = departureInfo.getString("temperature");
            departureConditions = departureInfo.getString("conditions");
            departureAirport = departureInfo.getString("airport");
            departureEstimateTime = departureInfo.getString("estimateTime");
            departureTerminal = departureInfo.getString("terminal");
            departureScheduleTime = departureInfo.getString("scheduleTime");
            departureGate = departureInfo.getString("gate");
            departureCity = departureInfo.getString("city");

            JSONObject arrivalInfo = json.getJSONArray("arrivalInfo").getJSONObject(0);
            arrivalTemperature = arrivalInfo.getString("temperature");
            arrivalConditions = arrivalInfo.getString("conditions");
            arrivalAirport = arrivalInfo.getString("airport");
            arrivalEstimateTime = arrivalInfo.getString("estimateTime");
            arrivalTerminal = arrivalInfo.getString("terminal");
            arrivalScheduleTime = arrivalInfo.getString("scheduleTime");
            arrivalGate = arrivalInfo.getString("gate");
            arrivalCity = arrivalInfo.getString("city");

            TextView flightInfo = (TextView) findViewById(R.id.flightInfo);
            flightInfo.setText(airline + " " + flightNumber + " - " + flightDate + "\n" + departureCity + " to " + arrivalCity);
            TextView leftCol = (TextView) findViewById(R.id.departureInfo);
            leftCol.setText(departureAirport + "\nScheduled:\n" + departureScheduleTime + "\nEstimated:\n" + departureEstimateTime + "\nGate:\n" + departureGate + "\nTerminal:\n" + departureTerminal);
            TextView rightCol = (TextView) findViewById(R.id.arrivalInfo);
            rightCol.setText(arrivalAirport + "\nScheduled:\n" + arrivalScheduleTime + "\nEstimated:\n" + arrivalEstimateTime + "\nGate:\n" + arrivalGate + "\nTerminal:\n" + arrivalTerminal);

            /*System.out.println(status);
            System.out.println(departureTemperature);
            System.out.println(arrivalAirport);
            System.out.println(flightDate);*/
        } catch (JSONException e) {
            e.printStackTrace();
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