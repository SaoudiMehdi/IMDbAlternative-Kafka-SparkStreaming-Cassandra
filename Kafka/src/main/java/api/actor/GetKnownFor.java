package api.actor;

import api.ApiResponse;
import api.movie.RatingMovie;
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
import java.util.List;

public class GetKnownFor {
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    private static final String MOST_POPULAR_CB_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
    private static final String ALL_KNOWN_FOR_PATH = "src/main/resources/movie_actor/actorKnownFor.csv";
    public String url = "https://imdb8.p.rapidapi.com/actors/get-known-for?nconst=";
    private int count = 0;
    private String id_actor;

    public GetKnownFor(String id_actor) {
        url += id_actor;
        this.id_actor = id_actor;
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray();
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
            JSONObject movieDescription = dataArray.getJSONObject(count++);
            System.out.println(movieDescription);
            String id_movie = movieDescription.getJSONObject("title").getString("id").split("/")[2];
            try{
                String titleType = movieDescription.getJSONObject("title").getString("titleType");
                if(titleType.toLowerCase().contains("movie")){
                    String category ="none";
                    if(movieDescription.has("summary") && movieDescription.getJSONObject("summary").has("category"))
                        category = movieDescription.getJSONObject("summary").getString("category");
                    else {
                        if(movieDescription.has("categories")){
                            JSONArray categArray = (JSONArray) movieDescription.get("categories");
                            int countCat = 0;
                            while( countCat < categArray.length()) {
                                category = ((String) categArray.get(countCat++));
                                if(category.contains("actor") || category.contains("actress"))
                                    break;
                            }
                        }
                    }

                    if(category.contains("actor") || category.contains("actress"))
                        return id_movie;
                    else{
                        System.out.println(id_actor+"Not actor : "+ id_movie);
                        return getNextString();
                    }
                }else{
                    System.out.println(id_actor+" Not movie : "+ id_movie);
                }
            }catch (JSONException e){
                System.err.println(id_movie);
                e.printStackTrace();
            }catch (ClassCastException e){
                System.err.println(id_movie);
                e.printStackTrace();
            }
        }
        if(count < dataArray.length()){
            return getNextString();
        }
        return null;
    }

    public static void saveAllKnownFor(){

        try {
            FileReader filereader = new FileReader(MOST_POPULAR_CB_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            FileWriter outputfile = null;
            File file = new File(ALL_KNOWN_FOR_PATH);
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

            int counter = 0;
            for (int i= 1; i<allData.size(); i++) {
                String id_actor = allData.get(i)[0];
                System.out.println(id_actor);
                GetKnownFor actorknownFor = new GetKnownFor(id_actor);
                while((id_movie = actorknownFor.getNextString()) !=null){
                    System.out.println(id_actor +" : "+id_movie);
                    data.add(new String[] {id_actor, id_movie });
                    counter++;
                }


            }
            writer.writeAll(data);
            System.out.println(counter);
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

}
