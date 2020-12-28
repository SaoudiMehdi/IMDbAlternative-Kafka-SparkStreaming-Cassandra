package api.actor;

import api.ApiResponse;
import api.movie.RatingMovie;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllFilmography {


    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    private static final String MOST_POPULAR_CB_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
    private static final String ALL_FILMO_PATH = "src/main/resources/movie_actor/allFilmography.csv";
    public String url = "https://imdb8.p.rapidapi.com/actors/get-all-filmography?nconst=";
    private int count = 0;
    private String id_actor;

    public AllFilmography(String id_actor) {
        url += id_actor;
        this.id_actor = id_actor;
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray().getJSONObject(0).get("filmography");
    }

    public Movie getNextMovie() {
        if(count < dataArray.length()){
            JSONObject MovieDescription = dataArray.getJSONObject(count++);
            String id = MovieDescription.getString("id").split("/")[2];
            String titleType = MovieDescription.getString("titleType");
            String title = MovieDescription.getString("title");

            double rating = new RatingMovie(id).getRating();
            Movie movie = new Movie(id, rating, titleType, title);
            return movie;
        }
        return null;
    }

    public String getNextString() {
        if(count < dataArray.length()){
            JSONObject MovieDescription = dataArray.getJSONObject(count++);
            String id = MovieDescription.getString("id").split("/")[2];
            return id;
        }
        return null;
    }

    public static void saveAlFilmography(){

        try {
            FileReader filereader = new FileReader(MOST_POPULAR_CB_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            FileWriter outputfile = null;
            File file = new File(ALL_FILMO_PATH);
            try {
                outputfile = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CSVWriter writer = new CSVWriter(outputfile);
            String id_movie;

            List<String[]> data = new ArrayList<String[]>();
            if(!file.exists())
                data.add(new String[] { "Actor_id", "Movie_id" });

            for (int i= 1; i<allData.size(); i++) {
                String id_actor = allData.get(i)[0];
                System.out.println(id_actor);
                AllFilmography allFilmography = new AllFilmography(id_actor);
                while((id_movie = allFilmography.getNextString()) !=null){
                    System.out.println(id_actor +" : "+id_movie);
                    data.add(new String[] {id_actor, id_movie });
                }


            }
            writer.writeAll(data);
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }
}
