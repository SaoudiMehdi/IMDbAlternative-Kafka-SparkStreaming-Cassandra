package api.actor;

import Work.ApiKeySwitcher;
import Work.NewsGetter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import util.News;

import java.util.ArrayList;

public class BirthdayNews {

    private HttpResponse<JsonNode> response;
    private JSONObject ActorDescription;
    private final String url = "https://imdb8.p.rapidapi.com/actors/get-all-news?nconst=";
    private String urlCourant;

    public BirthdayNews(String id_actor, int year){

    }


    /*public ArrayList<News> getNews(String body, String head, String id, String link, String publishTime) throws UnirestException, InterruptedException{

        String api;


        System.out.println("my url : "+ url);
        api = "imdb8.p.rapidapi.com";



        //if(i%4==0 && i!=0) TimeUnit.SECONDS.sleep(1);


        return NEWS_LIST;
    }

    private void fillInNews(HttpResponse<JsonNode> response) {

        JSONArray data = response.getBody().getArray().getJSONObject(0).getJSONArray("data");
        int dataLength = data.length();
        //limiter a 9 pour le test
        int dataNumber = 9;
        if(dataLength<dataNumber) dataNumber = dataLength;
        System.out.println("dataLenght : "+dataNumber);
        for(int i=0;i<dataNumber;i++) {

            System.out.println(i);
            JSONObject newsList = data.getJSONObject(i);
            //1
            String body = newsList.getString("body");

            //2
            String head = newsList.getString("head");

            //3
            String id = newsList.getString("id");

            //4
            String link = newsList.getString("link");

            //5
            String publishTime = newsList.getString("publishTime");

        }

    }*/
}
