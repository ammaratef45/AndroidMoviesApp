package com.life.ammar.movies;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MovieEntry extends RealmObject {
@SerializedName("adult")
@Expose
private boolean adult;
@SerializedName("backdrop_path")
@Expose
private String backdropPath;
@PrimaryKey
@SerializedName("id")
@Expose
private int id;
@SerializedName("original_language")
@Expose
private String originalLanguage;
@SerializedName("original_title")
@Expose
private String originalTitle;
@SerializedName("overview")
@Expose
private String overview;
@SerializedName("popularity")
@Expose
private double popularity;
@SerializedName("poster_path")
@Expose
private String posterPath;
@SerializedName("release_date")
@Expose
private String releaseDate;
@SerializedName("title")
@Expose
private String title;
@SerializedName("video")
@Expose
private boolean video;
@SerializedName("vote_average")
@Expose
private double voteAverage;
@SerializedName("vote_count")
@Expose
private int voteCount;
private int type; // 0 for popularity and 1 for votes
private boolean favourite;
public MovieEntry() {}

public MovieEntry(boolean adult, String backdropPath, int id,
                  String originalLanguage, String originalTitle, String overview,
                  double popularity, String posterPath, String releaseDate, String title,
                  boolean video, double voteAverage, int voteCount, int type) {
    this.adult = adult;
    this.backdropPath = backdropPath;
    this.id = id;
    this.originalLanguage = originalLanguage;
    this.originalTitle = originalTitle;
    this.overview = overview;
    this.popularity = popularity;
    this.posterPath = posterPath;
    this.releaseDate = releaseDate;
    this.title = title;
    this.video = video;
    this.voteAverage = voteAverage;
    this.voteCount = voteCount;
    this.type = type;
}


public boolean isAdult() {
    return adult;
}


public void setAdult(boolean adult) {
    this.adult = adult;
}

public String getBackdropPath() {
    return backdropPath;
}

public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
}

public int getId() {
    return id;
}


public void setId(int id) {
    this.id = id;
}


public String getOriginalLanguage() {
    return originalLanguage;
}
public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
}

public String getOriginalTitle() {
    return originalTitle;
}

public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
}

public String getOverview() {
    return overview;
}

public void setOverview(String overview) {
    this.overview = overview;
}

public double getPopularity() {
    return popularity;
}

public void setPopularity(double popularity) {
    this.popularity = popularity;
}

public String getPosterPath() {
    return posterPath;
}

public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
}

public String getReleaseDate() {
    return releaseDate;
}

public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
}

public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
}

public boolean isVideo() {
    return video;
}

public void setVideo(boolean video) {
    this.video = video;
}

public double getVoteAverage() {
    return voteAverage;
}

public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
}

public int getVoteCount() {
    return voteCount;
}

public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
}

public void setType(int type) {
    this.type = type;
}

public int getType() {
    return type;
}

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean getFavourite() {
        return favourite;
    }

}
