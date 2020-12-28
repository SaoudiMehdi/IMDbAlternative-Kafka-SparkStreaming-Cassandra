package api;

import api.actor.AllFilmography;
import api.actor.MostPopularClebs;
import api.movie.GetMetaData;
import api.movie.TopRatedMovies;
import util.Actor;
import util.Movie;

public class TestApi {

    public static void main(String[] args) {

        //TopRatedMovies topRatedMovies = new TopRatedMovies();
        //topRatedMovies.saveTopRatedMovies();

        //MostPopularClebs mostPopularClebs = new MostPopularClebs();
        //mostPopularClebs.saveTopRatedCelbs();

        //AllFilmography.saveAlFilmography();

        //GetMetaData.saveAlMovies();
        String API_KEYS[] = {
                "2787398dd7mshbb323b746979b1fp12f4a0jsn9b2ee9e88420",
                "6de49a8b6emsh4a8a294cbadd717p1c909bjsnd57e432650cb",
                "53b2201cf8msh551db147d054682p1feb8bjsn40c022266565",
                "a68b4f2876msh13379e835567ff6p17706bjsn3f5f786549a7",
                "4e7bbf9a79msh8020b2408f2c252p1c3cf4jsna92e11759d80",
                "e21695f5d2msh7aaa081e3cb8296p18b220jsnf6273ba406bc",
                "1a6436053cmshd347cfb997dede2p150a8fjsn1ec58a29ce96"};

        int index_api = 0;
        while(true){
            for(int i=index_api+1; i!=index_api ; i = (i+1)%API_KEYS.length) {
                System.out.println(i+" "+API_KEYS[i]);
            }
        }
    }
}
