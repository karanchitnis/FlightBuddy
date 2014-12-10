package calhack.flightbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import com.qualcomm.toq.smartwatch.api.v1.deckofcards.Constants;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.Card;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.ListCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.SimpleTextCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.DeckOfCardsManager;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCards;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCardsException;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteResourceStore;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.resource.CardImage;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Karan Chitnis on 12/7/2014.
 */
public class SavedActivity extends Activity {

    private DeckOfCardsManager mDeckOfCardsManager;
    private RemoteDeckOfCards mRemoteDeckOfCards;
    private CardImage[] mCardImages;
    private RemoteResourceStore mRemoteResourceStore;
    private boolean cardExist;
    private String status, departureConditions, departureTemperature, departureEstimateTime,
            departureGate, departureTerminal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        getWindow().setWindowAnimations(android.R.anim.slide_in_left);

        ImageButton searchButton = (ImageButton) findViewById(R.id.search);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        ImageButton descriptionButton = (ImageButton) findViewById(R.id.description);
        ImageButton savedButton = (ImageButton) findViewById(R.id.saved);

        savedButton.setBackgroundColor(Color.parseColor("#E8DDCB"));

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
        else {
            clearButton.setText("Add a flight!");
            clearButton.setOnClickListener(searchButtonListener);
        }

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
        simpleTextCard.setTitleText("Condition: " + departureConditions);
        simpleTextCard.setCardImage(mRemoteResourceStore, findImage(departureConditions));
        String[] messagesToShow = {"Temperature: " + departureTemperature};
        simpleTextCard.setMessageText(messagesToShow);
        simpleTextCard.setReceivingEvents(false);
        simpleTextCard.setShowDivider(true);
        listCard.add(simpleTextCard);

        simpleTextCard = new SimpleTextCard("statusCard");
        simpleTextCard.setHeaderText("Status");
<<<<<<< HEAD
        String[] messagesToShow2 = {"Estimated Departure Time: " + "12:00",
                "Departure Gate: " + "12",
                "Departure Terminal: " + "9 3/4"};
=======
        String[] messagesToShow2 = {"Estimated Departure Time: " + departureEstimateTime,
                "Departure Gate: " + departureGate,
                "Departure Terminal: " + departureTerminal};
>>>>>>> 025e6d334eb4749d35ff2499024ee3a374ca7548
        simpleTextCard.setMessageText(messagesToShow2);
        simpleTextCard.setReceivingEvents(false);
        simpleTextCard.setShowDivider(true);
        listCard.add(simpleTextCard);

        simpleTextCard = new SimpleTextCard("trafficCard");
        simpleTextCard.setHeaderText("Traffic");
        String[] trafficMessage = {"ETA: " + "26 minutes"};
        simpleTextCard.setMessageText(trafficMessage);
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

            int index = Integer.parseInt(myPrefs.getString("flightDataIndex", "0"));
            if (index == myPrefs.getString("flightData", "").split("---").length) {
                index -= 1;
            }
            getToqData(myPrefs.getString("flightData", "").split("---")[index]);

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

            Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
            startActivity(intent);
        }
    }

    public void getToqData(String data) {
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            status = json.getString("status");
            JSONObject departureInfo = json.getJSONArray("departureInfo").getJSONObject(0);
            departureTemperature = departureInfo.getString("temperature");
            departureConditions = departureInfo.getString("conditions");
            departureEstimateTime = departureInfo.getString("estimateTime");
            departureTerminal = departureInfo.getString("terminal");
            departureGate = departureInfo.getString("gate");
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