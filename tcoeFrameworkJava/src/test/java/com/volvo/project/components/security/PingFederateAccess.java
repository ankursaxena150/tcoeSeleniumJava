package com.volvo.project.components.security;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

public class PingFederateAccess {
    public String loginToAthos(String user, String password, String encodedClientSecret) throws UnirestException, JSONException {
//        String username = "cs-ws-s-tcoetester";
//        String encodedPassword = "78LM7K8ca%26%26K%25f-";  //hasło ma być w postaci

        //setSaveConn(); - tutaj może być potrzebna metoda do ominięcia certyfikatów

        HttpResponse<String> response = Unirest.post("https://federate-qa.volvo.com/as/token.oauth2")
                .header("cache-control", "no-cache")
                .header("content-type", "application/x-www-form-urlencoded")
                .header("Authorization", encodedClientSecret)
                .body("grant_type=password&validator_id=VolvoAuthNG&username="+user+"&password="+password)
                .asString();

        JSONObject jsonObject = new JSONObject(response.getBody());
        Object json = jsonObject.get("access_token");
        return json.toString();
    }
}
