package Work;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import util.News;

import java.util.ArrayList;

public class NewsGetter {
    public static ArrayList<News> NEWS_LIST = new ArrayList<News>();
    public static String API_KEY = "1a6436053cmshd347cfb997dede2p150a8fjsn1ec58a29ce96";

    public ArrayList<News> getNews(String body, String head, String id, String link, String publishTime) throws UnirestException, InterruptedException{

        ApiKeySwitcher api_switch = new ApiKeySwitcher();
        //api_switch.SwitchAPI();



        //creating request information
        String url;
        String api;

        url = "https://imdb8.p.rapidapi.com/actors/get-all-news?nconst=nm0001667";
        System.out.println("my url : "+ url);
        api = "imdb8.p.rapidapi.com";
        if(!API_KEY.isEmpty()) {
            HttpResponse<JsonNode> response;
            try {
                response = Unirest.get(url)
                        .header("x-rapidapi-host", api)
                        .header("x-rapidapi-key", NewsGetter.API_KEY)
                        .asJson();

                if(response.getBody().getArray().getJSONObject(0).has("message")) {
                    api_switch.SwitchAPI();
                    response = Unirest.get(url)
                            .header("x-rapidapi-host", api)
                            .header("x-rapidapi-key", NewsGetter.API_KEY)
                            .asJson();

                }else {
                    //Thread fill_thread = new FillingHotelInfo(response);
                    //fill_thread.start();
                    fillInNews(response);

                }
            } catch (UnirestException e) {
                // TODO Auto-generated catch block

            }
        }
        //if(i%4==0 && i!=0) TimeUnit.SECONDS.sleep(1);


        return NEWS_LIST;
    }
    private void fillInNews(HttpResponse<JsonNode> response) {
        // TODO Auto-generated method stub

        //A changer selon ce que tu désires
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

    }
}
