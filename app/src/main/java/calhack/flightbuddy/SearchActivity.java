package calhack.flightbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Karan Chitnis on 12/5/2014.
 */
public class SearchActivity extends Activity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dateView = (EditText) findViewById(R.id.dateInput);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        Button addFlightButton = (Button) findViewById(R.id.addFlight);
        addFlightButton.setOnClickListener(addFlightButtonListener);
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog (int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    View.OnClickListener addFlightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        String flightNumber = ((EditText) findViewById(R.id.flightNumInput)).getText().toString();
                        String airline = ((EditText) findViewById(R.id.airlineInput)).getText().toString();
                        String google = "http://www.google.com/search?q=";
                        String search = "flightstats " + airline + " flight " + flightNumber;
                        System.out.println(search);
                        String charset = "UTF-8";
                        String userAgent = "FlightBuddy";
                        String flightStatsBaseUrl = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=";

                        Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("li.g>h3>a");
                        String url = "";
                        boolean flightStatsUsed = false;

                        for (Element link : links) {
                            url = link.absUrl("href");
                            url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
                            if (url.toString().length() >= 75 && url.toString().substring(0, 75).equals(flightStatsBaseUrl)) {
                                flightStatsUsed = true;
                                break;
                            }
                        }
                        if (airline.toLowerCase().equals("southwest") || airline.toLowerCase().equals("southwest airline") || airline.toLowerCase().equals("southwest airlines")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(SWA)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (!flightStatsUsed) {
                            System.out.println("Flight cannot be found");
                            return;
                        }
                        else {
                            Document doc = Jsoup.connect(url).userAgent(userAgent).get();
                            String status = doc.select("div.statusType").text();
                            if (status.equals("En-Route - On-time")) {
                                status = "On-time";
                            }
                            else if (status.equals("En-Route - Delayed")) {
                                status = "Delayed";
                            }
                            else if (status.equals("Unknown Flight")) {
                                System.out.println("Flight cannot be found");
                                return;
                            }

                            String routeInfo = doc.select("div.route").text();
                            String departureAirCity = routeInfo.split(" to ")[0].split(",")[0];
                            String arrivalAirCity = routeInfo.split(" to ")[1].split(",")[0];
                            String departureAirport = departureAirCity.substring(0,5);
                            String departureCity = departureAirCity.substring(6);
                            String arrivalAirport = arrivalAirCity.substring(0,5);
                            String arrivalCity = arrivalAirCity.substring(6);

                            Elements buttons = doc.select(".secondaryNavTab.offSecondaryNavTab");
                            Element button = buttons.first();
                            String departureUrl = button.attr("abs:href");
                            String arrivalUrl = button.nextElementSibling().attr("abs:href");

                            Element departure = Jsoup.connect(departureUrl).userAgent(userAgent).get();
                            String departureGateandTerm = departure.select("div.statusBlockInnerDiv.halfWidthInnerDiv").text().trim();
                            String departureGate = "N/A";
                            String departureTerminal = "N/A";
                            if (departureGateandTerm.contains("Gate:") && departureGateandTerm.contains("Terminal")) {
                                departureGate = departureGateandTerm.split("Gate:")[1].split("\\s+")[1];
                                departureTerminal = departureGateandTerm.split("Terminal")[1].split("\\)")[0];
                            }
                            else if (departureGateandTerm.contains("Gate:") && !departureGateandTerm.contains("Terminal")) {
                                departureGate = departureGateandTerm.split("Gate:")[1].split("\\s+")[1];
                                departureTerminal = "N/A";
                            }
                            else if (!departureGateandTerm.contains("Gate:") && departureGateandTerm.contains("Terminal")) {
                                departureGate = "N/A";
                                departureTerminal = departureGateandTerm.split("Terminal")[1].split("\\)")[0];
                            }

                            String departureScheduleTime = departure.select("tr>td.statusValue").get(2).text().split(" - ")[0];
                            String departureEstimateTime = departure.select("tr>td.statusValue").get(4).text().split(" - ")[0];

                            Element arrival = Jsoup.connect(arrivalUrl).userAgent(userAgent).get();
                            String arrivalGateandTerm = arrival.select("div.statusBlockInnerDiv.halfWidthInnerDiv").text().trim();
                            String arrivalGate = "N/A";
                            String arrivalTerminal = "N/A";
                            if (arrivalGateandTerm.contains("Gate:") && arrivalGateandTerm.contains("Terminal")) {
                                arrivalGate = arrivalGateandTerm.split("Gate:")[1].split("\\s+")[1];
                                arrivalTerminal = arrivalGateandTerm.split("Terminal")[1].split("\\)")[0];
                            }
                            else if (arrivalGateandTerm.contains("Gate:") && !arrivalGateandTerm.contains("Terminal")) {
                                arrivalGate = arrivalGateandTerm.split("Gate:")[1].split("\\s+")[1];
                                arrivalTerminal = "N/A";
                            }
                            else if (!arrivalGateandTerm.contains("Gate:") && arrivalGateandTerm.contains("Terminal")) {
                                arrivalGate = "N/A";
                                arrivalTerminal = arrivalGateandTerm.split("Terminal")[1].split("\\)")[0];
                            }

                            String arrivalScheduleTime = arrival.select("tr>td.statusValue").get(2).text().split(" - ")[0];
                            String arrivalEstimateTime = arrival.select("tr>td.statusValue").get(4).text().split(" - ")[0];

                            url = "http://www.flightstats.com/go/Airport/weather.do?airportCode=" + departureAirport.substring(1,departureAirport.length()-1);
                            Element departureWeatherElem = Jsoup.connect(url).userAgent(userAgent).get();
                            String departureWeather = departureWeatherElem.select(".uiComponentBody").get(2).text();
                            String departureConditions = departureWeather.split("Conditions:")[1].split("Temperature:")[0].trim();
                            String departureTemperature = departureWeather.split("Temperature:")[1].split("Dewpoint:")[0].trim();

                            url = "http://www.flightstats.com/go/Airport/weather.do?airportCode=" + arrivalAirport.substring(1,arrivalAirport.length()-1);
                            Element arrivalWeatherElem = Jsoup.connect(url).userAgent(userAgent).get();
                            String arrivalWeather = arrivalWeatherElem.select(".uiComponentBody").get(2).text();
                            String arrivalConditions = arrivalWeather.split("Conditions:")[1].split("Temperature:")[0].trim();
                            String arrivalTemperature = arrivalWeather.split("Temperature:")[1].split("Dewpoint:")[0].trim();

                            JSONObject searchInfo = new JSONObject();
                            searchInfo.put("status", status);

                            JSONArray departureArray = new JSONArray();
                            JSONObject departureInfo = new JSONObject();
                            departureInfo.put("airport", departureAirport);
                            departureInfo.put("city", departureCity);
                            departureInfo.put("gate", departureGate);
                            departureInfo.put("terminal", departureTerminal);
                            departureInfo.put("scheduleTime", departureScheduleTime);
                            departureInfo.put("estimateTime", departureEstimateTime);
                            departureInfo.put("conditions", departureConditions);
                            departureInfo.put("temperature", departureTemperature);
                            departureArray.put(departureInfo);

                            JSONArray arrivalArray = new JSONArray();
                            JSONObject arrivalInfo = new JSONObject();
                            arrivalInfo.put("airport", arrivalAirport);
                            arrivalInfo.put("city", arrivalCity);
                            arrivalInfo.put("gate", arrivalGate);
                            arrivalInfo.put("terminal", arrivalTerminal);
                            arrivalInfo.put("scheduleTime", arrivalScheduleTime);
                            arrivalInfo.put("estimateTime", arrivalEstimateTime);
                            arrivalInfo.put("conditions", arrivalConditions);
                            arrivalInfo.put("temperature", arrivalTemperature);
                            arrivalArray.put(arrivalInfo);

                            searchInfo.put("departureInfo", departureArray);
                            searchInfo.put("arrivalInfo", arrivalArray);
                            System.out.println(searchInfo);

                            /*
                            System.out.println(departureAirport);
                            System.out.println(departureCity);
                            System.out.println(arrivalAirport);
                            System.out.println(arrivalCity);
                            System.out.println(status);
                            System.out.println(departureGate);
                            System.out.println(departureTerminal);
                            System.out.println(departureScheduleTime);
                            System.out.println(departureEstimateTime);
                            System.out.println(arrivalGate);
                            System.out.println(arrivalTerminal);
                            System.out.println(arrivalScheduleTime);
                            System.out.println(arrivalEstimateTime);
                            System.out.println(departureConditions);
                            System.out.println(departureTemperature);
                            System.out.println(arrivalConditions);
                            System.out.println(arrivalTemperature);
                            */
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    };
}
