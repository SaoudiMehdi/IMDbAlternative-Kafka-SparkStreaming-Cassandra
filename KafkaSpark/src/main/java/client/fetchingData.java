package client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import util.RatingApi;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class fetchingData {
    //Connection to IMDb APi
    public static String API_KEY = "3b05458d0cmsh5d99c67eb7708cep1b8116jsnc9eb6470d605";
    public static ArrayList<RatingApi> Movie_LIST = new ArrayList<RatingApi>();
    public static String url = "https://imdb8.p.rapidapi.com/title/get-top-rated-tv-shows";
    public static String api = "imdb8.p.rapidapi.com";

    public ArrayList<RatingApi> getMovies() throws UnirestException, InterruptedException {
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
                    fillInMovies(response);

                }
            } catch (UnirestException e) {
                // TODO Auto-generated catch block

            }
        }
        return Movie_LIST;
    }

    public void fillInMovies(HttpResponse<JsonNode> response) {

        JSONObject data = response.getBody().getArray().getJSONObject(0);

        int dataLength = data.length();
        int dataNumber = 9;
        if (dataLength < dataNumber) dataNumber = dataLength;
        System.out.println("dataLenght : " + dataNumber);
        for (int i = 0; i < 90; i++) {
            JSONObject MovieDescription = response.getBody().getArray().getJSONObject(i);

            String id = MovieDescription.getString("id");
            int chart = MovieDescription.getInt("chartRating");
                /*
                    String titleType = MovieDescription.getString("titleType");
                    String year = MovieDescription.getString("year");
                */
            //String image_link = hotelDescription.getJSONObject("photo").getJSONObject("images").getJSONObject("medium").getString("url");
            Movie_LIST.add(new RatingApi(id, chart));
        }


    }
    public static void close() {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
