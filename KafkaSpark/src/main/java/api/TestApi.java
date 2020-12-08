package api;

import api.title.*;

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
         */

        //title
        //System.out.println(new ComingSoonTvShows().getNextTvShow());
        //new MoreLikeThisMovie("tt13310164");
        //new PopularGenres().getNextGenre();
        System.out.println(new RatingMovie("tt0111161").getRating());
        //System.out.println(new TopRatedMovies().getNextMovie());
    }
}
