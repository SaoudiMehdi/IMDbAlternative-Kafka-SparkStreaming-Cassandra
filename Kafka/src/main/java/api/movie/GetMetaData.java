package api.movie;

import api.ApiResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Movie;

public class GetMetaData {
    /*HttpResponse<String> response = Unirest.get("=tt4154756&")
            .header("x-rapidapi-key", "2787398dd7mshbb323b746979b1fp12f4a0jsn9b2ee9e88420")
            .header("x-rapidapi-host", "imdb8.p.rapidapi.com")
            .asString();*/

    private HttpResponse<JsonNode> response;
    private JSONObject dataJson = null;
    public static String url = "https://imdb8.p.rapidapi.com/title/get-meta-data?ids=";

    private int count = 0;
    private String id_movie;

    public GetMetaData(String id_movie) {
        this.id_movie = id_movie;
        url += id_movie;
        response = ApiResponse.getResponseApi(url);
        dataJson = (JSONObject) response.getBody().getObject();

    }

    public Movie getMovie() {
        if(!dataJson.isNull(id_movie)){
            dataJson = (JSONObject) dataJson.get(id_movie);

            String id = id_movie;
            String titleType = dataJson.getJSONObject("title").getString("titleType");
            String title = dataJson.getJSONObject("title").getString("title");
            double rating = 0;
            if(dataJson.getJSONObject("ratings").getBoolean("canRate"))
                rating = dataJson.getJSONObject("ratings").getDouble("rating");
            Movie movie = new Movie(id, rating, titleType, title);
            System.out.println(movie);
            return movie;
        }
        return null;
    }
}
