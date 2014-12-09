package calhack.flightbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends Activity {

    private String flightDate, departureTemperature, departureConditions,
        departureAirport, departureEstimateTime, arrivalAirport, arrivalEstimateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setWindowAnimations(android.R.anim.slide_in_left);

        Button searchButton = (Button) findViewById(R.id.search);
        Button homeToSearch = (Button) findViewById(R.id.homeToSearch);
        Button savedButton = (Button) findViewById(R.id.saved);
        Button descriptionButton = (Button) findViewById(R.id.description);

        searchButton.setOnClickListener(searchButtonListener);
        homeToSearch.setOnClickListener(searchButtonListener);
        savedButton.setOnClickListener(savedButtonListener);
        descriptionButton.setOnClickListener(descriptionButtonListener);

        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

        TextView upcommingFlight = (TextView) findViewById(R.id.upcommingFlight);
        if (myPrefs.getString("flightData", "").equals("")) {
            upcommingFlight.setText("No Flights Saved!");
        }
        else {
            String flightDataIndex = myPrefs.getString("flightDataIndex", "0");
            int index = Integer.parseInt(flightDataIndex);
            if (index == myPrefs.getString("flightData",  "").split("---").length) {
                index -= 1;
            }
            String flightInfo = myPrefs.getString("flightData",  "").split("---")[index];
            updateAttributes(flightInfo);
            upcommingFlight.setText(upcommingFlight.getText() + " " + flightDate);
            ImageView condition = (ImageView) findViewById(R.id.condition);
            if (departureConditions.toLowerCase().contains("sun")) {
                condition.setImageResource(R.drawable.sunny);
            }
            else if (departureConditions.toLowerCase().contains("cloud")) {
                condition.setImageResource(R.drawable.cloudy);
            }
            else if (departureConditions.toLowerCase().contains("storm")) {
                condition.setImageResource(R.drawable.stormy);
            }
            else if (departureConditions.toLowerCase().contains("snow")) {
                condition.setImageResource(R.drawable.snowy);
            }
            else if (departureConditions.toLowerCase().contains("rain")) {
                condition.setImageResource(R.drawable.rainy);
            }
            else if (departureConditions.toLowerCase().contains("fog")) {
                condition.setImageResource(R.drawable.foggy);
            }
            else {
                condition.setImageResource(R.drawable.cloudy);
            }
            TextView temperature = (TextView) findViewById(R.id.temperature);
            temperature.setText(departureTemperature);
            TextView depAirport = (TextView) findViewById(R.id.depAirport);
            depAirport.setText(departureAirport);
            TextView arrAirport = (TextView) findViewById(R.id.arrAirport);
            arrAirport.setText(arrivalAirport);
            TextView depTimes = (TextView) findViewById(R.id.depTimes);
            depTimes.setText(departureEstimateTime);
            TextView arrTimes = (TextView) findViewById(R.id.arrTimes);
            arrTimes.setText(arrivalEstimateTime);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAttributes(String data) {
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            flightDate = json.getString("flightDate");

            JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
            departureTemperature = departureInfo.getString("temperature");
            departureConditions = departureInfo.getString("conditions");
            departureAirport = departureInfo.getString("airport");
            departureEstimateTime = departureInfo.getString("estimateTime");

            JSONObject arrivalInfo = json.getJSONArray("arrivalInfo").getJSONObject(0);
            arrivalAirport = arrivalInfo.getString("airport");
            arrivalEstimateTime = arrivalInfo.getString("estimateTime");

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
    View.OnClickListener descriptionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
            startActivity(intent);
        }
    };
}

