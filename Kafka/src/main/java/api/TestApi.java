package api;

import api.actor.AllFilmography;
import api.actor.MostPopularClebs;
import api.movie.GetMetaData;
import api.movie.TopRatedMovies;
import util.Actor;
import util.Movie;

public class TestApi {

    public static void main(String[] args) {
        //Actor
        //new MostPopularClebs();
        //System.out.println(new Biography("nm0001667").getActor());
        /*MostPopularClebs m = new MostPopularClebs();
        String id;
        while((id = m.getNextActor()) !=null){
            System.out.println(id);
        }

        AllFilmography allFilmography = new AllFilmography("nm0001667");
        Movie movie;
        while((movie = allFilmography.getNextMovie()) !=null){
            System.out.println(movie);
        }*/

        /*MostPopularClebs mostPopularClebs = new MostPopularClebs();
        Actor actor;

        while ((actor = mostPopularClebs.getNextActor()) != null) {
            System.out.println(actor);
        }*/

        TopRatedMovies topRatedMovies = new TopRatedMovies();
        Movie movie;
        while((movie = topRatedMovies.getNextMovie()) !=null){
            System.out.println(movie);
        }


        //movie
        //new GetMetaData("tt4154756").getMovie();
        //System.out.println(new ComingSoonTvShows().getNextTvShow());
        //new MoreLikeThisMovie("tt13310164");
        //new PopularGenres().getNextGenre();
        //System.out.println(new RatingMovie("tt0111161").getRating());
        //System.out.println(new TopRatedMovies().getNextMovie());
    }
}
