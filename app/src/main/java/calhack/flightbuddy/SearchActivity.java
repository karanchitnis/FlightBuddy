package calhack.flightbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Iterator;

import com.qualcomm.toq.smartwatch.api.v1.deckofcards.Constants;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.Card;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.ListCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.SimpleTextCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.DeckOfCardsManager;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCards;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCardsException;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteResourceStore;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.resource.CardImage;

/**
 * Created by Karan Chitnis on 12/5/2014.
 */
public class SearchActivity extends Activity {

    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;

    private DeckOfCardsManager mDeckOfCardsManager;
    private RemoteDeckOfCards mRemoteDeckOfCards;
    private CardImage[] mCardImages;
    private RemoteResourceStore mRemoteResourceStore;
    private boolean cardExist;
    private Button saveToqB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setWindowAnimations(android.R.anim.slide_in_left);

        dateView = (EditText) findViewById(R.id.dateInput);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("notFound")) {
            String errorMessage = b.getString("notFound");
            Toast.makeText(getApplicationContext(), errorMessage,
                    Toast.LENGTH_LONG).show();
        }

        ImageButton savedButton = (ImageButton) findViewById(R.id.saved);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        ImageButton descriptionButton = (ImageButton) findViewById(R.id.description);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search);

        searchButton.setBackgroundColor(Color.parseColor("#E8DDCB"));

        savedButton.setOnClickListener(savedButtonListener);
        homeButton.setOnClickListener(homeButtonListener);
        descriptionButton.setOnClickListener(descriptionButtonListener);

        Button addFlightButton = (Button) findViewById(R.id.addFlight);
        addFlightButton.setOnClickListener(addFlightButtonListener);


        Button toqB = (Button) findViewById(R.id.toqIns);
        toqB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                install();
            }
        });
        mDeckOfCardsManager = DeckOfCardsManager.getInstance(getApplication());
        toqIni();
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
                        String charset = "UTF-8";
                        String userAgent = "FlightBuddy";
                        String flightStatsBaseUrl = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=";
                        String errorMessage = "Flight cannot be found";

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
                        if (airline.toLowerCase().contains("southwest")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(SWA)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (airline.toLowerCase().contains("united airlines")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(UA)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (airline.toLowerCase().contains("new zealand")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(NZ)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (airline.toLowerCase().contains("delta air")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(DL)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (airline.toLowerCase().contains("american")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(AA)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (airline.toLowerCase().contains("blue")) {
                            url = "http://www.flightstats.com/go/FlightStatus/flightStatusByFlight.do?airline=(B6)&flightNumber=" + flightNumber;
                            flightStatsUsed = true;
                        }
                        if (!flightStatsUsed) {
                            System.out.println(errorMessage);
                            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                            intent.putExtra("notFound", errorMessage);
                            startActivity(intent);
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
                                System.out.println(errorMessage);
                                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                                intent.putExtra("notFound", errorMessage);
                                startActivity(intent);
                                return;
                            }

                            String routeInfo = doc.select("div.route").text();
                            String departureAirCity = routeInfo.split(" to ")[0].split(",")[0];
                            if (!routeInfo.contains(" to ")) {
                                System.out.println(errorMessage);
                                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                                intent.putExtra("notFound", errorMessage);
                                startActivity(intent);
                                return;
                            }
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
                            String flightDate = ((EditText) findViewById(R.id.dateInput)).getText().toString();

                            JSONObject searchInfo = new JSONObject();
                            searchInfo.put("status", status);
                            searchInfo.put("flightDate", flightDate);
                            searchInfo.put("flightNumber", flightNumber);
                            searchInfo.put("airline", airline);

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

//                            uninstall();

//                            install();
                            if (cardExist) {
                                updateCard();
                            } else if (mRemoteDeckOfCards != null) {
                                makeCards();
                            }

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                            intent.putExtra("searchInfoJson", searchInfo.toString());
                            startActivity(intent);
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
            String flightNumber = ((EditText) findViewById(R.id.flightNumInput)).getText().toString();
            String airline = ((EditText) findViewById(R.id.airlineInput)).getText().toString();
            if (flightNumber.length() == 0) {
                Toast.makeText(getApplicationContext(), "Must enter a valid flight number",
                        Toast.LENGTH_SHORT).show();
            }
            else if (airline.length() == 0) {
                Toast.makeText(getApplicationContext(), "Must enter a valid airline",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                thread.start();
            }
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
    View.OnClickListener descriptionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
            startActivity(intent);
        }
    };


    private void toqIni() {
        mRemoteResourceStore = new RemoteResourceStore();
        // Try to retrieve a stored deck of cards
        try {
            mRemoteDeckOfCards = createDeckOfCards();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void install() {
        boolean isInstalled = false;

        try {
            isInstalled = mDeckOfCardsManager.isInstalled();
        } catch (RemoteDeckOfCardsException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: Can't determine if app is installed", Toast.LENGTH_SHORT).show();
        }

        if (!isInstalled) {
            try {
                mDeckOfCardsManager.installDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);

//                makeCards();
            } catch (RemoteDeckOfCardsException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: Cannot install application", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "App is already installed!", Toast.LENGTH_SHORT).show();
        }


        if (mRemoteDeckOfCards == null) {
            mRemoteDeckOfCards = createDeckOfCards();
//            makeCards();
        }

    }

    private CardImage findImage(String conditionIn ) {
        if (conditionIn.toLowerCase().contains("sun")) {
            return mCardImages[5];
        }
        else if (conditionIn.toLowerCase().contains("cloud")) {
            return mCardImages[0];
        }
        else if (conditionIn.toLowerCase().contains("storm")) {
            return mCardImages[4];
        }
        else if (conditionIn.toLowerCase().contains("snow")) {
            return mCardImages[3];
        }
        else if (conditionIn.toLowerCase().contains("rain")) {
            return mCardImages[2];
        }
        else if (conditionIn.toLowerCase().contains("fog")) {
            return mCardImages[1];
        }
        else {
            return mCardImages[5];
        }
    }


    // Read an image from assets and return as a bitmap
    private Bitmap getBitmap(String fileName) throws Exception {

        try {
            InputStream is = getAssets().open(fileName);
            Bitmap b = BitmapFactory.decodeStream(is);
            return Bitmap.createScaledBitmap(b, 250, 288, false);
        } catch (Exception e) {
            throw new Exception("An error occurred getting the bitmap: " + fileName, e);
        }
    }

    private RemoteDeckOfCards createDeckOfCards(){

        ListCard listCard= new ListCard();
        return new RemoteDeckOfCards(this, listCard);
    }

    // Need Input HERERERERERERERERERERERERERER KARAAAAAAAAAAAAAAAAAAAAAN
    private void makeCards() {


        mCardImages = new CardImage[6];
        try {
            mCardImages[0] = new CardImage("cloudy", getBitmap("cloudy.png"));
            mCardImages[1] = new CardImage("foggy", getBitmap("foggy.png"));
            mCardImages[2] = new CardImage("rainy", getBitmap("rainy.png"));
            mCardImages[3] = new CardImage("snowy", getBitmap("snowy.png"));
            mCardImages[4] = new CardImage("stormy", getBitmap("stormy.png"));
            mCardImages[5] = new CardImage("sunny", getBitmap("sunny.png"));

            mRemoteResourceStore.addResource(mCardImages[0]);
            mRemoteResourceStore.addResource(mCardImages[1]);
            mRemoteResourceStore.addResource(mCardImages[2]);
            mRemoteResourceStore.addResource(mCardImages[3]);
            mRemoteResourceStore.addResource(mCardImages[4]);
            mRemoteResourceStore.addResource(mCardImages[5]);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't get picture icon");
            return;
        }

        try {
            mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
        } catch (RemoteDeckOfCardsException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to add resources", Toast.LENGTH_SHORT).show();
        }


        ListCard listCard = mRemoteDeckOfCards.getListCard();

        SimpleTextCard simpleTextCard = new SimpleTextCard("WeatherCard");
        simpleTextCard.setHeaderText("Weather");
        simpleTextCard.setTitleText("Condition: " + "STOORRMMMYY");
        simpleTextCard.setCardImage(mRemoteResourceStore, findImage("fog"));
        String[] messagesToShow = {"Temperature: " + "100000 F"};
        simpleTextCard.setMessageText(messagesToShow);
        simpleTextCard.setReceivingEvents(false);
        simpleTextCard.setShowDivider(true);
        listCard.add(simpleTextCard);

        simpleTextCard = new SimpleTextCard("statusCard");
        simpleTextCard.setHeaderText("Status");
        String[] messagesToShow2 = {"Estimated Departure Time: " + "12:00",
                                    "Departure Gate: " + "15",
                                    "Departure Terminal: " + "9 3/4"};
        simpleTextCard.setMessageText(messagesToShow2);
        simpleTextCard.setReceivingEvents(false);
        simpleTextCard.setShowDivider(true);
        listCard.add(simpleTextCard);

        simpleTextCard = new SimpleTextCard("trafficCard");
        simpleTextCard.setHeaderText("Traffic");
        simpleTextCard.setReceivingEvents(false);
        simpleTextCard.setShowDivider(true);
        listCard.add(simpleTextCard);

        cardExist = true;

        try {
            mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
        } catch (RemoteDeckOfCardsException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to Create SimpleTextCard", Toast.LENGTH_SHORT).show();
        }

    }



    private void updateCard() {
        if (cardExist) {


            int count = 0;
            for (Iterator<Card> it = mRemoteDeckOfCards.getListCard().iterator(); it.hasNext(); count ++) {
                if (count == 0) {
                    //weather!
                    ((SimpleTextCard) it.next()).setTitleText("Condition: " + "cloudy");
                    ((SimpleTextCard) it.next()).setCardImage(mRemoteResourceStore, findImage("cloudy"));
                    String[] messagesToShow = {"Temperature: " + "78F"};
                    ((SimpleTextCard) it.next()).setMessageText(messagesToShow);
                    try {
                        mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
                    } catch (RemoteDeckOfCardsException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to Update SimpleTextCard", Toast.LENGTH_SHORT).show();
                    }
                } else if (count == 1) {
                    // status
                    String[] messagesToShow2 = {"Estimated Departure Time: " + "22:00",
                            "Departure Gate: " + "61c",
                            "Departure Terminal: " + "8 8/9"};
                    ((SimpleTextCard) it.next()).setMessageText(messagesToShow2);
                    try {
                        mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
                    } catch (RemoteDeckOfCardsException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to Update SimpleTextCard", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            try {
                mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
            } catch (RemoteDeckOfCardsException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to Create SimpleTextCard", Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected void onStart(){
        super.onStart();

        // If not connected, try to connect
        if (!mDeckOfCardsManager.isConnected()){
            try{
                mDeckOfCardsManager.connect();
            }
            catch (RemoteDeckOfCardsException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to connect the Toq", Toast.LENGTH_SHORT).show();
            }
        }
//        install();
//        makeCards();

    }

    private void uninstall() {
        boolean isInstalled = true;
        cardExist = false;

        try {
            isInstalled = mDeckOfCardsManager.isInstalled();
        } catch (RemoteDeckOfCardsException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: Can't determine if app is installed", Toast.LENGTH_SHORT).show();
        }

        if (isInstalled) {
            try {
                removeDeckOfCards();
                mDeckOfCardsManager.uninstallDeckOfCards();
            } catch (RemoteDeckOfCardsException e) {
                Toast.makeText(this,"Error uninstalling deck of cards", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Already uninstalled", Toast.LENGTH_SHORT).show();
        }
    }
    private void removeDeckOfCards() {
        ListCard listCard = mRemoteDeckOfCards.getListCard();
        while (listCard.size() != 0) {
            listCard.remove(0);
            try {
                mDeckOfCardsManager.updateDeckOfCards(mRemoteDeckOfCards, mRemoteResourceStore);
            } catch (RemoteDeckOfCardsException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to delete Card from ListCard", Toast.LENGTH_SHORT).show();
            }
        }
    }

}