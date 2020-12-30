package api.actor;

import api.ApiResponse;
import api.movie.RatingMovie;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GetBornToday {

    private HttpResponse<JsonNode> response;
    private JSONArray dataArray;
    public String url = "https://imdb8.p.rapidapi.com/actors/list-born-today";
    List<String> dest = new ArrayList<String>();


    public GetBornToday(int month, int day) {
        url += "?month=" + month + "&day=" + day;
        response = ApiResponse.getResponseApi(url);
        dataArray = response.getBody().getArray();
    }

  

    public List<String> getAllBornToday() {
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                dest.add(dataArray.getString(i).split("/")[2]);
                System.out.println("Born today "+ dataArray.getString(i).split("/")[2]);
            }
        }
        return dest;
    }
    
    

    


}
