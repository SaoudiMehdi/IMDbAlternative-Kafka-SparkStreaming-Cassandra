package api.movie;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.opencsv.CSVWriter;
import org.json.JSONArray;
import util.Movie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopRatedMovies {
    public String url = "https://imdb8.p.rapidapi.com/title/get-top-rated-movies";
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;
    private final String CSV_FILE_PATH = "src/main/resources/movie/topRatedMovies.csv";

    private int count = 0;

    public TopRatedMovies() {
        response = ApiResponse.getResponseApi(url);
        System.out.println(response);
        dataArray = response.getBody().getArray();
        System.out.println(dataArray);
    }

    public Movie getNextMovie() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.getJSONObject(count++).get("id")).split("/")[2];;
            System.out.println(id);
            Movie movie = new GetMetaData(id).getMovie();
            return movie;
        }
        return null;
    }

    public String getNextString() {
        if(count < dataArray.length()){
            String id = ((String) dataArray.getJSONObject(count++).get("id")).split("/")[2];
            System.out.println(dataArray.length()+" "+count);
            return id;
        }
        return null;
    }

    public void saveTopRatedMovies(){
        FileWriter outputfile = null;
        try {
            outputfile = new FileWriter(new File(CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVWriter writer = new CSVWriter(outputfile);

        String id;

        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "Ranking", "Movie_id" });
        while((id = getNextString()) !=null){
            System.out.println(count +" : "+id);
            data.add(new String[] {String.valueOf(count), id });
        }
        writer.writeAll(data);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
