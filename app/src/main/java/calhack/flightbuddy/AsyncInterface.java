package calhack.flightbuddy;

import org.json.JSONObject;

/**
 * Created by Karan Chitnis on 12/7/2014.
 */
public interface AsyncInterface {
    public JSONObject processFinish(String flightNumber, String airline);
}
