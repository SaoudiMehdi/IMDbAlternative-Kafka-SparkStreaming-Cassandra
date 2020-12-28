package api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ApiResponse {

    public static String API_KEY = "2787398dd7mshbb323b746979b1fp12f4a0jsn9b2ee9e88420";
    public static String api = "imdb8.p.rapidapi.com";
    public static String API_KEYS[] = {
            "2787398dd7mshbb323b746979b1fp12f4a0jsn9b2ee9e88420",
            "6de49a8b6emsh4a8a294cbadd717p1c909bjsnd57e432650cb",
            "53b2201cf8msh551db147d054682p1feb8bjsn40c022266565",
            "a68b4f2876msh13379e835567ff6p17706bjsn3f5f786549a7",
            "4e7bbf9a79msh8020b2408f2c252p1c3cf4jsna92e11759d80",
            "e21695f5d2msh7aaa081e3cb8296p18b220jsnf6273ba406bc",
            "1a6436053cmshd347cfb997dede2p150a8fjsn1ec58a29ce96"};

    public static int index_api = 0;


    public synchronized  static HttpResponse<JsonNode> getResponseApi(String url)  {
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(url)
                    .header("x-rapidapi-host", api)
                    .header("x-rapidapi-key", API_KEY)
                    .asJson();

            System.out.println("1"+response.getBody());
            if (response==null  || (response.getBody().getArray().get(0) instanceof JSONObject && response.getBody().getArray().getJSONObject(0).has("message"))) {
                System.out.println("2"+response);
                response = SwitchAPI(url);

            }
            System.out.println("3"+response);
            return response;

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println("4");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getResponseApi(url);
    }

    public static HttpResponse<JsonNode> SwitchAPI(String url) {
        // TODO Auto-generated method stub
        System.out.println("5");
        for(int i=index_api+1; i!=index_api ; i = (i+1)%API_KEYS.length) {
            try {
                HttpResponse<JsonNode> response = Unirest.get(url)
                        .header("x-rapidapi-host", api)
                        .header("x-rapidapi-key", API_KEYS[i])
                        .asJson();
                System.out.println("6"+response.getBody()+" "+i);
                if (!(response==null  || (response.getBody().getArray().get(0) instanceof JSONObject && response.getBody().getArray().getJSONObject(0).has("message")))) {
                    API_KEY = API_KEYS[i];
                    index_api = i;
                    return response;
                }
                System.out.println("6bis"+response+" "+i);
            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


}
