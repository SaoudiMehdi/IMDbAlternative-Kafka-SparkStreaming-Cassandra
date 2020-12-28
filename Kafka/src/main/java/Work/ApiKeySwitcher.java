package Work;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class ApiKeySwitcher{

    String API_KEYS[] = {
            "6de49a8b6emsh4a8a294cbadd717p1c909bjsnd57e432650cb",
            "53b2201cf8msh551db147d054682p1feb8bjsn40c022266565",
            "a68b4f2876msh13379e835567ff6p17706bjsn3f5f786549a7",
            "4e7bbf9a79msh8020b2408f2c252p1c3cf4jsna92e11759d80",
            "e21695f5d2msh7aaa081e3cb8296p18b220jsnf6273ba406bc",
            "1a6436053cmshd347cfb997dede2p150a8fjsn1ec58a29ce96"};


    public ApiKeySwitcher() {

    }

    public void SwitchAPI() {
        // TODO Auto-generated method stub
        for(int i=0; i<API_KEYS.length ; i++) {
            try {
                if(searchingValidAPI(i)) break;
            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
    //Par exemple ici cette méthode pour récuperer les news, si tu veux collecter d'autres info, il faut changer créer une autre méthode et changer l'URL
    private synchronized boolean  searchingValidAPI(int i) throws UnirestException {
        // TODO Auto-generated method stub
        String url = "https://imdb8.p.rapidapi.com/actors/get-all-news?nconst=nm0001667";
        String api = "imdb8.p.rapidapi.com";
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("x-rapidapi-host", api)
                .header("x-rapidapi-key", API_KEYS[i])
                .asJson();
        if(!response.getBody().getArray().getJSONObject(0).has("message")) {
            NewsGetter.API_KEY = API_KEYS[i];
            return true;
        }
        System.out.println("api : "+NewsGetter.API_KEY );

        return false;
    }

}