package api.actor;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Actor;
import util.Movie;

public class Biography {

    private HttpResponse<JsonNode> response;
    private JSONObject ActorDescription;
    public static String url = "https://imdb8.p.rapidapi.com/actors/get-bio?nconst=";

    public Biography(String id_actor) {
        url += id_actor;
        response = ApiResponse.getResponseApi(url);

    }

    public Actor getActor() {
        if(response != null){
            ActorDescription = response.getBody().getObject();
            String id = ActorDescription.getString("id").split("/")[2];
            String birthDate = ActorDescription.getString("birthDate");
            String birthPlace = ActorDescription.getString("birthPlace");
            String name = ActorDescription.getString("name");
            String realName = ActorDescription.getString("realName");
            String gender = ActorDescription.getString("gender");

            Actor actor = new Actor(id, name, birthDate, birthPlace, gender, realName);
            return actor;
        }return null;
    }
}
