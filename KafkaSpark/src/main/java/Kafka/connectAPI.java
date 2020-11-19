package Kafka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

import java.util.ArrayList;

public class connectAPI {

        //Connection to IMDb APi
        public static String API_KEY = "3b05458d0cmsh5d99c67eb7708cep1b8116jsnc9eb6470d605";
        public static ArrayList<Movie> Movie_LIST = new ArrayList<Movie>();

        public ArrayList<Movie> getMovies () throws UnirestException, InterruptedException {
            //ApiKeySwitcher api_switch = new ApiKeySwitcher();
            //api_switch.SwitchAPI();


            //creating request information
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
                        fillInMovies(response);

                    }
                } catch (UnirestException e) {
                    // TODO Auto-generated catch block

                }
            }


            return Movie_LIST;
        }

        public void fillInMovies(HttpResponse<JsonNode> response) {
            // TODO Auto-generated method stub
            //Je dois corriger hadi pour récuperer length wndirha fl boucle for
            JSONObject data = response.getBody().getArray().getJSONObject(0);
            int dataLength = data.length();
            int dataNumber = 9;
            if (dataLength < dataNumber) dataNumber = dataLength;
            System.out.println("dataLenght : " + dataNumber);
            for (int i = 0; i < 90; i++) {

                JSONObject MovieDescription = response.getBody().getArray().getJSONObject(i);
                //1
                String id = MovieDescription.getString("id");

                //2
                int chart = MovieDescription.getInt("chartRating");
/*
            //3
            String titleType = MovieDescription.getString("titleType");

            //4
            String year = MovieDescription.getString("year");
*/
                //String image_link = hotelDescription.getJSONObject("photo").getJSONObject("images").getJSONObject("medium").getString("url");
                Movie_LIST.add(new Movie(id, chart));
            }
            for(Movie m : Movie_LIST){
                System.out.println(m.getId());
            }

        }

    }

