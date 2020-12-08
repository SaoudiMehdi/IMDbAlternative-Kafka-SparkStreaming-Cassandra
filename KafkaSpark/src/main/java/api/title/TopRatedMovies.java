package api.title;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

public class TopRatedMovies {
    String url = "https://imdb8.p.rapidapi.com/title/get-top-rated-movies";
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;

    private int count = 0;

    public TopRatedMovies() {
        response = ApiResponse.getResponseApiList(url);
        dataArray = response.getBody().getArray();
    }

    public String getNextMovie() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.getJSONObject(count++).get("id")).split("/")[2];;
            return id;
        }
        return null;
    }
}
