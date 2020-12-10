package api.movie;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;

public class ComingSoonTvShows {
    String url = "https://imdb8.p.rapidapi.com/title/get-coming-soon-tv-shows?currentCountry=US";
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;

    private int count = 0;

    public ComingSoonTvShows() {
        response = ApiResponse.getResponseApiList(url);
        dataArray = response.getBody().getArray();
    }

    public String getNextTvShow() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.get(count++)).split("/")[2];;
            return id;
        }
        return null;
    }


}
