package api.actor;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Movie;
import util.News;
import util.SDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetRecentNews {
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    public String url = "https://imdb8.p.rapidapi.com/actors/get-all-news?nconst=";
    private int count = 0;
    private String id_actor;

    public GetRecentNews(String id_actor) {
        this.id_actor = id_actor;
        url += id_actor;
        response = ApiResponse.getResponseApi(url);
        dataArray = (JSONArray) response.getBody().getArray().getJSONObject(0).get("items");
    }

    public News getNextNews(SDate dateCourante) {
        if (count < dataArray.length()) {
            try {
                JSONObject NewsDescription = dataArray.getJSONObject(count++);
                String publishtime = NewsDescription.getString("publishDateTime");
                publishtime = dateCourante.toString() + "T" +publishtime.split("T")[1];
                System.out.println(publishtime);
                String body = NewsDescription.getString("body");
                String head = NewsDescription.getString("head");
                String id = NewsDescription.getString("id").split("/")[3];
                String link = NewsDescription.getString("link");
                News news = new News(body, head, id, link, publishtime, id_actor);
                return news;
            }catch (JSONException e){
                e.printStackTrace();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }
        if (count < dataArray.length()) {
            return getNextNews(dateCourante);
        }
        return null;
    }
}
