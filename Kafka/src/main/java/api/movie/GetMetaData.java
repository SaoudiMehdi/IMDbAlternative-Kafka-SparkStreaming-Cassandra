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
    private static final String ALL_KNOWN_FOR_PATH2 = "src/main/resources/movie_actor/actorKnownFor_v2.csv";
    private static final String TOP_RATED_PATH = "src/main/resources/movie/topRatedMovies.csv";
    private static final String TOP_RATED_PATH2 = "src/main/resources/movie/topRatedMovies_v2.csv";
    private static final String MOVIES_PATH = "src/main/resources/movie/knownMovies.csv";
    private static final String MOST_POPULAR_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
    private static final String MOST_POPULAR_PATH2 = "src/main/resources/actor/mostPopularCelebs2.csv";


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


    public static void deleteMoviesNotInList(){
        Map<String, Integer> allMovies = new HashMap<>();
        try {
            FileReader filereader = new FileReader(MOVIES_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                allMovies.put(allData.get(i)[0], 1);
            }

            filereader = new FileReader(TOP_RATED_PATH);
            csvReader = new CSVReader(filereader);
            allData = csvReader.readAll();
            List<String[]> topRatedData = new ArrayList<>();
            topRatedData.add(allData.get(0));
            for (int i= 1; i<allData.size(); i++) {
                if(allMovies.containsKey(allData.get(i)[1]))
                    topRatedData.add(allData.get(i));
                else{
                    System.out.println(allData.get(i));
                    System.out.println(allData.get(i)[0]+" : 0 : "+allData.get(i)[1]);
                }
            }


            FileWriter outputfile = null;
            File file = new File(TOP_RATED_PATH2);
            try {
                outputfile = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeAll(topRatedData);
            writer.close();

            filereader = new FileReader(ALL_KNOWN_FOR_PATH);
            csvReader = new CSVReader(filereader);
            allData = csvReader.readAll();
            List<String[]> actorKnownFor = new ArrayList<>();
            actorKnownFor.add(allData.get(0));
            for (int i= 1; i<allData.size(); i++) {
                if(allMovies.containsKey(allData.get(i)[1]))
                    actorKnownFor.add(allData.get(i));
                else{
                    System.out.println(allData.get(i)[0]+" : 1 : "+allData.get(i)[1]);
                }
            }


            outputfile = null;
            file = new File(ALL_KNOWN_FOR_PATH2);
            try {
                outputfile = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = new CSVWriter(outputfile);
            writer.writeAll(actorKnownFor);
            writer.close();




        } catch (IOException ioException) {
            ioException.printStackTrace();
        }



    }

    public static void deleteCelbsWithoutMovie(){
        Map<String, Integer> allActors = new HashMap<>();
        try {
            FileReader filereader = new FileReader(ALL_KNOWN_FOR_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();

            for (int i= 1; i<allData.size(); i++) {
                allActors.put(allData.get(i)[0], 1);
            }



            filereader = new FileReader(MOST_POPULAR_PATH);
            csvReader = new CSVReader(filereader);
            allData = csvReader.readAll();
            List<String[]> celbsActors = new ArrayList<>();
            celbsActors.add(allData.get(0));
            for (int i= 1; i<allData.size(); i++) {
                if(allActors.containsKey(allData.get(i)[0]))
                    celbsActors.add(allData.get(i));
                else{
                    System.out.println(allData.get(i)[0]);
                }
            }


            FileWriter outputfile = null;
            File file = new File(MOST_POPULAR_PATH2);
            try {
                outputfile = new FileWriter(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeAll(celbsActors);
            writer.close();


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }



    }

}
