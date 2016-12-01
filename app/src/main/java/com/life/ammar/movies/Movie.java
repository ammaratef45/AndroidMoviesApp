package com.life.ammar.movies;

import java.net.URL;

/**
 * Created by ammar on 07/03/16.
 */
public class Movie {
    private URL url;
    private int id;

    public Movie (URL _url, int _id) {
        url = _url;
        id = _id;
    }

    public URL getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }
}
