package com.life.ammar.movies;

/**
 * Created by ammar on 15/04/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MovieTrailer extends RealmObject {

    private int movieId;
    @PrimaryKey
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieTrailer() {}

    /**
     *
     * @param key
     * @param name
     * @param type
     * @param size
     */
    public MovieTrailer(String name, String size, String key, String type) {
        this.name = name;
        this.size = size;
        this.key = key;
        this.type = type;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The size
     */
    public String getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The source
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The source
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

}
