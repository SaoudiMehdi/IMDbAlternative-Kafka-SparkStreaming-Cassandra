package api.actor;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Actor;
import util.Movie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MostPopularClebs {

    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    public static String url = "https://imdb8.p.rapidapi.com/actors/list-most-popular-celebs";
    private final String CSV_FILE_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
    private int count = 0;

    public MostPopularClebs() {
        response = ApiResponse.getResponseApi(url);
        dataArray = response.getBody().getArray();
    }


    public Actor getNextActor() {
        while(count < dataArray.length()) {
            String id = ((String) dataArray.get(count++)).split("/")[2];
            System.out.println(id);
            Actor actor = new Biography(id).getActor();
            if(actor == null){
                System.out.println("field requiered");
                return getNextActor();
            }
            System.out.println(actor);
            return actor;
        }
        return null;
    }


    public void saveTopRatedCelbs(){
        FileWriter outputfile = null;
        try {
            outputfile = new FileWriter(new File(CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVWriter writer = new CSVWriter(outputfile);

        Actor actor;
        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "id", "name", "birthDate", "birthPlace", "gender"});
        while((actor = getNextActor()) !=null){
            System.out.println(actor );
            data.add(actor.toArray());
        }
        writer.writeAll(data);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
