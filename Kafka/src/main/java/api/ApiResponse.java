package api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ApiResponse {

    public static String API_KEY = "";
    public static String api = "imdb8.p.rapidapi.com";

    public static String API_KEYS[] = {
            
			};

    public static int index_api = 0;


    public synchronized  static HttpResponse<JsonNode> getResponseApi(String url)  {
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(url)
                    .header("x-rapidapi-host", api)
                    .header("x-rapidapi-key", API_KEY)
                    .asJson();

            //System.out.println("1"+response.getBody());
            if (response==null  || (response.getBody().getArray().get(0) instanceof JSONObject && response.getBody().getArray().getJSONObject(0).has("message"))) {
                //System.out.println("2"+response);
                response = SwitchAPI(url);

            }
            //System.out.println("3"+response);
            return response;

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        //System.out.println("4");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getResponseApi(url);
    }

    public static HttpResponse<JsonNode> SwitchAPI(String url) {
        //System.out.println("5");
        for(int i=index_api+1; i!=index_api ; i = (i+1)%API_KEYS.length) {
            try {
                HttpResponse<JsonNode> response = Unirest.get(url)
                        .header("x-rapidapi-host", api)
                        .header("x-rapidapi-key", API_KEYS[i])
                        .asJson();
                //System.out.println("6"+response.getBody()+" "+i);
                if (!(response==null  || (response.getBody().getArray().get(0) instanceof JSONObject && response.getBody().getArray().getJSONObject(0).has("message")))) {
                    API_KEY = API_KEYS[i];
                    index_api = i;
                    return response;
                }
                //System.out.println("6bis"+response+" "+i);
            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


}
