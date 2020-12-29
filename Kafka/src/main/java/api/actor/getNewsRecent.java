package api.actor;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;
import util.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class getNewsRecent {
    private HttpResponse<JsonNode> response;
    private JSONArray dataArray = null;
    //private static final String FOR_NEWS = "src/main/resources/News/StreamingNews.csv";
    idForNews idd = new idForNews();
    public String url = "https://imdb8.p.rapidapi.com/actors/get-all-news?nconst=";
    private int count = 0;
    private List<News> ns = new ArrayList<News>();

    public getNewsRecent() {
        for (int i = 0; i < idd.getFinalId().size(); i++) {
            url += idd.getFinalId().get(i);
            response = ApiResponse.getResponseApi(url);
            dataArray = (JSONArray) response.getBody().getArray().getJSONObject(0).get("items");
        }
    }

    public News getNextNews() {
        if (count < dataArray.length()) {
            JSONObject NewsDescription = dataArray.getJSONObject(count++);
            String body = NewsDescription.getString("body");
            String head = NewsDescription.getString("head");
            String id = NewsDescription.getString("id");
            String link = NewsDescription.getString("link");
            String publishtime = NewsDescription.getString("publishDateTime");
            News news = new News(body, head, id, link, publishtime);

            return news;
        }
        return null;
    }
}
