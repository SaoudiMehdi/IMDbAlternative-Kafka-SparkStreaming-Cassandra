package api.actor;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

public class MostPopularClebs {

    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    public static String url = "https://imdb8.p.rapidapi.com/actors/list-most-popular-celebs";

    private int count = 0;

    public MostPopularClebs() {
        response = ApiResponse.getResponseApiList(url);
        dataArray = response.getBody().getArray();
    }

    public String getNextActor() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.get(count++)).split("/")[2];;
            return id;
        }
        return null;
    }
}
