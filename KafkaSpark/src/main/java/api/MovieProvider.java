package api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

import java.util.ArrayList;

public class MovieProvider {

    private int count = 0;
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    private int countMovies = 0;

    //Connection to IMDb APi
    public static String API_KEY = "3b05458d0cmsh5d99c67eb7708cep1b8116jsnc9eb6470d605";
    public static ArrayList<Movie> Movie_LIST = new ArrayList<Movie>();
    public static String url = "https://imdb8.p.rapidapi.com/title/get-top-rated-tv-shows";
    public static String api = "imdb8.p.rapidapi.com";

    public static MovieProvider movieProvider;

    private MovieProvider() {
        getResponse();
    }

    public static MovieProvider getMovieProvider(){
        if(movieProvider == null)
            movieProvider = new MovieProvider();
        return movieProvider;
    }

    public void getResponse(){
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
                    this.response = response;
                    dataArray = response.getBody().getArray();
                    countMovies = 90;


                }
            } catch (UnirestException e) {

            }
        }
    }


    public Movie getNext() {
        if(count < countMovies){
            JSONObject MovieDescription = dataArray.getJSONObject(count++);
            String id = MovieDescription.getString("id");
            int rating = MovieDescription.getInt("chartRating");
            Movie movie = Movie.newBuilder()
                    .setId(id)
                    .setRate(rating)
                    .build();
            return movie;
        }
        return null;
    }

}
