package api.title;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

public class RatingMovie {
    String url = "https://imdb8.p.rapidapi.com/title/get-ratings?tconst=";
    private HttpResponse<JsonNode> response;
    private JSONObject object;

    private int count = 0;

    public RatingMovie(String id_movie) {
        url += id_movie;
        response = ApiResponse.getResponseApiList(url);
        object = response.getBody().getObject();;
        System.out.println(object);
    }

    public double getRating() {
        return object.getDouble("rating");
    }

}
