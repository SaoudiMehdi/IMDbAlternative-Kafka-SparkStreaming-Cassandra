package api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ApiResponse {

    public static String API_KEY = "2787398dd7mshbb323b746979b1fp12f4a0jsn9b2ee9e88420";
    public static String api = "imdb8.p.rapidapi.com";

    public synchronized  static HttpResponse<JsonNode> getResponseApi(String url){
        if (!API_KEY.isEmpty()) {
            HttpResponse<JsonNode> response;
            try {
                response = Unirest.get(url)
                        .header("x-rapidapi-host", api)
                        .header("x-rapidapi-key", API_KEY)
                        .asJson();

                if (response.getBody().getArray().getJSONObject(0).has("message")) {
                    //api_switch.SwitchAPI();
                    response = Unirest.get(url)
                            .header("x-rapidapi-host", api)
                            .header("x-rapidapi-key", API_KEY)
                            .asJson();

                } else {
                    return response;
                }
            } catch (UnirestException e) {

            }
        }
        return null;
    }
    public synchronized  static HttpResponse<JsonNode> getResponseApiList(String url){
        if (!API_KEY.isEmpty()) {
            HttpResponse<JsonNode> response;
            try {
                response = Unirest.get(url)
                        .header("x-rapidapi-host", api)
                        .header("x-rapidapi-key", API_KEY)
                        .asJson();
                return response;
            } catch (UnirestException e) {

            }
        }
        return null;
    }

}
