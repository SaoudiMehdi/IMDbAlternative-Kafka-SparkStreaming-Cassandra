package api;

import api.actor.AllFilmography;
import api.actor.GetKnownFor;
import api.actor.MostPopularClebs;
import api.movie.GetMetaData;
import api.movie.TopRatedMovies;
import util.Actor;
import util.Movie;

import java.io.File;

public class TestApi {

    public static void main(String[] args) {

        //TopRatedMovies topRatedMovies = new TopRatedMovies();
        //topRatedMovies.saveTopRatedMovies();

        //MostPopularClebs mostPopularClebs = new MostPopularClebs();
        //mostPopularClebs.saveTopRatedCelbs();

        //AllFilmography.saveAlFilmography();

        GetMetaData.saveAlMovies();

        //GetKnownFor.saveAllKnownFor();


    }
}
