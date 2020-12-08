package api.actor;

import api.ApiResponse;
import api.MovieProvider;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

import java.util.ArrayList;

public class AllFilmography {


    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    public static String url = "https://imdb8.p.rapidapi.com/actors/get-all-filmography?nconst=";

    private int count = 0;

    public AllFilmography(String id_actor) {
        url += id_actor;
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray().getJSONObject(0).get("filmography");
    }

    public Movie getNextMovie() {
        if(count < dataArray.length()){
            JSONObject MovieDescription = dataArray.getJSONObject(count++);
            String id = MovieDescription.getString("id").split("/")[2];
            String titleType = MovieDescription.getString("titleType");
            String title = MovieDescription.getString("title");

            // add rating !!
            Movie movie = Movie.newBuilder()
                    .setId(id)
                    .setTitletype(titleType)
                    .setTitle(title)
                    .build();
            return movie;
        }
        return null;
    }
}
