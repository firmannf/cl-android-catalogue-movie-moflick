package com.firmannf.moflick.util;

/**
 * Created by codelabs on 16/07/18.
 */

public class AppConstant {
    public static final String BASE_API_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIE_POPULAR_URL = BASE_API_URL + "movie/popular/";
    public static final String MOVIE_SEARCH_URL = BASE_API_URL + "search/movie/";
    public static final String MOVIE_UPCOMING_URL = BASE_API_URL + "movie/upcoming/";
    public static final String MOVIE_NOW_PLAYING_URL = BASE_API_URL + "movie/now_playing/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    public static int MOVIE_NOW_PLAYING_LOADER_ID = 101;
    public static int MOVIE_POPULAR_LOADER_ID = 102;
    public static int MOVIE_UPCOMING_LOADER_ID = 103;
    public static int MOVIE_SEARCH_LOADER_ID = 104;
    public static int MOVIE_LOVED_LOADER_ID = 105;

    public static String EXTRAS_MOVIE = "extras-movie";
    public static String EXTRAS_TITLE = "extras-title";
}