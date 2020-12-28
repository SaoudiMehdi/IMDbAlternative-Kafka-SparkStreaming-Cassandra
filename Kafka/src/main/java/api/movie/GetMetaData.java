package api.movie;

import api.ApiResponse;
import api.actor.AllFilmography;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Movie;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMetaData {
    private HttpResponse<JsonNode> response;
    private JSONObject dataJson = null;
    public String url = "https://imdb8.p.rapidapi.com/title/get-meta-data?ids=";
    private static final String ALL_KNOWN_FOR_PATH = "src/main/resources/movie_actor/actorKnownFor.csv";
    private static final String TOP_RATED_PATH = "src/main/resources/movie/topRatedMovies.csv";
    private static final String MOVIES_PATH = "src/main/resources/movie/knownMovies.csv";

    private int count = 0;
    private String id_movie;

    public GetMetaData(String id_movie) {
        this.id_movie = id_movie;
        url += id_movie;
        response = ApiResponse.getResponseApi(url);
        dataJson = (JSONObject) response.getBody().getObject();

    }

    public Movie getMovie() {
        dataJson = (JSONObject) dataJson.get(id_movie);
        String id = id_movie;
        try {
            String titleType = dataJson.getJSONObject("title").getString("titleType");
            if(titleType.toLowerCase().contains("movie")){
                String title = dataJson.getJSONObject("title").getString("title");
                double rating = 0;
                Boolean canRate = dataJson.getJSONObject("ratings").getBoolean("canRate");
                if(canRate)
                    rating = dataJson.getJSONObject("ratings").getDouble("rating");

                String releaseDate = (String) dataJson.get("releaseDate");
                int runningTimeInMinutes = dataJson.getJSONObject("title").getInt("runningTimeInMinutes");
                Movie movie = new Movie(id, rating, titleType, title, canRate, releaseDate, runningTimeInMinutes);
                System.out.println(movie);
                return movie;
            }else{
                System.out.println(id+" not a movie : "+titleType);
            }
        }catch (JSONException e){
            System.err.println(id);
            e.printStackTrace();
        }catch (ClassCastException e){
            System.err.println(id);
            e.printStackTrace();
        }
        return null;
    }


    public static void saveAlMovies(){

        Map<String, Integer> allMovies = new HashMap<>();
        try {
            FileReader filereader = new FileReader(ALL_KNOWN_FOR_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                allMovies.put(allData.get(i)[1], 1);
            }

            filereader = new FileReader(TOP_RATED_PATH);
            csvReader = new CSVReader(filereader);
            allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                allMovies.put(allData.get(i)[1], 1);
            }

            System.out.println(allMovies.keySet().size());

            FileWriter outputfile = null;
            File file = new File(MOVIES_PATH);
            try {
                outputfile = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();

            data.add(new String[] { "id", "rate", "title", "canRate", "releaseDate", "runningTimeInMinutes" });

            for(String id_movie : allMovies.keySet()){
                System.out.println(id_movie);
                GetMetaData movie_data = new GetMetaData(id_movie);
                Movie movie = movie_data.getMovie();
                if(movie != null){
                    data.add(movie.toArray());
                }
            }
            writer.writeAll(data);
            writer.close();




        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

}
