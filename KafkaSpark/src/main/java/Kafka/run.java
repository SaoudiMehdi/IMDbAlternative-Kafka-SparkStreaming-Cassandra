package Kafka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import util.Movie;

import java.util.ArrayList;

public class run {
  /*  //Connection to IMDb APi
    public static String API_KEY = "3b05458d0cmsh5d99c67eb7708cep1b8116jsnc9eb6470d605";
    public static ArrayList<Movie> Movie_LIST = new ArrayList<Movie>();*/

    public static void main(String[] args) {
        /*//creating request information
        String url;
        String api;
        //String country = "US";

        url = "https://imdb8.p.rapidapi.com/title/get-top-rated-tv-shows";
        System.out.println("my url : " + url);
        api = "imdb8.p.rapidapi.com";
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
                    connectAPI.fillInMovies(response);

                }
            } catch (UnirestException e) {
                // TODO Auto-generated catch block

            }

        }*/
    }
}
