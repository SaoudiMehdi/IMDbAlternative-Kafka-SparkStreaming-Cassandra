package api.movie;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import util.Movie;

public class MoreLikeThisMovie {
    public String url = "https://imdb8.p.rapidapi.com/title/get-more-like-this?tconst=";
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;

    private int count = 0;

    public MoreLikeThisMovie(String id_movie) {
        url += id_movie;
        response = ApiResponse.getResponseApi(url);
        dataArray = response.getBody().getArray();
    }

    public Movie getNextMovie() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.get(count++)).split("/")[2];;
            Movie movie = new GetMetaData(id).getMovie();
            return movie;
        }
        return null;
    }

}
