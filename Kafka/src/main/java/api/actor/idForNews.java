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

public class idForNews {
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    private static final String FOR_NEWS = "src/main/resources/News/StreamingNews.csv";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String formatted = df.format(new Date());
    String[] parts = formatted.split("-");
    String day = parts[2];
    String month = parts[1];
    public String url = "https://imdb8.p.rapidapi.com/actors/list-born-today?month=" + month + "&day=" + day + "";
    private int count = 0;
    ArrayList<String> toArray = new ArrayList<String>();
    ArrayList<String> dest = new ArrayList<String>();

    public idForNews() {
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray();
    }

  

    public ArrayList<String> getFinalId() {
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                toArray.add(dataArray.getString(i));
            }
        }
        for (int j = 0; j < toArray.size(); j++) {
            String[] parts = toArray.get(0).split("/");
            dest.add(parts[2]);
        }
        return dest;
    }
    
    

    


}
