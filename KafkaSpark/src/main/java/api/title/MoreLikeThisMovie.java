package api.title;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;

public class MoreLikeThisMovie {
    String url = "https://imdb8.p.rapidapi.com/title/get-more-like-this?tconst=";
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;

    private int count = 0;

    public MoreLikeThisMovie(String id_movie) {
        url += id_movie;
        response = ApiResponse.getResponseApiList(url);
        dataArray = response.getBody().getArray();
    }

    public String getNextMovie() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.get(count++)).split("/")[2];;
            return id;
        }
        return null;
    }

}
