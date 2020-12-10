package api.movie;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

public class PopularGenres {
    public static String url = "https://imdb8.p.rapidapi.com/title/list-popular-genres";
    private HttpResponse<JsonNode> response;
    private JSONObject object;
    private JSONArray dataArray;
    private int count = 0;



    public PopularGenres() {
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray().getJSONObject(0).get("genres");
    }

    public String getNextGenre() {
        if(count < dataArray.length()){
            String genre = (String) dataArray.getJSONObject(count++).get("description");
            return genre;
        }
        return null;
    }
}
