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
        ActorDescription = response.getBody().getObject();
    }

    public Actor getActor() {

        String id = ActorDescription.getString("id").split("/")[2];
        String birthDate = ActorDescription.getString("birthDate");
        String birthPlace = ActorDescription.getString("birthPlace");
        String name = ActorDescription.getString("name");
        String realName = ActorDescription.getString("realName");
        String gender = ActorDescription.getString("gender");

        Actor actor = Actor.newBuilder()
                .setId(id)
                .setBirthDate(birthDate)
                .setBirthPlace(birthPlace)
                .setName(name)
                .setReaName(realName)
                .setGender(gender)
                .build();
        return actor;
    }
}
