package calhack.flightbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Karan Chitnis on 12/7/2014.
 */
public class SavedActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        getWindow().setWindowAnimations(android.R.anim.slide_in_left);

<<<<<<< Updated upstream
        Button searchButton = (Button) findViewById(R.id.search);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        Button descriptionButton = (Button) findViewById(R.id.description);
=======
        ImageButton searchButton = (ImageButton) findViewById(R.id.search);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        ImageButton descriptionButton = (ImageButton) findViewById(R.id.description);
        ImageButton savedButton = (ImageButton) findViewById(R.id.saved);

        savedButton.setBackgroundColor(Color.parseColor("#E8DDCB"));
>>>>>>> Stashed changes

        searchButton.setOnClickListener(searchButtonListener);
        homeButton.setOnClickListener(homeButtonListener);
        descriptionButton.setOnClickListener(descriptionButtonListener);

        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(clearButtonListener);

        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String flightData = myPrefs.getString("flightData", "");
        System.out.println(flightData);
        if (flightData.length() > 0) {
            if (!flightData.contains("---")) {
                JSONObject json = null;
                try {
                    setBackgroundColorLayout(0);
                    json = new JSONObject(flightData);
                    String flightDate = json.getString("flightDate");

                    JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
                    String departureAirport = departureInfo.getString("airport");
                    String departureEstimateTime = departureInfo.getString("estimateTime");

                    JSONObject arrivalInfo = json.getJSONArray("arrivalInfo").getJSONObject(0);
                    String arrivalAirport = arrivalInfo.getString("airport");
                    String arrivalEstimateTime = arrivalInfo.getString("estimateTime");

                    TextView textFlight = ((TextView) findViewById(R.id.saved0));
                    TextView textFlightTimes = ((TextView) findViewById(R.id.saved0Times));
                    textFlight.setText(departureAirport + " to " + arrivalAirport);
                    textFlightTimes.setText(departureEstimateTime + " to " + arrivalEstimateTime + " on " + flightDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                String[] allSearches = flightData.split("---");
                JSONObject[] allJSONSearches = new JSONObject[allSearches.length];
                setBackgroundColorLayout(allSearches.length - 1);
                for (int i = 0; i < allSearches.length; i++) {
                    try {
                        allJSONSearches[i] = new JSONObject(allSearches[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < allJSONSearches.length; i++) {
                    try {
                        JSONObject json = allJSONSearches[i];
                        String flightDate = json.getString("flightDate");

                        JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
                        String departureAirport = departureInfo.getString("airport");
                        String departureEstimateTime = departureInfo.getString("estimateTime");

                        JSONObject arrivalInfo = json.getJSONArray("arrivalInfo").getJSONObject(0);
                        String arrivalAirport = arrivalInfo.getString("airport");
                        String arrivalEstimateTime = arrivalInfo.getString("estimateTime");

                        TextView textFlight = ((TextView) findViewById(R.id.saved0));
                        TextView textFlightTimes = ((TextView) findViewById(R.id.saved0Times));

                        if (i == 0) {
                            textFlight = ((TextView) findViewById(R.id.saved0));
                            textFlightTimes = ((TextView) findViewById(R.id.saved0Times));
                        }
                        else if (i == 1) {
                            textFlight = ((TextView) findViewById(R.id.saved1));
                            textFlightTimes = ((TextView) findViewById(R.id.saved1Times));
                        }
                        else if (i == 2) {
                            textFlight = ((TextView) findViewById(R.id.saved2));
                            textFlightTimes = ((TextView) findViewById(R.id.saved2Times));
                        }
                        else if (i == 3) {
                            textFlight = ((TextView) findViewById(R.id.saved3));
                            textFlightTimes = ((TextView) findViewById(R.id.saved3Times));
                        }
                        else if (i == 4) {
                            textFlight = ((TextView) findViewById(R.id.saved4));
                            textFlightTimes = ((TextView) findViewById(R.id.saved4Times));
                        }
                        else {
                            textFlight = ((TextView) findViewById(R.id.saved5));
                            textFlightTimes = ((TextView) findViewById(R.id.saved5Times));
                        }

                        textFlight.setText(departureAirport + " to " + arrivalAirport);
                        textFlightTimes.setText(departureEstimateTime + " to " + arrivalEstimateTime + " on " + flightDate);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setBackgroundColorLayout(int index) {
        if (index >= 0) {
            LinearLayout layout0 = (LinearLayout) findViewById(R.id.savedLayout0);
            layout0.setBackgroundColor(Color.WHITE);
        }
        if (index >= 1) {
            LinearLayout layout1 = (LinearLayout) findViewById(R.id.savedLayout1);
            layout1.setBackgroundColor(Color.WHITE);
        }
        if (index >= 2) {
            LinearLayout layout2 = (LinearLayout) findViewById(R.id.savedLayout2);
            layout2.setBackgroundColor(Color.WHITE);
        }
        if (index >= 3) {
            LinearLayout layout3 = (LinearLayout) findViewById(R.id.savedLayout3);
            layout3.setBackgroundColor(Color.WHITE);
        }
        if (index >= 4) {
            LinearLayout layout4 = (LinearLayout) findViewById(R.id.savedLayout4);
            layout4.setBackgroundColor(Color.WHITE);
        }
        if (index >= 5) {
            LinearLayout layout5 = (LinearLayout) findViewById(R.id.savedLayout5);
            layout5.setBackgroundColor(Color.WHITE);
        }
    }

    public void onClick(View v) {
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (!myPrefs.getString("flightData", "").equals("")) {
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.remove("flightDataIndex");
            if (v.getId() == R.id.savedLayout0) {
                editor.putString("flightDataIndex", "0");
            } else if (v.getId() == R.id.savedLayout1) {
                editor.putString("flightDataIndex", "1");
            } else if (v.getId() == R.id.savedLayout2) {
                editor.putString("flightDataIndex", "2");
            } else if (v.getId() == R.id.savedLayout3) {
                editor.putString("flightDataIndex", "3");
            } else if (v.getId() == R.id.savedLayout4) {
                editor.putString("flightDataIndex", "4");
            } else {
                editor.putString("flightDataIndex", "5");
            }
            editor.commit();

            Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
            startActivity(intent);
        }
    }

    View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SearchActivity.class);
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
    View.OnClickListener descriptionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener clearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(SavedActivity.this)
                    .setTitle("Clear Saved Flights")
                    .setMessage("Are you sure you want to clear all saved flights?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.remove("flightData");
                            editor.remove("flightDataIndex");
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    };
}